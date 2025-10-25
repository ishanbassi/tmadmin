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
import { ITrademarkPlan } from 'app/entities/trademark-plan/trademark-plan.model';
import { TrademarkPlanService } from 'app/entities/trademark-plan/service/trademark-plan.service';
import { ITrademarkClass } from 'app/entities/trademark-class/trademark-class.model';
import { TrademarkClassService } from 'app/entities/trademark-class/service/trademark-class.service';
import { HeadOffice } from 'app/entities/enumerations/head-office.model';
import { TrademarkStatus } from 'app/entities/enumerations/trademark-status.model';
import { TrademarkType } from 'app/entities/enumerations/trademark-type.model';
import { TrademarkSource } from 'app/entities/enumerations/trademark-source.model';
import { TrademarkService } from '../service/trademark.service';
import { ITrademark } from '../trademark.model';
import { TrademarkFormGroup, TrademarkFormService } from './trademark-form.service';

@Component({
  selector: 'jhi-trademark-update',
  templateUrl: './trademark-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TrademarkUpdateComponent implements OnInit {
  isSaving = false;
  trademark: ITrademark | null = null;
  headOfficeValues = Object.keys(HeadOffice);
  trademarkStatusValues = Object.keys(TrademarkStatus);
  trademarkTypeValues = Object.keys(TrademarkType);
  trademarkSourceValues = Object.keys(TrademarkSource);

  leadsSharedCollection: ILead[] = [];
  userProfilesSharedCollection: IUserProfile[] = [];
  trademarkPlansSharedCollection: ITrademarkPlan[] = [];
  trademarkClassesSharedCollection: ITrademarkClass[] = [];

  protected trademarkService = inject(TrademarkService);
  protected trademarkFormService = inject(TrademarkFormService);
  protected leadService = inject(LeadService);
  protected userProfileService = inject(UserProfileService);
  protected trademarkPlanService = inject(TrademarkPlanService);
  protected trademarkClassService = inject(TrademarkClassService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TrademarkFormGroup = this.trademarkFormService.createTrademarkFormGroup();

  compareLead = (o1: ILead | null, o2: ILead | null): boolean => this.leadService.compareLead(o1, o2);

  compareUserProfile = (o1: IUserProfile | null, o2: IUserProfile | null): boolean => this.userProfileService.compareUserProfile(o1, o2);

  compareTrademarkPlan = (o1: ITrademarkPlan | null, o2: ITrademarkPlan | null): boolean =>
    this.trademarkPlanService.compareTrademarkPlan(o1, o2);

  compareTrademarkClass = (o1: ITrademarkClass | null, o2: ITrademarkClass | null): boolean =>
    this.trademarkClassService.compareTrademarkClass(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ trademark }) => {
      this.trademark = trademark;
      if (trademark) {
        this.updateForm(trademark);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const trademark = this.trademarkFormService.getTrademark(this.editForm);
    if (trademark.id !== null) {
      this.subscribeToSaveResponse(this.trademarkService.update(trademark));
    } else {
      this.subscribeToSaveResponse(this.trademarkService.create(trademark));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITrademark>>): void {
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

  protected updateForm(trademark: ITrademark): void {
    this.trademark = trademark;
    this.trademarkFormService.resetForm(this.editForm, trademark);

    this.leadsSharedCollection = this.leadService.addLeadToCollectionIfMissing<ILead>(this.leadsSharedCollection, trademark.lead);
    this.userProfilesSharedCollection = this.userProfileService.addUserProfileToCollectionIfMissing<IUserProfile>(
      this.userProfilesSharedCollection,
      trademark.user,
    );
    this.trademarkPlansSharedCollection = this.trademarkPlanService.addTrademarkPlanToCollectionIfMissing<ITrademarkPlan>(
      this.trademarkPlansSharedCollection,
      trademark.trademarkPlan,
    );
    this.trademarkClassesSharedCollection = this.trademarkClassService.addTrademarkClassToCollectionIfMissing<ITrademarkClass>(
      this.trademarkClassesSharedCollection,
      ...(trademark.trademarkClasses ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.leadService
      .query()
      .pipe(map((res: HttpResponse<ILead[]>) => res.body ?? []))
      .pipe(map((leads: ILead[]) => this.leadService.addLeadToCollectionIfMissing<ILead>(leads, this.trademark?.lead)))
      .subscribe((leads: ILead[]) => (this.leadsSharedCollection = leads));

    this.userProfileService
      .query()
      .pipe(map((res: HttpResponse<IUserProfile[]>) => res.body ?? []))
      .pipe(
        map((userProfiles: IUserProfile[]) =>
          this.userProfileService.addUserProfileToCollectionIfMissing<IUserProfile>(userProfiles, this.trademark?.user),
        ),
      )
      .subscribe((userProfiles: IUserProfile[]) => (this.userProfilesSharedCollection = userProfiles));

    this.trademarkPlanService
      .query()
      .pipe(map((res: HttpResponse<ITrademarkPlan[]>) => res.body ?? []))
      .pipe(
        map((trademarkPlans: ITrademarkPlan[]) =>
          this.trademarkPlanService.addTrademarkPlanToCollectionIfMissing<ITrademarkPlan>(trademarkPlans, this.trademark?.trademarkPlan),
        ),
      )
      .subscribe((trademarkPlans: ITrademarkPlan[]) => (this.trademarkPlansSharedCollection = trademarkPlans));

    this.trademarkClassService
      .query()
      .pipe(map((res: HttpResponse<ITrademarkClass[]>) => res.body ?? []))
      .pipe(
        map((trademarkClasses: ITrademarkClass[]) =>
          this.trademarkClassService.addTrademarkClassToCollectionIfMissing<ITrademarkClass>(
            trademarkClasses,
            ...(this.trademark?.trademarkClasses ?? []),
          ),
        ),
      )
      .subscribe((trademarkClasses: ITrademarkClass[]) => (this.trademarkClassesSharedCollection = trademarkClasses));
  }
}
