import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

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

type PublishedTmFormDefaults = Pick<NewPublishedTm, 'id' | 'deleted'>;

type PublishedTmFormGroupContent = {
  id: FormControl<IPublishedTm['id'] | NewPublishedTm['id']>;
  name: FormControl<IPublishedTm['name']>;
  details: FormControl<IPublishedTm['details']>;
  applicationNo: FormControl<IPublishedTm['applicationNo']>;
  applicationDate: FormControl<IPublishedTm['applicationDate']>;
  agentName: FormControl<IPublishedTm['agentName']>;
  agentAddress: FormControl<IPublishedTm['agentAddress']>;
  proprietorName: FormControl<IPublishedTm['proprietorName']>;
  proprietorAddress: FormControl<IPublishedTm['proprietorAddress']>;
  headOffice: FormControl<IPublishedTm['headOffice']>;
  imgUrl: FormControl<IPublishedTm['imgUrl']>;
  tmClass: FormControl<IPublishedTm['tmClass']>;
  journalNo: FormControl<IPublishedTm['journalNo']>;
  deleted: FormControl<IPublishedTm['deleted']>;
  usage: FormControl<IPublishedTm['usage']>;
  associatedTms: FormControl<IPublishedTm['associatedTms']>;
  trademarkStatus: FormControl<IPublishedTm['trademarkStatus']>;
};

export type PublishedTmFormGroup = FormGroup<PublishedTmFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PublishedTmFormService {
  createPublishedTmFormGroup(publishedTm: PublishedTmFormGroupInput = { id: null }): PublishedTmFormGroup {
    const publishedTmRawValue = {
      ...this.getFormDefaults(),
      ...publishedTm,
    };
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
    });
  }

  getPublishedTm(form: PublishedTmFormGroup): IPublishedTm | NewPublishedTm {
    return form.getRawValue() as IPublishedTm | NewPublishedTm;
  }

  resetForm(form: PublishedTmFormGroup, publishedTm: PublishedTmFormGroupInput): void {
    const publishedTmRawValue = { ...this.getFormDefaults(), ...publishedTm };
    form.reset(
      {
        ...publishedTmRawValue,
        id: { value: publishedTmRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PublishedTmFormDefaults {
    return {
      id: null,
      deleted: false,
    };
  }
}
