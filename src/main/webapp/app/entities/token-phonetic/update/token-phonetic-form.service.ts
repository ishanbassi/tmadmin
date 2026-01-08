import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ITokenPhonetic, NewTokenPhonetic } from '../token-phonetic.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITokenPhonetic for edit and NewTokenPhoneticFormGroupInput for create.
 */
type TokenPhoneticFormGroupInput = ITokenPhonetic | PartialWithRequiredKeyOf<NewTokenPhonetic>;

type TokenPhoneticFormDefaults = Pick<NewTokenPhonetic, 'id'>;

type TokenPhoneticFormGroupContent = {
  id: FormControl<ITokenPhonetic['id'] | NewTokenPhonetic['id']>;
  algorithm: FormControl<ITokenPhonetic['algorithm']>;
  phoneticCode: FormControl<ITokenPhonetic['phoneticCode']>;
  secondaryPhoneticCode: FormControl<ITokenPhonetic['secondaryPhoneticCode']>;
  trademarkToken: FormControl<ITokenPhonetic['trademarkToken']>;
};

export type TokenPhoneticFormGroup = FormGroup<TokenPhoneticFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TokenPhoneticFormService {
  createTokenPhoneticFormGroup(tokenPhonetic: TokenPhoneticFormGroupInput = { id: null }): TokenPhoneticFormGroup {
    const tokenPhoneticRawValue = {
      ...this.getFormDefaults(),
      ...tokenPhonetic,
    };
    return new FormGroup<TokenPhoneticFormGroupContent>({
      id: new FormControl(
        { value: tokenPhoneticRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      algorithm: new FormControl(tokenPhoneticRawValue.algorithm),
      phoneticCode: new FormControl(tokenPhoneticRawValue.phoneticCode),
      secondaryPhoneticCode: new FormControl(tokenPhoneticRawValue.secondaryPhoneticCode),
      trademarkToken: new FormControl(tokenPhoneticRawValue.trademarkToken),
    });
  }

  getTokenPhonetic(form: TokenPhoneticFormGroup): ITokenPhonetic | NewTokenPhonetic {
    return form.getRawValue() as ITokenPhonetic | NewTokenPhonetic;
  }

  resetForm(form: TokenPhoneticFormGroup, tokenPhonetic: TokenPhoneticFormGroupInput): void {
    const tokenPhoneticRawValue = { ...this.getFormDefaults(), ...tokenPhonetic };
    form.reset(
      {
        ...tokenPhoneticRawValue,
        id: { value: tokenPhoneticRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TokenPhoneticFormDefaults {
    return {
      id: null,
    };
  }
}
