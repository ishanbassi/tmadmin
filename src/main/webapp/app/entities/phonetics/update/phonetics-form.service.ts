import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPhonetics, NewPhonetics } from '../phonetics.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPhonetics for edit and NewPhoneticsFormGroupInput for create.
 */
type PhoneticsFormGroupInput = IPhonetics | PartialWithRequiredKeyOf<NewPhonetics>;

type PhoneticsFormDefaults = Pick<NewPhonetics, 'id' | 'complete'>;

type PhoneticsFormGroupContent = {
  id: FormControl<IPhonetics['id'] | NewPhonetics['id']>;
  sanitizedTm: FormControl<IPhonetics['sanitizedTm']>;
  phoneticPk: FormControl<IPhonetics['phoneticPk']>;
  phoneticSk: FormControl<IPhonetics['phoneticSk']>;
  complete: FormControl<IPhonetics['complete']>;
  trademark: FormControl<IPhonetics['trademark']>;
};

export type PhoneticsFormGroup = FormGroup<PhoneticsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PhoneticsFormService {
  createPhoneticsFormGroup(phonetics: PhoneticsFormGroupInput = { id: null }): PhoneticsFormGroup {
    const phoneticsRawValue = {
      ...this.getFormDefaults(),
      ...phonetics,
    };
    return new FormGroup<PhoneticsFormGroupContent>({
      id: new FormControl(
        { value: phoneticsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      sanitizedTm: new FormControl(phoneticsRawValue.sanitizedTm),
      phoneticPk: new FormControl(phoneticsRawValue.phoneticPk),
      phoneticSk: new FormControl(phoneticsRawValue.phoneticSk),
      complete: new FormControl(phoneticsRawValue.complete),
      trademark: new FormControl(phoneticsRawValue.trademark),
    });
  }

  getPhonetics(form: PhoneticsFormGroup): IPhonetics | NewPhonetics {
    return form.getRawValue() as IPhonetics | NewPhonetics;
  }

  resetForm(form: PhoneticsFormGroup, phonetics: PhoneticsFormGroupInput): void {
    const phoneticsRawValue = { ...this.getFormDefaults(), ...phonetics };
    form.reset(
      {
        ...phoneticsRawValue,
        id: { value: phoneticsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PhoneticsFormDefaults {
    return {
      id: null,
      complete: false,
    };
  }
}
