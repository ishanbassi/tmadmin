import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICompany, NewCompany } from '../company.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICompany for edit and NewCompanyFormGroupInput for create.
 */
type CompanyFormGroupInput = ICompany | PartialWithRequiredKeyOf<NewCompany>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ICompany | NewCompany> = Omit<T, 'createdDate' | 'modifiedDate'> & {
  createdDate?: string | null;
  modifiedDate?: string | null;
};

type CompanyFormRawValue = FormValueOf<ICompany>;

type NewCompanyFormRawValue = FormValueOf<NewCompany>;

type CompanyFormDefaults = Pick<NewCompany, 'id' | 'createdDate' | 'modifiedDate' | 'deleted'>;

type CompanyFormGroupContent = {
  id: FormControl<CompanyFormRawValue['id'] | NewCompany['id']>;
  type: FormControl<CompanyFormRawValue['type']>;
  name: FormControl<CompanyFormRawValue['name']>;
  cin: FormControl<CompanyFormRawValue['cin']>;
  gstin: FormControl<CompanyFormRawValue['gstin']>;
  natureOfBusiness: FormControl<CompanyFormRawValue['natureOfBusiness']>;
  address: FormControl<CompanyFormRawValue['address']>;
  state: FormControl<CompanyFormRawValue['state']>;
  pincode: FormControl<CompanyFormRawValue['pincode']>;
  city: FormControl<CompanyFormRawValue['city']>;
  createdDate: FormControl<CompanyFormRawValue['createdDate']>;
  modifiedDate: FormControl<CompanyFormRawValue['modifiedDate']>;
  deleted: FormControl<CompanyFormRawValue['deleted']>;
  user: FormControl<CompanyFormRawValue['user']>;
};

export type CompanyFormGroup = FormGroup<CompanyFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CompanyFormService {
  createCompanyFormGroup(company: CompanyFormGroupInput = { id: null }): CompanyFormGroup {
    const companyRawValue = this.convertCompanyToCompanyRawValue({
      ...this.getFormDefaults(),
      ...company,
    });
    return new FormGroup<CompanyFormGroupContent>({
      id: new FormControl(
        { value: companyRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      type: new FormControl(companyRawValue.type),
      name: new FormControl(companyRawValue.name),
      cin: new FormControl(companyRawValue.cin),
      gstin: new FormControl(companyRawValue.gstin),
      natureOfBusiness: new FormControl(companyRawValue.natureOfBusiness),
      address: new FormControl(companyRawValue.address),
      state: new FormControl(companyRawValue.state),
      pincode: new FormControl(companyRawValue.pincode),
      city: new FormControl(companyRawValue.city),
      createdDate: new FormControl(companyRawValue.createdDate),
      modifiedDate: new FormControl(companyRawValue.modifiedDate),
      deleted: new FormControl(companyRawValue.deleted),
      user: new FormControl(companyRawValue.user),
    });
  }

  getCompany(form: CompanyFormGroup): ICompany | NewCompany {
    return this.convertCompanyRawValueToCompany(form.getRawValue() as CompanyFormRawValue | NewCompanyFormRawValue);
  }

  resetForm(form: CompanyFormGroup, company: CompanyFormGroupInput): void {
    const companyRawValue = this.convertCompanyToCompanyRawValue({ ...this.getFormDefaults(), ...company });
    form.reset(
      {
        ...companyRawValue,
        id: { value: companyRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CompanyFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      modifiedDate: currentTime,
      deleted: false,
    };
  }

  private convertCompanyRawValueToCompany(rawCompany: CompanyFormRawValue | NewCompanyFormRawValue): ICompany | NewCompany {
    return {
      ...rawCompany,
      createdDate: dayjs(rawCompany.createdDate, DATE_TIME_FORMAT),
      modifiedDate: dayjs(rawCompany.modifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertCompanyToCompanyRawValue(
    company: ICompany | (Partial<NewCompany> & CompanyFormDefaults),
  ): CompanyFormRawValue | PartialWithRequiredKeyOf<NewCompanyFormRawValue> {
    return {
      ...company,
      createdDate: company.createdDate ? company.createdDate.format(DATE_TIME_FORMAT) : undefined,
      modifiedDate: company.modifiedDate ? company.modifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
