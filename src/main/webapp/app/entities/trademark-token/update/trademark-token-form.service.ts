import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ITrademarkToken, NewTrademarkToken } from '../trademark-token.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITrademarkToken for edit and NewTrademarkTokenFormGroupInput for create.
 */
type TrademarkTokenFormGroupInput = ITrademarkToken | PartialWithRequiredKeyOf<NewTrademarkToken>;

type TrademarkTokenFormDefaults = Pick<NewTrademarkToken, 'id'>;

type TrademarkTokenFormGroupContent = {
  id: FormControl<ITrademarkToken['id'] | NewTrademarkToken['id']>;
  tokenText: FormControl<ITrademarkToken['tokenText']>;
  tokenType: FormControl<ITrademarkToken['tokenType']>;
  position: FormControl<ITrademarkToken['position']>;
  trademark: FormControl<ITrademarkToken['trademark']>;
};

export type TrademarkTokenFormGroup = FormGroup<TrademarkTokenFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TrademarkTokenFormService {
  createTrademarkTokenFormGroup(trademarkToken: TrademarkTokenFormGroupInput = { id: null }): TrademarkTokenFormGroup {
    const trademarkTokenRawValue = {
      ...this.getFormDefaults(),
      ...trademarkToken,
    };
    return new FormGroup<TrademarkTokenFormGroupContent>({
      id: new FormControl(
        { value: trademarkTokenRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      tokenText: new FormControl(trademarkTokenRawValue.tokenText),
      tokenType: new FormControl(trademarkTokenRawValue.tokenType),
      position: new FormControl(trademarkTokenRawValue.position),
      trademark: new FormControl(trademarkTokenRawValue.trademark),
    });
  }

  getTrademarkToken(form: TrademarkTokenFormGroup): ITrademarkToken | NewTrademarkToken {
    return form.getRawValue() as ITrademarkToken | NewTrademarkToken;
  }

  resetForm(form: TrademarkTokenFormGroup, trademarkToken: TrademarkTokenFormGroupInput): void {
    const trademarkTokenRawValue = { ...this.getFormDefaults(), ...trademarkToken };
    form.reset(
      {
        ...trademarkTokenRawValue,
        id: { value: trademarkTokenRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TrademarkTokenFormDefaults {
    return {
      id: null,
    };
  }
}
