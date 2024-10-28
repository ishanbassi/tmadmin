import dayjs from 'dayjs/esm';

import { IUserProfile, NewUserProfile } from './user-profile.model';

export const sampleWithRequiredData: IUserProfile = {
  id: 8030,
};

export const sampleWithPartialData: IUserProfile = {
  id: 2210,
  createdDate: dayjs('2024-10-19T00:39'),
  modifiedDate: dayjs('2024-10-18T21:52'),
  deleted: false,
};

export const sampleWithFullData: IUserProfile = {
  id: 26055,
  createdDate: dayjs('2024-10-18T17:14'),
  modifiedDate: dayjs('2024-10-18T18:26'),
  deleted: true,
};

export const sampleWithNewData: NewUserProfile = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
