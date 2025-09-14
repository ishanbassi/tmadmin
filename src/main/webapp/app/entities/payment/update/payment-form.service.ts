import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPayment, NewPayment } from '../payment.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPayment for edit and NewPaymentFormGroupInput for create.
 */
type PaymentFormGroupInput = IPayment | PartialWithRequiredKeyOf<NewPayment>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IPayment | NewPayment> = Omit<T, 'createdDate' | 'modifiedDate'> & {
  createdDate?: string | null;
  modifiedDate?: string | null;
};

type PaymentFormRawValue = FormValueOf<IPayment>;

type NewPaymentFormRawValue = FormValueOf<NewPayment>;

type PaymentFormDefaults = Pick<NewPayment, 'id' | 'createdDate' | 'deleted' | 'modifiedDate'>;

type PaymentFormGroupContent = {
  id: FormControl<PaymentFormRawValue['id'] | NewPayment['id']>;
  gateway: FormControl<PaymentFormRawValue['gateway']>;
  gatewayPaymentId: FormControl<PaymentFormRawValue['gatewayPaymentId']>;
  amount: FormControl<PaymentFormRawValue['amount']>;
  currency: FormControl<PaymentFormRawValue['currency']>;
  status: FormControl<PaymentFormRawValue['status']>;
  paymentMethod: FormControl<PaymentFormRawValue['paymentMethod']>;
  createdDate: FormControl<PaymentFormRawValue['createdDate']>;
  deleted: FormControl<PaymentFormRawValue['deleted']>;
  modifiedDate: FormControl<PaymentFormRawValue['modifiedDate']>;
  orderId: FormControl<PaymentFormRawValue['orderId']>;
  gatewayOrderId: FormControl<PaymentFormRawValue['gatewayOrderId']>;
  failureReason: FormControl<PaymentFormRawValue['failureReason']>;
  trademark: FormControl<PaymentFormRawValue['trademark']>;
};

export type PaymentFormGroup = FormGroup<PaymentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PaymentFormService {
  createPaymentFormGroup(payment: PaymentFormGroupInput = { id: null }): PaymentFormGroup {
    const paymentRawValue = this.convertPaymentToPaymentRawValue({
      ...this.getFormDefaults(),
      ...payment,
    });
    return new FormGroup<PaymentFormGroupContent>({
      id: new FormControl(
        { value: paymentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      gateway: new FormControl(paymentRawValue.gateway),
      gatewayPaymentId: new FormControl(paymentRawValue.gatewayPaymentId),
      amount: new FormControl(paymentRawValue.amount),
      currency: new FormControl(paymentRawValue.currency),
      status: new FormControl(paymentRawValue.status),
      paymentMethod: new FormControl(paymentRawValue.paymentMethod),
      createdDate: new FormControl(paymentRawValue.createdDate),
      deleted: new FormControl(paymentRawValue.deleted),
      modifiedDate: new FormControl(paymentRawValue.modifiedDate),
      orderId: new FormControl(paymentRawValue.orderId),
      gatewayOrderId: new FormControl(paymentRawValue.gatewayOrderId),
      failureReason: new FormControl(paymentRawValue.failureReason),
      trademark: new FormControl(paymentRawValue.trademark),
    });
  }

  getPayment(form: PaymentFormGroup): IPayment | NewPayment {
    return this.convertPaymentRawValueToPayment(form.getRawValue() as PaymentFormRawValue | NewPaymentFormRawValue);
  }

  resetForm(form: PaymentFormGroup, payment: PaymentFormGroupInput): void {
    const paymentRawValue = this.convertPaymentToPaymentRawValue({ ...this.getFormDefaults(), ...payment });
    form.reset(
      {
        ...paymentRawValue,
        id: { value: paymentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PaymentFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      deleted: false,
      modifiedDate: currentTime,
    };
  }

  private convertPaymentRawValueToPayment(rawPayment: PaymentFormRawValue | NewPaymentFormRawValue): IPayment | NewPayment {
    return {
      ...rawPayment,
      createdDate: dayjs(rawPayment.createdDate, DATE_TIME_FORMAT),
      modifiedDate: dayjs(rawPayment.modifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertPaymentToPaymentRawValue(
    payment: IPayment | (Partial<NewPayment> & PaymentFormDefaults),
  ): PaymentFormRawValue | PartialWithRequiredKeyOf<NewPaymentFormRawValue> {
    return {
      ...payment,
      createdDate: payment.createdDate ? payment.createdDate.format(DATE_TIME_FORMAT) : undefined,
      modifiedDate: payment.modifiedDate ? payment.modifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
