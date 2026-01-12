import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IUserEventsTracking, NewUserEventsTracking } from '../user-events-tracking.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUserEventsTracking for edit and NewUserEventsTrackingFormGroupInput for create.
 */
type UserEventsTrackingFormGroupInput = IUserEventsTracking | PartialWithRequiredKeyOf<NewUserEventsTracking>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IUserEventsTracking | NewUserEventsTracking> = Omit<T, 'createdDate' | 'modifiedDate'> & {
  createdDate?: string | null;
  modifiedDate?: string | null;
};

type UserEventsTrackingFormRawValue = FormValueOf<IUserEventsTracking>;

type NewUserEventsTrackingFormRawValue = FormValueOf<NewUserEventsTracking>;

type UserEventsTrackingFormDefaults = Pick<NewUserEventsTracking, 'id' | 'createdDate' | 'deleted' | 'modifiedDate'>;

type UserEventsTrackingFormGroupContent = {
  id: FormControl<UserEventsTrackingFormRawValue['id'] | NewUserEventsTracking['id']>;
  eventType: FormControl<UserEventsTrackingFormRawValue['eventType']>;
  pageName: FormControl<UserEventsTrackingFormRawValue['pageName']>;
  deviceType: FormControl<UserEventsTrackingFormRawValue['deviceType']>;
  createdDate: FormControl<UserEventsTrackingFormRawValue['createdDate']>;
  deleted: FormControl<UserEventsTrackingFormRawValue['deleted']>;
  modifiedDate: FormControl<UserEventsTrackingFormRawValue['modifiedDate']>;
  userProfile: FormControl<UserEventsTrackingFormRawValue['userProfile']>;
};

export type UserEventsTrackingFormGroup = FormGroup<UserEventsTrackingFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UserEventsTrackingFormService {
  createUserEventsTrackingFormGroup(userEventsTracking: UserEventsTrackingFormGroupInput = { id: null }): UserEventsTrackingFormGroup {
    const userEventsTrackingRawValue = this.convertUserEventsTrackingToUserEventsTrackingRawValue({
      ...this.getFormDefaults(),
      ...userEventsTracking,
    });
    return new FormGroup<UserEventsTrackingFormGroupContent>({
      id: new FormControl(
        { value: userEventsTrackingRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      eventType: new FormControl(userEventsTrackingRawValue.eventType),
      pageName: new FormControl(userEventsTrackingRawValue.pageName),
      deviceType: new FormControl(userEventsTrackingRawValue.deviceType),
      createdDate: new FormControl(userEventsTrackingRawValue.createdDate),
      deleted: new FormControl(userEventsTrackingRawValue.deleted),
      modifiedDate: new FormControl(userEventsTrackingRawValue.modifiedDate),
      userProfile: new FormControl(userEventsTrackingRawValue.userProfile),
    });
  }

  getUserEventsTracking(form: UserEventsTrackingFormGroup): IUserEventsTracking | NewUserEventsTracking {
    return this.convertUserEventsTrackingRawValueToUserEventsTracking(
      form.getRawValue() as UserEventsTrackingFormRawValue | NewUserEventsTrackingFormRawValue,
    );
  }

  resetForm(form: UserEventsTrackingFormGroup, userEventsTracking: UserEventsTrackingFormGroupInput): void {
    const userEventsTrackingRawValue = this.convertUserEventsTrackingToUserEventsTrackingRawValue({
      ...this.getFormDefaults(),
      ...userEventsTracking,
    });
    form.reset(
      {
        ...userEventsTrackingRawValue,
        id: { value: userEventsTrackingRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): UserEventsTrackingFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      deleted: false,
      modifiedDate: currentTime,
    };
  }

  private convertUserEventsTrackingRawValueToUserEventsTracking(
    rawUserEventsTracking: UserEventsTrackingFormRawValue | NewUserEventsTrackingFormRawValue,
  ): IUserEventsTracking | NewUserEventsTracking {
    return {
      ...rawUserEventsTracking,
      createdDate: dayjs(rawUserEventsTracking.createdDate, DATE_TIME_FORMAT),
      modifiedDate: dayjs(rawUserEventsTracking.modifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertUserEventsTrackingToUserEventsTrackingRawValue(
    userEventsTracking: IUserEventsTracking | (Partial<NewUserEventsTracking> & UserEventsTrackingFormDefaults),
  ): UserEventsTrackingFormRawValue | PartialWithRequiredKeyOf<NewUserEventsTrackingFormRawValue> {
    return {
      ...userEventsTracking,
      createdDate: userEventsTracking.createdDate ? userEventsTracking.createdDate.format(DATE_TIME_FORMAT) : undefined,
      modifiedDate: userEventsTracking.modifiedDate ? userEventsTracking.modifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
