import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPublishedTmPhonetics, NewPublishedTmPhonetics } from '../published-tm-phonetics.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPublishedTmPhonetics for edit and NewPublishedTmPhoneticsFormGroupInput for create.
 */
type PublishedTmPhoneticsFormGroupInput = IPublishedTmPhonetics | PartialWithRequiredKeyOf<NewPublishedTmPhonetics>;

type PublishedTmPhoneticsFormDefaults = Pick<NewPublishedTmPhonetics, 'id' | 'complete'>;

type PublishedTmPhoneticsFormGroupContent = {
  id: FormControl<IPublishedTmPhonetics['id'] | NewPublishedTmPhonetics['id']>;
  sanitizedTm: FormControl<IPublishedTmPhonetics['sanitizedTm']>;
  phoneticPk: FormControl<IPublishedTmPhonetics['phoneticPk']>;
  phoneticSk: FormControl<IPublishedTmPhonetics['phoneticSk']>;
  complete: FormControl<IPublishedTmPhonetics['complete']>;
  publishedTm: FormControl<IPublishedTmPhonetics['publishedTm']>;
};

export type PublishedTmPhoneticsFormGroup = FormGroup<PublishedTmPhoneticsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PublishedTmPhoneticsFormService {
  createPublishedTmPhoneticsFormGroup(
    publishedTmPhonetics: PublishedTmPhoneticsFormGroupInput = { id: null },
  ): PublishedTmPhoneticsFormGroup {
    const publishedTmPhoneticsRawValue = {
      ...this.getFormDefaults(),
      ...publishedTmPhonetics,
    };
    return new FormGroup<PublishedTmPhoneticsFormGroupContent>({
      id: new FormControl(
        { value: publishedTmPhoneticsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      sanitizedTm: new FormControl(publishedTmPhoneticsRawValue.sanitizedTm),
      phoneticPk: new FormControl(publishedTmPhoneticsRawValue.phoneticPk),
      phoneticSk: new FormControl(publishedTmPhoneticsRawValue.phoneticSk),
      complete: new FormControl(publishedTmPhoneticsRawValue.complete),
      publishedTm: new FormControl(publishedTmPhoneticsRawValue.publishedTm),
    });
  }

  getPublishedTmPhonetics(form: PublishedTmPhoneticsFormGroup): IPublishedTmPhonetics | NewPublishedTmPhonetics {
    return form.getRawValue() as IPublishedTmPhonetics | NewPublishedTmPhonetics;
  }

  resetForm(form: PublishedTmPhoneticsFormGroup, publishedTmPhonetics: PublishedTmPhoneticsFormGroupInput): void {
    const publishedTmPhoneticsRawValue = { ...this.getFormDefaults(), ...publishedTmPhonetics };
    form.reset(
      {
        ...publishedTmPhoneticsRawValue,
        id: { value: publishedTmPhoneticsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PublishedTmPhoneticsFormDefaults {
    return {
      id: null,
      complete: false,
    };
  }
}
