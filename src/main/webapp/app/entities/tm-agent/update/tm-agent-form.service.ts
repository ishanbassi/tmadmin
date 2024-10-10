import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
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

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ITmAgent | NewTmAgent> = Omit<T, 'createdDate' | 'modifiedDate'> & {
  createdDate?: string | null;
  modifiedDate?: string | null;
};

type TmAgentFormRawValue = FormValueOf<ITmAgent>;

type NewTmAgentFormRawValue = FormValueOf<NewTmAgent>;

type TmAgentFormDefaults = Pick<NewTmAgent, 'id' | 'createdDate' | 'modifiedDate' | 'deleted'>;

type TmAgentFormGroupContent = {
  id: FormControl<TmAgentFormRawValue['id'] | NewTmAgent['id']>;
  firstName: FormControl<TmAgentFormRawValue['firstName']>;
  lastName: FormControl<TmAgentFormRawValue['lastName']>;
  address: FormControl<TmAgentFormRawValue['address']>;
  createdDate: FormControl<TmAgentFormRawValue['createdDate']>;
  modifiedDate: FormControl<TmAgentFormRawValue['modifiedDate']>;
  deleted: FormControl<TmAgentFormRawValue['deleted']>;
  companyName: FormControl<TmAgentFormRawValue['companyName']>;
  agentCode: FormControl<TmAgentFormRawValue['agentCode']>;
  email: FormControl<TmAgentFormRawValue['email']>;
};

export type TmAgentFormGroup = FormGroup<TmAgentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TmAgentFormService {
  createTmAgentFormGroup(tmAgent: TmAgentFormGroupInput = { id: null }): TmAgentFormGroup {
    const tmAgentRawValue = this.convertTmAgentToTmAgentRawValue({
      ...this.getFormDefaults(),
      ...tmAgent,
    });
    return new FormGroup<TmAgentFormGroupContent>({
      id: new FormControl(
        { value: tmAgentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      firstName: new FormControl(tmAgentRawValue.firstName),
      lastName: new FormControl(tmAgentRawValue.lastName),
      address: new FormControl(tmAgentRawValue.address),
      createdDate: new FormControl(tmAgentRawValue.createdDate),
      modifiedDate: new FormControl(tmAgentRawValue.modifiedDate),
      deleted: new FormControl(tmAgentRawValue.deleted),
      companyName: new FormControl(tmAgentRawValue.companyName),
      agentCode: new FormControl(tmAgentRawValue.agentCode),
      email: new FormControl(tmAgentRawValue.email),
    });
  }

  getTmAgent(form: TmAgentFormGroup): ITmAgent | NewTmAgent {
    return this.convertTmAgentRawValueToTmAgent(form.getRawValue() as TmAgentFormRawValue | NewTmAgentFormRawValue);
  }

  resetForm(form: TmAgentFormGroup, tmAgent: TmAgentFormGroupInput): void {
    const tmAgentRawValue = this.convertTmAgentToTmAgentRawValue({ ...this.getFormDefaults(), ...tmAgent });
    form.reset(
      {
        ...tmAgentRawValue,
        id: { value: tmAgentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TmAgentFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      modifiedDate: currentTime,
      deleted: false,
    };
  }

  private convertTmAgentRawValueToTmAgent(rawTmAgent: TmAgentFormRawValue | NewTmAgentFormRawValue): ITmAgent | NewTmAgent {
    return {
      ...rawTmAgent,
      createdDate: dayjs(rawTmAgent.createdDate, DATE_TIME_FORMAT),
      modifiedDate: dayjs(rawTmAgent.modifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertTmAgentToTmAgentRawValue(
    tmAgent: ITmAgent | (Partial<NewTmAgent> & TmAgentFormDefaults),
  ): TmAgentFormRawValue | PartialWithRequiredKeyOf<NewTmAgentFormRawValue> {
    return {
      ...tmAgent,
      createdDate: tmAgent.createdDate ? tmAgent.createdDate.format(DATE_TIME_FORMAT) : undefined,
      modifiedDate: tmAgent.modifiedDate ? tmAgent.modifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
