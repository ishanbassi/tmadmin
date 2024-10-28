import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITrademark, NewTrademark } from '../trademark.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITrademark for edit and NewTrademarkFormGroupInput for create.
 */
type TrademarkFormGroupInput = ITrademark | PartialWithRequiredKeyOf<NewTrademark>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ITrademark | NewTrademark> = Omit<T, 'createdDate' | 'modifiedDate'> & {
  createdDate?: string | null;
  modifiedDate?: string | null;
};

type TrademarkFormRawValue = FormValueOf<ITrademark>;

type NewTrademarkFormRawValue = FormValueOf<NewTrademark>;

type TrademarkFormDefaults = Pick<NewTrademark, 'id' | 'deleted' | 'createdDate' | 'modifiedDate'>;

type TrademarkFormGroupContent = {
  id: FormControl<TrademarkFormRawValue['id'] | NewTrademark['id']>;
  name: FormControl<TrademarkFormRawValue['name']>;
  details: FormControl<TrademarkFormRawValue['details']>;
  applicationNo: FormControl<TrademarkFormRawValue['applicationNo']>;
  applicationDate: FormControl<TrademarkFormRawValue['applicationDate']>;
  agentName: FormControl<TrademarkFormRawValue['agentName']>;
  agentAddress: FormControl<TrademarkFormRawValue['agentAddress']>;
  proprietorName: FormControl<TrademarkFormRawValue['proprietorName']>;
  proprietorAddress: FormControl<TrademarkFormRawValue['proprietorAddress']>;
  headOffice: FormControl<TrademarkFormRawValue['headOffice']>;
  imgUrl: FormControl<TrademarkFormRawValue['imgUrl']>;
  tmClass: FormControl<TrademarkFormRawValue['tmClass']>;
  journalNo: FormControl<TrademarkFormRawValue['journalNo']>;
  deleted: FormControl<TrademarkFormRawValue['deleted']>;
  usage: FormControl<TrademarkFormRawValue['usage']>;
  associatedTms: FormControl<TrademarkFormRawValue['associatedTms']>;
  trademarkStatus: FormControl<TrademarkFormRawValue['trademarkStatus']>;
  createdDate: FormControl<TrademarkFormRawValue['createdDate']>;
  modifiedDate: FormControl<TrademarkFormRawValue['modifiedDate']>;
  userProfile: FormControl<TrademarkFormRawValue['userProfile']>;
};

export type TrademarkFormGroup = FormGroup<TrademarkFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TrademarkFormService {
  createTrademarkFormGroup(trademark: TrademarkFormGroupInput = { id: null }): TrademarkFormGroup {
    const trademarkRawValue = this.convertTrademarkToTrademarkRawValue({
      ...this.getFormDefaults(),
      ...trademark,
    });
    return new FormGroup<TrademarkFormGroupContent>({
      id: new FormControl(
        { value: trademarkRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(trademarkRawValue.name),
      details: new FormControl(trademarkRawValue.details),
      applicationNo: new FormControl(trademarkRawValue.applicationNo),
      applicationDate: new FormControl(trademarkRawValue.applicationDate),
      agentName: new FormControl(trademarkRawValue.agentName),
      agentAddress: new FormControl(trademarkRawValue.agentAddress),
      proprietorName: new FormControl(trademarkRawValue.proprietorName),
      proprietorAddress: new FormControl(trademarkRawValue.proprietorAddress),
      headOffice: new FormControl(trademarkRawValue.headOffice),
      imgUrl: new FormControl(trademarkRawValue.imgUrl),
      tmClass: new FormControl(trademarkRawValue.tmClass),
      journalNo: new FormControl(trademarkRawValue.journalNo),
      deleted: new FormControl(trademarkRawValue.deleted),
      usage: new FormControl(trademarkRawValue.usage),
      associatedTms: new FormControl(trademarkRawValue.associatedTms),
      trademarkStatus: new FormControl(trademarkRawValue.trademarkStatus),
      createdDate: new FormControl(trademarkRawValue.createdDate),
      modifiedDate: new FormControl(trademarkRawValue.modifiedDate),
      userProfile: new FormControl(trademarkRawValue.userProfile),
    });
  }

  getTrademark(form: TrademarkFormGroup): ITrademark | NewTrademark {
    return this.convertTrademarkRawValueToTrademark(form.getRawValue() as TrademarkFormRawValue | NewTrademarkFormRawValue);
  }

  resetForm(form: TrademarkFormGroup, trademark: TrademarkFormGroupInput): void {
    const trademarkRawValue = this.convertTrademarkToTrademarkRawValue({ ...this.getFormDefaults(), ...trademark });
    form.reset(
      {
        ...trademarkRawValue,
        id: { value: trademarkRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TrademarkFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      deleted: false,
      createdDate: currentTime,
      modifiedDate: currentTime,
    };
  }

  private convertTrademarkRawValueToTrademark(rawTrademark: TrademarkFormRawValue | NewTrademarkFormRawValue): ITrademark | NewTrademark {
    return {
      ...rawTrademark,
      createdDate: dayjs(rawTrademark.createdDate, DATE_TIME_FORMAT),
      modifiedDate: dayjs(rawTrademark.modifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertTrademarkToTrademarkRawValue(
    trademark: ITrademark | (Partial<NewTrademark> & TrademarkFormDefaults),
  ): TrademarkFormRawValue | PartialWithRequiredKeyOf<NewTrademarkFormRawValue> {
    return {
      ...trademark,
      createdDate: trademark.createdDate ? trademark.createdDate.format(DATE_TIME_FORMAT) : undefined,
      modifiedDate: trademark.modifiedDate ? trademark.modifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
