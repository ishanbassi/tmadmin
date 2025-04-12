import dayjs from 'dayjs/esm';

import { IUserProfile, NewUserProfile } from './user-profile.model';

export const sampleWithRequiredData: IUserProfile = {
  id: 12176,
};

export const sampleWithPartialData: IUserProfile = {
  id: 17574,
  createdDate: dayjs('2024-10-18T16:03'),
  modifiedDate: dayjs('2024-10-18T17:14'),
};

export const sampleWithFullData: IUserProfile = {
  id: 22792,
  createdDate: dayjs('2024-10-19T00:09'),
  modifiedDate: dayjs('2024-10-19T03:39'),
  deleted: true,
};

export const sampleWithNewData: NewUserProfile = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
