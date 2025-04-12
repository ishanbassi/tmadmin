import dayjs from 'dayjs/esm';

import { IUserProfile, NewUserProfile } from './user-profile.model';

export const sampleWithRequiredData: IUserProfile = {
  id: 4033,
};

export const sampleWithPartialData: IUserProfile = {
  id: 31140,
  createdDate: dayjs('2024-10-19T00:21'),
  modifiedDate: dayjs('2024-10-19T00:28'),
  deleted: false,
};

export const sampleWithFullData: IUserProfile = {
  id: 9570,
  createdDate: dayjs('2024-10-19T05:09'),
  modifiedDate: dayjs('2024-10-19T08:33'),
  deleted: true,
};

export const sampleWithNewData: NewUserProfile = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
