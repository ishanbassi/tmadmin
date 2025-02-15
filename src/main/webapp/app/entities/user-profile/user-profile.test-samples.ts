import dayjs from 'dayjs/esm';

import { IUserProfile, NewUserProfile } from './user-profile.model';

export const sampleWithRequiredData: IUserProfile = {
  id: 24440,
};

export const sampleWithPartialData: IUserProfile = {
  id: 11619,
  createdDate: dayjs('2024-10-18T17:08'),
  modifiedDate: dayjs('2024-10-19T01:26'),
  deleted: false,
};

export const sampleWithFullData: IUserProfile = {
  id: 11682,
  createdDate: dayjs('2024-10-18T12:18'),
  modifiedDate: dayjs('2024-10-18T16:21'),
  deleted: true,
};

export const sampleWithNewData: NewUserProfile = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
