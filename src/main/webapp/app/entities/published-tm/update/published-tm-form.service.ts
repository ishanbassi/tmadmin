import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPublishedTm, NewPublishedTm } from '../published-tm.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPublishedTm for edit and NewPublishedTmFormGroupInput for create.
 */
type PublishedTmFormGroupInput = IPublishedTm | PartialWithRequiredKeyOf<NewPublishedTm>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IPublishedTm | NewPublishedTm> = Omit<T, 'createdDate' | 'modifiedDate'> & {
  createdDate?: string | null;
  modifiedDate?: string | null;
};

type PublishedTmFormRawValue = FormValueOf<IPublishedTm>;

type NewPublishedTmFormRawValue = FormValueOf<NewPublishedTm>;

type PublishedTmFormDefaults = Pick<NewPublishedTm, 'id' | 'deleted' | 'createdDate' | 'modifiedDate'>;

type PublishedTmFormGroupContent = {
  id: FormControl<PublishedTmFormRawValue['id'] | NewPublishedTm['id']>;
  name: FormControl<PublishedTmFormRawValue['name']>;
  details: FormControl<PublishedTmFormRawValue['details']>;
  applicationNo: FormControl<PublishedTmFormRawValue['applicationNo']>;
  applicationDate: FormControl<PublishedTmFormRawValue['applicationDate']>;
  agentName: FormControl<PublishedTmFormRawValue['agentName']>;
  agentAddress: FormControl<PublishedTmFormRawValue['agentAddress']>;
  proprietorName: FormControl<PublishedTmFormRawValue['proprietorName']>;
  proprietorAddress: FormControl<PublishedTmFormRawValue['proprietorAddress']>;
  headOffice: FormControl<PublishedTmFormRawValue['headOffice']>;
  imgUrl: FormControl<PublishedTmFormRawValue['imgUrl']>;
  tmClass: FormControl<PublishedTmFormRawValue['tmClass']>;
  journalNo: FormControl<PublishedTmFormRawValue['journalNo']>;
  deleted: FormControl<PublishedTmFormRawValue['deleted']>;
  usage: FormControl<PublishedTmFormRawValue['usage']>;
  associatedTms: FormControl<PublishedTmFormRawValue['associatedTms']>;
  trademarkStatus: FormControl<PublishedTmFormRawValue['trademarkStatus']>;
  createdDate: FormControl<PublishedTmFormRawValue['createdDate']>;
  modifiedDate: FormControl<PublishedTmFormRawValue['modifiedDate']>;
  renewalDate: FormControl<PublishedTmFormRawValue['renewalDate']>;
  type: FormControl<PublishedTmFormRawValue['type']>;
  pageNo: FormControl<PublishedTmFormRawValue['pageNo']>;
  tmAgent: FormControl<PublishedTmFormRawValue['tmAgent']>;
};

export type PublishedTmFormGroup = FormGroup<PublishedTmFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PublishedTmFormService {
  createPublishedTmFormGroup(publishedTm: PublishedTmFormGroupInput = { id: null }): PublishedTmFormGroup {
    const publishedTmRawValue = this.convertPublishedTmToPublishedTmRawValue({
      ...this.getFormDefaults(),
      ...publishedTm,
    });
    return new FormGroup<PublishedTmFormGroupContent>({
      id: new FormControl(
        { value: publishedTmRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(publishedTmRawValue.name),
      details: new FormControl(publishedTmRawValue.details),
      applicationNo: new FormControl(publishedTmRawValue.applicationNo),
      applicationDate: new FormControl(publishedTmRawValue.applicationDate),
      agentName: new FormControl(publishedTmRawValue.agentName),
      agentAddress: new FormControl(publishedTmRawValue.agentAddress),
      proprietorName: new FormControl(publishedTmRawValue.proprietorName),
      proprietorAddress: new FormControl(publishedTmRawValue.proprietorAddress),
      headOffice: new FormControl(publishedTmRawValue.headOffice),
      imgUrl: new FormControl(publishedTmRawValue.imgUrl),
      tmClass: new FormControl(publishedTmRawValue.tmClass),
      journalNo: new FormControl(publishedTmRawValue.journalNo),
      deleted: new FormControl(publishedTmRawValue.deleted),
      usage: new FormControl(publishedTmRawValue.usage),
      associatedTms: new FormControl(publishedTmRawValue.associatedTms),
      trademarkStatus: new FormControl(publishedTmRawValue.trademarkStatus),
      createdDate: new FormControl(publishedTmRawValue.createdDate),
      modifiedDate: new FormControl(publishedTmRawValue.modifiedDate),
      renewalDate: new FormControl(publishedTmRawValue.renewalDate),
      type: new FormControl(publishedTmRawValue.type),
      pageNo: new FormControl(publishedTmRawValue.pageNo),
      tmAgent: new FormControl(publishedTmRawValue.tmAgent),
    });
  }

  getPublishedTm(form: PublishedTmFormGroup): IPublishedTm | NewPublishedTm {
    return this.convertPublishedTmRawValueToPublishedTm(form.getRawValue() as PublishedTmFormRawValue | NewPublishedTmFormRawValue);
  }

  resetForm(form: PublishedTmFormGroup, publishedTm: PublishedTmFormGroupInput): void {
    const publishedTmRawValue = this.convertPublishedTmToPublishedTmRawValue({ ...this.getFormDefaults(), ...publishedTm });
    form.reset(
      {
        ...publishedTmRawValue,
        id: { value: publishedTmRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PublishedTmFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      deleted: false,
      createdDate: currentTime,
      modifiedDate: currentTime,
    };
  }

  private convertPublishedTmRawValueToPublishedTm(
    rawPublishedTm: PublishedTmFormRawValue | NewPublishedTmFormRawValue,
  ): IPublishedTm | NewPublishedTm {
    return {
      ...rawPublishedTm,
      createdDate: dayjs(rawPublishedTm.createdDate, DATE_TIME_FORMAT),
      modifiedDate: dayjs(rawPublishedTm.modifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertPublishedTmToPublishedTmRawValue(
    publishedTm: IPublishedTm | (Partial<NewPublishedTm> & PublishedTmFormDefaults),
  ): PublishedTmFormRawValue | PartialWithRequiredKeyOf<NewPublishedTmFormRawValue> {
    return {
      ...publishedTm,
      createdDate: publishedTm.createdDate ? publishedTm.createdDate.format(DATE_TIME_FORMAT) : undefined,
      modifiedDate: publishedTm.modifiedDate ? publishedTm.modifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
