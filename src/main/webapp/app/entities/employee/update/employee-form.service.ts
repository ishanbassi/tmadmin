import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IEmployee, NewEmployee } from '../employee.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEmployee for edit and NewEmployeeFormGroupInput for create.
 */
type EmployeeFormGroupInput = IEmployee | PartialWithRequiredKeyOf<NewEmployee>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IEmployee | NewEmployee> = Omit<T, 'createdDate' | 'modifiedDate'> & {
  createdDate?: string | null;
  modifiedDate?: string | null;
};

type EmployeeFormRawValue = FormValueOf<IEmployee>;

type NewEmployeeFormRawValue = FormValueOf<NewEmployee>;

type EmployeeFormDefaults = Pick<NewEmployee, 'id' | 'createdDate' | 'modifiedDate' | 'deleted'>;

type EmployeeFormGroupContent = {
  id: FormControl<EmployeeFormRawValue['id'] | NewEmployee['id']>;
  fullName: FormControl<EmployeeFormRawValue['fullName']>;
  phoneNumber: FormControl<EmployeeFormRawValue['phoneNumber']>;
  email: FormControl<EmployeeFormRawValue['email']>;
  createdDate: FormControl<EmployeeFormRawValue['createdDate']>;
  modifiedDate: FormControl<EmployeeFormRawValue['modifiedDate']>;
  deleted: FormControl<EmployeeFormRawValue['deleted']>;
  designation: FormControl<EmployeeFormRawValue['designation']>;
  joiningDate: FormControl<EmployeeFormRawValue['joiningDate']>;
};

export type EmployeeFormGroup = FormGroup<EmployeeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EmployeeFormService {
  createEmployeeFormGroup(employee: EmployeeFormGroupInput = { id: null }): EmployeeFormGroup {
    const employeeRawValue = this.convertEmployeeToEmployeeRawValue({
      ...this.getFormDefaults(),
      ...employee,
    });
    return new FormGroup<EmployeeFormGroupContent>({
      id: new FormControl(
        { value: employeeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      fullName: new FormControl(employeeRawValue.fullName),
      phoneNumber: new FormControl(employeeRawValue.phoneNumber),
      email: new FormControl(employeeRawValue.email),
      createdDate: new FormControl(employeeRawValue.createdDate),
      modifiedDate: new FormControl(employeeRawValue.modifiedDate),
      deleted: new FormControl(employeeRawValue.deleted),
      designation: new FormControl(employeeRawValue.designation),
      joiningDate: new FormControl(employeeRawValue.joiningDate),
    });
  }

  getEmployee(form: EmployeeFormGroup): IEmployee | NewEmployee {
    return this.convertEmployeeRawValueToEmployee(form.getRawValue() as EmployeeFormRawValue | NewEmployeeFormRawValue);
  }

  resetForm(form: EmployeeFormGroup, employee: EmployeeFormGroupInput): void {
    const employeeRawValue = this.convertEmployeeToEmployeeRawValue({ ...this.getFormDefaults(), ...employee });
    form.reset(
      {
        ...employeeRawValue,
        id: { value: employeeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): EmployeeFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      modifiedDate: currentTime,
      deleted: false,
    };
  }

  private convertEmployeeRawValueToEmployee(rawEmployee: EmployeeFormRawValue | NewEmployeeFormRawValue): IEmployee | NewEmployee {
    return {
      ...rawEmployee,
      createdDate: dayjs(rawEmployee.createdDate, DATE_TIME_FORMAT),
      modifiedDate: dayjs(rawEmployee.modifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertEmployeeToEmployeeRawValue(
    employee: IEmployee | (Partial<NewEmployee> & EmployeeFormDefaults),
  ): EmployeeFormRawValue | PartialWithRequiredKeyOf<NewEmployeeFormRawValue> {
    return {
      ...employee,
      createdDate: employee.createdDate ? employee.createdDate.format(DATE_TIME_FORMAT) : undefined,
      modifiedDate: employee.modifiedDate ? employee.modifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
