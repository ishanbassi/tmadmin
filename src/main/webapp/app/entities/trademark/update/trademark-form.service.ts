import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

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

type TrademarkFormDefaults = Pick<NewTrademark, 'id' | 'deleted'>;

type TrademarkFormGroupContent = {
  id: FormControl<ITrademark['id'] | NewTrademark['id']>;
  name: FormControl<ITrademark['name']>;
  details: FormControl<ITrademark['details']>;
  applicationNo: FormControl<ITrademark['applicationNo']>;
  applicationDate: FormControl<ITrademark['applicationDate']>;
  agentName: FormControl<ITrademark['agentName']>;
  agentAddress: FormControl<ITrademark['agentAddress']>;
  proprietorName: FormControl<ITrademark['proprietorName']>;
  proprietorAddress: FormControl<ITrademark['proprietorAddress']>;
  headOffice: FormControl<ITrademark['headOffice']>;
  imgUrl: FormControl<ITrademark['imgUrl']>;
  tmClass: FormControl<ITrademark['tmClass']>;
  journalNo: FormControl<ITrademark['journalNo']>;
  deleted: FormControl<ITrademark['deleted']>;
  usage: FormControl<ITrademark['usage']>;
  associatedTms: FormControl<ITrademark['associatedTms']>;
  trademarkStatus: FormControl<ITrademark['trademarkStatus']>;
};

export type TrademarkFormGroup = FormGroup<TrademarkFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TrademarkFormService {
  createTrademarkFormGroup(trademark: TrademarkFormGroupInput = { id: null }): TrademarkFormGroup {
    const trademarkRawValue = {
      ...this.getFormDefaults(),
      ...trademark,
    };
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
    });
  }

  getTrademark(form: TrademarkFormGroup): ITrademark | NewTrademark {
    return form.getRawValue() as ITrademark | NewTrademark;
  }

  resetForm(form: TrademarkFormGroup, trademark: TrademarkFormGroupInput): void {
    const trademarkRawValue = { ...this.getFormDefaults(), ...trademark };
    form.reset(
      {
        ...trademarkRawValue,
        id: { value: trademarkRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TrademarkFormDefaults {
    return {
      id: null,
      deleted: false,
    };
  }
}
