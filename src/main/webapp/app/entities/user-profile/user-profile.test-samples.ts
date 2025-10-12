import dayjs from 'dayjs/esm';

import { IUserProfile, NewUserProfile } from './user-profile.model';

export const sampleWithRequiredData: IUserProfile = {
  id: 4033,
  firstName: 'Tianna',
  lastName: 'Cruickshank',
  city: 'Hellerland',
  zipCode: 26375,
};

export const sampleWithPartialData: IUserProfile = {
  id: 23224,
  createdDate: dayjs('2024-10-18T23:57'),
  modifiedDate: dayjs('2024-10-19T06:10'),
  deleted: false,
  firstName: 'Waylon',
  lastName: 'Williamson',
  city: 'Albuquerque',
  zipCode: 19632,
  state: 'unpleasant yearn',
  utmCampaign: 'license scorn',
  utmSource: 'supposing coop bulky',
  utmContent: 'aside at',
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
  city: 'Mabellemouth',
  zipCode: 4150,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
