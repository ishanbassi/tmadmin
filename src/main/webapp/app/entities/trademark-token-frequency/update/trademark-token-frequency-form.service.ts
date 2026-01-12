import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITrademarkTokenFrequency, NewTrademarkTokenFrequency } from '../trademark-token-frequency.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITrademarkTokenFrequency for edit and NewTrademarkTokenFrequencyFormGroupInput for create.
 */
type TrademarkTokenFrequencyFormGroupInput = ITrademarkTokenFrequency | PartialWithRequiredKeyOf<NewTrademarkTokenFrequency>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ITrademarkTokenFrequency | NewTrademarkTokenFrequency> = Omit<T, 'createdDate' | 'modifiedDate'> & {
  createdDate?: string | null;
  modifiedDate?: string | null;
};

type TrademarkTokenFrequencyFormRawValue = FormValueOf<ITrademarkTokenFrequency>;

type NewTrademarkTokenFrequencyFormRawValue = FormValueOf<NewTrademarkTokenFrequency>;

type TrademarkTokenFrequencyFormDefaults = Pick<NewTrademarkTokenFrequency, 'id' | 'createdDate' | 'deleted' | 'modifiedDate'>;

type TrademarkTokenFrequencyFormGroupContent = {
  id: FormControl<TrademarkTokenFrequencyFormRawValue['id'] | NewTrademarkTokenFrequency['id']>;
  frequency: FormControl<TrademarkTokenFrequencyFormRawValue['frequency']>;
  word: FormControl<TrademarkTokenFrequencyFormRawValue['word']>;
  createdDate: FormControl<TrademarkTokenFrequencyFormRawValue['createdDate']>;
  deleted: FormControl<TrademarkTokenFrequencyFormRawValue['deleted']>;
  modifiedDate: FormControl<TrademarkTokenFrequencyFormRawValue['modifiedDate']>;
};

export type TrademarkTokenFrequencyFormGroup = FormGroup<TrademarkTokenFrequencyFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TrademarkTokenFrequencyFormService {
  createTrademarkTokenFrequencyFormGroup(
    trademarkTokenFrequency: TrademarkTokenFrequencyFormGroupInput = { id: null },
  ): TrademarkTokenFrequencyFormGroup {
    const trademarkTokenFrequencyRawValue = this.convertTrademarkTokenFrequencyToTrademarkTokenFrequencyRawValue({
      ...this.getFormDefaults(),
      ...trademarkTokenFrequency,
    });
    return new FormGroup<TrademarkTokenFrequencyFormGroupContent>({
      id: new FormControl(
        { value: trademarkTokenFrequencyRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      frequency: new FormControl(trademarkTokenFrequencyRawValue.frequency),
      word: new FormControl(trademarkTokenFrequencyRawValue.word),
      createdDate: new FormControl(trademarkTokenFrequencyRawValue.createdDate),
      deleted: new FormControl(trademarkTokenFrequencyRawValue.deleted),
      modifiedDate: new FormControl(trademarkTokenFrequencyRawValue.modifiedDate),
    });
  }

  getTrademarkTokenFrequency(form: TrademarkTokenFrequencyFormGroup): ITrademarkTokenFrequency | NewTrademarkTokenFrequency {
    return this.convertTrademarkTokenFrequencyRawValueToTrademarkTokenFrequency(
      form.getRawValue() as TrademarkTokenFrequencyFormRawValue | NewTrademarkTokenFrequencyFormRawValue,
    );
  }

  resetForm(form: TrademarkTokenFrequencyFormGroup, trademarkTokenFrequency: TrademarkTokenFrequencyFormGroupInput): void {
    const trademarkTokenFrequencyRawValue = this.convertTrademarkTokenFrequencyToTrademarkTokenFrequencyRawValue({
      ...this.getFormDefaults(),
      ...trademarkTokenFrequency,
    });
    form.reset(
      {
        ...trademarkTokenFrequencyRawValue,
        id: { value: trademarkTokenFrequencyRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TrademarkTokenFrequencyFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      deleted: false,
      modifiedDate: currentTime,
    };
  }

  private convertTrademarkTokenFrequencyRawValueToTrademarkTokenFrequency(
    rawTrademarkTokenFrequency: TrademarkTokenFrequencyFormRawValue | NewTrademarkTokenFrequencyFormRawValue,
  ): ITrademarkTokenFrequency | NewTrademarkTokenFrequency {
    return {
      ...rawTrademarkTokenFrequency,
      createdDate: dayjs(rawTrademarkTokenFrequency.createdDate, DATE_TIME_FORMAT),
      modifiedDate: dayjs(rawTrademarkTokenFrequency.modifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertTrademarkTokenFrequencyToTrademarkTokenFrequencyRawValue(
    trademarkTokenFrequency: ITrademarkTokenFrequency | (Partial<NewTrademarkTokenFrequency> & TrademarkTokenFrequencyFormDefaults),
  ): TrademarkTokenFrequencyFormRawValue | PartialWithRequiredKeyOf<NewTrademarkTokenFrequencyFormRawValue> {
    return {
      ...trademarkTokenFrequency,
      createdDate: trademarkTokenFrequency.createdDate ? trademarkTokenFrequency.createdDate.format(DATE_TIME_FORMAT) : undefined,
      modifiedDate: trademarkTokenFrequency.modifiedDate ? trademarkTokenFrequency.modifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
