import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ILead } from 'app/entities/lead/lead.model';
import { LeadService } from 'app/entities/lead/service/lead.service';
import { IUserProfile } from 'app/entities/user-profile/user-profile.model';
import { UserProfileService } from 'app/entities/user-profile/service/user-profile.service';
import { PaymentService } from '../service/payment.service';
import { IPayment } from '../payment.model';
import { PaymentFormGroup, PaymentFormService } from './payment-form.service';

@Component({
  selector: 'jhi-payment-update',
  templateUrl: './payment-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PaymentUpdateComponent implements OnInit {
  isSaving = false;
  payment: IPayment | null = null;

  leadsSharedCollection: ILead[] = [];
  userProfilesSharedCollection: IUserProfile[] = [];

  protected paymentService = inject(PaymentService);
  protected paymentFormService = inject(PaymentFormService);
  protected leadService = inject(LeadService);
  protected userProfileService = inject(UserProfileService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PaymentFormGroup = this.paymentFormService.createPaymentFormGroup();

  compareLead = (o1: ILead | null, o2: ILead | null): boolean => this.leadService.compareLead(o1, o2);

  compareUserProfile = (o1: IUserProfile | null, o2: IUserProfile | null): boolean => this.userProfileService.compareUserProfile(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ payment }) => {
      this.payment = payment;
      if (payment) {
        this.updateForm(payment);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const payment = this.paymentFormService.getPayment(this.editForm);
    if (payment.id !== null) {
      this.subscribeToSaveResponse(this.paymentService.update(payment));
    } else {
      this.subscribeToSaveResponse(this.paymentService.create(payment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPayment>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(payment: IPayment): void {
    this.payment = payment;
    this.paymentFormService.resetForm(this.editForm, payment);

    this.leadsSharedCollection = this.leadService.addLeadToCollectionIfMissing<ILead>(this.leadsSharedCollection, payment.lead);
    this.userProfilesSharedCollection = this.userProfileService.addUserProfileToCollectionIfMissing<IUserProfile>(
      this.userProfilesSharedCollection,
      payment.user,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.leadService
      .query()
      .pipe(map((res: HttpResponse<ILead[]>) => res.body ?? []))
      .pipe(map((leads: ILead[]) => this.leadService.addLeadToCollectionIfMissing<ILead>(leads, this.payment?.lead)))
      .subscribe((leads: ILead[]) => (this.leadsSharedCollection = leads));

    this.userProfileService
      .query()
      .pipe(map((res: HttpResponse<IUserProfile[]>) => res.body ?? []))
      .pipe(
        map((userProfiles: IUserProfile[]) =>
          this.userProfileService.addUserProfileToCollectionIfMissing<IUserProfile>(userProfiles, this.payment?.user),
        ),
      )
      .subscribe((userProfiles: IUserProfile[]) => (this.userProfilesSharedCollection = userProfiles));
  }
}
