import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITmAgent, NewTmAgent } from '../tm-agent.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITmAgent for edit and NewTmAgentFormGroupInput for create.
 */
type TmAgentFormGroupInput = ITmAgent | PartialWithRequiredKeyOf<NewTmAgent>;

type TmAgentFormDefaults = Pick<NewTmAgent, 'id'>;

type TmAgentFormGroupContent = {
  id: FormControl<ITmAgent['id'] | NewTmAgent['id']>;
  agentCode: FormControl<ITmAgent['agentCode']>;
  firstName: FormControl<ITmAgent['firstName']>;
  lastName: FormControl<ITmAgent['lastName']>;
  address: FormControl<ITmAgent['address']>;
};

export type TmAgentFormGroup = FormGroup<TmAgentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TmAgentFormService {
  createTmAgentFormGroup(tmAgent: TmAgentFormGroupInput = { id: null }): TmAgentFormGroup {
    const tmAgentRawValue = {
      ...this.getFormDefaults(),
      ...tmAgent,
    };
    return new FormGroup<TmAgentFormGroupContent>({
      id: new FormControl(
        { value: tmAgentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      agentCode: new FormControl(tmAgentRawValue.agentCode),
      firstName: new FormControl(tmAgentRawValue.firstName),
      lastName: new FormControl(tmAgentRawValue.lastName),
      address: new FormControl(tmAgentRawValue.address),
    });
  }

  getTmAgent(form: TmAgentFormGroup): ITmAgent | NewTmAgent {
    return form.getRawValue() as ITmAgent | NewTmAgent;
  }

  resetForm(form: TmAgentFormGroup, tmAgent: TmAgentFormGroupInput): void {
    const tmAgentRawValue = { ...this.getFormDefaults(), ...tmAgent };
    form.reset(
      {
        ...tmAgentRawValue,
        id: { value: tmAgentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TmAgentFormDefaults {
    return {
      id: null,
    };
  }
}
