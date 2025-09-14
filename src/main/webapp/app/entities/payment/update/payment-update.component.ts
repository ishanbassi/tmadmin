import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITrademark } from 'app/entities/trademark/trademark.model';
import { TrademarkService } from 'app/entities/trademark/service/trademark.service';
import { IPayment } from '../payment.model';
import { PaymentService } from '../service/payment.service';
import { PaymentFormGroup, PaymentFormService } from './payment-form.service';

@Component({
  selector: 'jhi-payment-update',
  templateUrl: './payment-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PaymentUpdateComponent implements OnInit {
  isSaving = false;
  payment: IPayment | null = null;

  trademarksSharedCollection: ITrademark[] = [];

  protected paymentService = inject(PaymentService);
  protected paymentFormService = inject(PaymentFormService);
  protected trademarkService = inject(TrademarkService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PaymentFormGroup = this.paymentFormService.createPaymentFormGroup();

  compareTrademark = (o1: ITrademark | null, o2: ITrademark | null): boolean => this.trademarkService.compareTrademark(o1, o2);

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

    this.trademarksSharedCollection = this.trademarkService.addTrademarkToCollectionIfMissing<ITrademark>(
      this.trademarksSharedCollection,
      payment.trademark,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.trademarkService
      .query()
      .pipe(map((res: HttpResponse<ITrademark[]>) => res.body ?? []))
      .pipe(
        map((trademarks: ITrademark[]) =>
          this.trademarkService.addTrademarkToCollectionIfMissing<ITrademark>(trademarks, this.payment?.trademark),
        ),
      )
      .subscribe((trademarks: ITrademark[]) => (this.trademarksSharedCollection = trademarks));
  }
}
