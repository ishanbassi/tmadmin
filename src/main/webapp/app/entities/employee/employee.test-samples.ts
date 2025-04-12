import dayjs from 'dayjs/esm';

import { IEmployee, NewEmployee } from './employee.model';

export const sampleWithRequiredData: IEmployee = {
  id: 8899,
};

export const sampleWithPartialData: IEmployee = {
  id: 15010,
  fullName: 'bracelet gratefully make',
  phoneNumber: 'whose broadcast',
  email: 'Jeremy0@gmail.com',
  createdDate: dayjs('2025-04-11T19:01'),
  deleted: false,
};

export const sampleWithFullData: IEmployee = {
  id: 19019,
  fullName: 'sharply questionably softly',
  phoneNumber: 'yak near',
  email: 'Mya.Pagac46@gmail.com',
  createdDate: dayjs('2025-04-11T16:28'),
  modifiedDate: dayjs('2025-04-12T03:59'),
  deleted: false,
  designation: 'brr glum',
  joiningDate: dayjs('2025-04-11'),
};

export const sampleWithNewData: NewEmployee = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
