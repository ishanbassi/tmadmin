import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITrademarkClass, NewTrademarkClass } from '../trademark-class.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITrademarkClass for edit and NewTrademarkClassFormGroupInput for create.
 */
type TrademarkClassFormGroupInput = ITrademarkClass | PartialWithRequiredKeyOf<NewTrademarkClass>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ITrademarkClass | NewTrademarkClass> = Omit<T, 'createdDate' | 'modifiedDate'> & {
  createdDate?: string | null;
  modifiedDate?: string | null;
};

type TrademarkClassFormRawValue = FormValueOf<ITrademarkClass>;

type NewTrademarkClassFormRawValue = FormValueOf<NewTrademarkClass>;

type TrademarkClassFormDefaults = Pick<NewTrademarkClass, 'id' | 'createdDate' | 'modifiedDate' | 'deleted' | 'trademarks'>;

type TrademarkClassFormGroupContent = {
  id: FormControl<TrademarkClassFormRawValue['id'] | NewTrademarkClass['id']>;
  code: FormControl<TrademarkClassFormRawValue['code']>;
  tmClass: FormControl<TrademarkClassFormRawValue['tmClass']>;
  keyword: FormControl<TrademarkClassFormRawValue['keyword']>;
  title: FormControl<TrademarkClassFormRawValue['title']>;
  description: FormControl<TrademarkClassFormRawValue['description']>;
  createdDate: FormControl<TrademarkClassFormRawValue['createdDate']>;
  modifiedDate: FormControl<TrademarkClassFormRawValue['modifiedDate']>;
  deleted: FormControl<TrademarkClassFormRawValue['deleted']>;
  trademarks: FormControl<TrademarkClassFormRawValue['trademarks']>;
};

export type TrademarkClassFormGroup = FormGroup<TrademarkClassFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TrademarkClassFormService {
  createTrademarkClassFormGroup(trademarkClass: TrademarkClassFormGroupInput = { id: null }): TrademarkClassFormGroup {
    const trademarkClassRawValue = this.convertTrademarkClassToTrademarkClassRawValue({
      ...this.getFormDefaults(),
      ...trademarkClass,
    });
    return new FormGroup<TrademarkClassFormGroupContent>({
      id: new FormControl(
        { value: trademarkClassRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      code: new FormControl(trademarkClassRawValue.code),
      tmClass: new FormControl(trademarkClassRawValue.tmClass),
      keyword: new FormControl(trademarkClassRawValue.keyword),
      title: new FormControl(trademarkClassRawValue.title),
      description: new FormControl(trademarkClassRawValue.description),
      createdDate: new FormControl(trademarkClassRawValue.createdDate),
      modifiedDate: new FormControl(trademarkClassRawValue.modifiedDate),
      deleted: new FormControl(trademarkClassRawValue.deleted),
      trademarks: new FormControl(trademarkClassRawValue.trademarks ?? []),
    });
  }

  getTrademarkClass(form: TrademarkClassFormGroup): ITrademarkClass | NewTrademarkClass {
    return this.convertTrademarkClassRawValueToTrademarkClass(
      form.getRawValue() as TrademarkClassFormRawValue | NewTrademarkClassFormRawValue,
    );
  }

  resetForm(form: TrademarkClassFormGroup, trademarkClass: TrademarkClassFormGroupInput): void {
    const trademarkClassRawValue = this.convertTrademarkClassToTrademarkClassRawValue({ ...this.getFormDefaults(), ...trademarkClass });
    form.reset(
      {
        ...trademarkClassRawValue,
        id: { value: trademarkClassRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TrademarkClassFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      modifiedDate: currentTime,
      deleted: false,
      trademarks: [],
    };
  }

  private convertTrademarkClassRawValueToTrademarkClass(
    rawTrademarkClass: TrademarkClassFormRawValue | NewTrademarkClassFormRawValue,
  ): ITrademarkClass | NewTrademarkClass {
    return {
      ...rawTrademarkClass,
      createdDate: dayjs(rawTrademarkClass.createdDate, DATE_TIME_FORMAT),
      modifiedDate: dayjs(rawTrademarkClass.modifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertTrademarkClassToTrademarkClassRawValue(
    trademarkClass: ITrademarkClass | (Partial<NewTrademarkClass> & TrademarkClassFormDefaults),
  ): TrademarkClassFormRawValue | PartialWithRequiredKeyOf<NewTrademarkClassFormRawValue> {
    return {
      ...trademarkClass,
      createdDate: trademarkClass.createdDate ? trademarkClass.createdDate.format(DATE_TIME_FORMAT) : undefined,
      modifiedDate: trademarkClass.modifiedDate ? trademarkClass.modifiedDate.format(DATE_TIME_FORMAT) : undefined,
      trademarks: trademarkClass.trademarks ?? [],
    };
  }
}
