import dayjs from 'dayjs/esm';

import { IEmployee, NewEmployee } from './employee.model';

export const sampleWithRequiredData: IEmployee = {
  id: 1274,
};

export const sampleWithPartialData: IEmployee = {
  id: 10242,
  phoneNumber: 'yum floodlight gleefully',
  email: 'Nichole.Hansen@gmail.com',
  createdDate: dayjs('2025-04-11T09:11'),
  modifiedDate: dayjs('2025-04-11T16:31'),
  deleted: false,
  designation: 'consequently',
};

export const sampleWithFullData: IEmployee = {
  id: 29283,
  fullName: 'upon loyally',
  phoneNumber: 'calmly lest',
  email: 'Arvilla43@gmail.com',
  createdDate: dayjs('2025-04-11T18:21'),
  modifiedDate: dayjs('2025-04-11T08:59'),
  deleted: false,
  designation: 'skyline boo plastic',
  joiningDate: dayjs('2025-04-11'),
};

export const sampleWithNewData: NewEmployee = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
