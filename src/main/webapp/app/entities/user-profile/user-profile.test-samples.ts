import dayjs from 'dayjs/esm';

import { IUserProfile, NewUserProfile } from './user-profile.model';

export const sampleWithRequiredData: IUserProfile = {
  id: 4033,
  firstName: 'Tianna',
  lastName: 'Cruickshank',
};

export const sampleWithPartialData: IUserProfile = {
  id: 25987,
  createdDate: dayjs('2024-10-19T01:04'),
  modifiedDate: dayjs('2024-10-19T10:30'),
  deleted: true,
  firstName: 'Vesta',
  lastName: 'Abshire',
  city: 'Glennaside',
  zipCode: 15972,
  state: 'mortally',
  utmSource: 'anti accidentally',
};

export const sampleWithFullData: IUserProfile = {
  id: 9570,
  createdDate: dayjs('2024-10-19T05:09'),
  modifiedDate: dayjs('2024-10-19T08:33'),
  deleted: true,
  firstName: 'Doug',
  lastName: 'Shields',
  active: true,
  email: 'Lavina.Moen86@yahoo.com',
  phoneNumber: 'cuddly randomize',
  addressLine1: 'second duh',
  addressLine2: 'access',
  city: 'Savionfort',
  zipCode: 25030,
  state: 'apud',
  utmCampaign: 'sleepily pacemaker tall',
  utmSource: 'finally',
  utmMedium: 'per toaster',
  utmContent: 'fax',
};

export const sampleWithNewData: NewUserProfile = {
  firstName: 'Mertie',
  lastName: 'Tillman',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
