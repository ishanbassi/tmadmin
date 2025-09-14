import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITrademarkPlan, NewTrademarkPlan } from '../trademark-plan.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITrademarkPlan for edit and NewTrademarkPlanFormGroupInput for create.
 */
type TrademarkPlanFormGroupInput = ITrademarkPlan | PartialWithRequiredKeyOf<NewTrademarkPlan>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ITrademarkPlan | NewTrademarkPlan> = Omit<T, 'createdDate' | 'modifiedDate'> & {
  createdDate?: string | null;
  modifiedDate?: string | null;
};

type TrademarkPlanFormRawValue = FormValueOf<ITrademarkPlan>;

type NewTrademarkPlanFormRawValue = FormValueOf<NewTrademarkPlan>;

type TrademarkPlanFormDefaults = Pick<NewTrademarkPlan, 'id' | 'createdDate' | 'deleted' | 'modifiedDate'>;

type TrademarkPlanFormGroupContent = {
  id: FormControl<TrademarkPlanFormRawValue['id'] | NewTrademarkPlan['id']>;
  name: FormControl<TrademarkPlanFormRawValue['name']>;
  fees: FormControl<TrademarkPlanFormRawValue['fees']>;
  notes: FormControl<TrademarkPlanFormRawValue['notes']>;
  createdDate: FormControl<TrademarkPlanFormRawValue['createdDate']>;
  deleted: FormControl<TrademarkPlanFormRawValue['deleted']>;
  modifiedDate: FormControl<TrademarkPlanFormRawValue['modifiedDate']>;
};

export type TrademarkPlanFormGroup = FormGroup<TrademarkPlanFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TrademarkPlanFormService {
  createTrademarkPlanFormGroup(trademarkPlan: TrademarkPlanFormGroupInput = { id: null }): TrademarkPlanFormGroup {
    const trademarkPlanRawValue = this.convertTrademarkPlanToTrademarkPlanRawValue({
      ...this.getFormDefaults(),
      ...trademarkPlan,
    });
    return new FormGroup<TrademarkPlanFormGroupContent>({
      id: new FormControl(
        { value: trademarkPlanRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(trademarkPlanRawValue.name),
      fees: new FormControl(trademarkPlanRawValue.fees),
      notes: new FormControl(trademarkPlanRawValue.notes),
      createdDate: new FormControl(trademarkPlanRawValue.createdDate),
      deleted: new FormControl(trademarkPlanRawValue.deleted),
      modifiedDate: new FormControl(trademarkPlanRawValue.modifiedDate),
    });
  }

  getTrademarkPlan(form: TrademarkPlanFormGroup): ITrademarkPlan | NewTrademarkPlan {
    return this.convertTrademarkPlanRawValueToTrademarkPlan(form.getRawValue() as TrademarkPlanFormRawValue | NewTrademarkPlanFormRawValue);
  }

  resetForm(form: TrademarkPlanFormGroup, trademarkPlan: TrademarkPlanFormGroupInput): void {
    const trademarkPlanRawValue = this.convertTrademarkPlanToTrademarkPlanRawValue({ ...this.getFormDefaults(), ...trademarkPlan });
    form.reset(
      {
        ...trademarkPlanRawValue,
        id: { value: trademarkPlanRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TrademarkPlanFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      deleted: false,
      modifiedDate: currentTime,
    };
  }

  private convertTrademarkPlanRawValueToTrademarkPlan(
    rawTrademarkPlan: TrademarkPlanFormRawValue | NewTrademarkPlanFormRawValue,
  ): ITrademarkPlan | NewTrademarkPlan {
    return {
      ...rawTrademarkPlan,
      createdDate: dayjs(rawTrademarkPlan.createdDate, DATE_TIME_FORMAT),
      modifiedDate: dayjs(rawTrademarkPlan.modifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertTrademarkPlanToTrademarkPlanRawValue(
    trademarkPlan: ITrademarkPlan | (Partial<NewTrademarkPlan> & TrademarkPlanFormDefaults),
  ): TrademarkPlanFormRawValue | PartialWithRequiredKeyOf<NewTrademarkPlanFormRawValue> {
    return {
      ...trademarkPlan,
      createdDate: trademarkPlan.createdDate ? trademarkPlan.createdDate.format(DATE_TIME_FORMAT) : undefined,
      modifiedDate: trademarkPlan.modifiedDate ? trademarkPlan.modifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
