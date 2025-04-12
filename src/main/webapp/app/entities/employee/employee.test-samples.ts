import dayjs from 'dayjs/esm';

import { IEmployee, NewEmployee } from './employee.model';

export const sampleWithRequiredData: IEmployee = {
  id: 20898,
};

export const sampleWithPartialData: IEmployee = {
  id: 4571,
  fullName: 'coordinator',
  phoneNumber: 'yuck',
  email: 'Katheryn4@yahoo.com',
  createdDate: dayjs('2025-04-11T19:38'),
  modifiedDate: dayjs('2025-04-11T09:05'),
  designation: 'snow',
};

export const sampleWithFullData: IEmployee = {
  id: 22670,
  fullName: 'excitedly precis',
  phoneNumber: 'baffle beneath',
  email: 'Arne54@hotmail.com',
  createdDate: dayjs('2025-04-11T20:44'),
  modifiedDate: dayjs('2025-04-11T13:22'),
  deleted: true,
  designation: 'laparoscope',
  joiningDate: dayjs('2025-04-12'),
};

export const sampleWithNewData: NewEmployee = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
