import dayjs from 'dayjs/esm';

import { IPublishedTm, NewPublishedTm } from './published-tm.model';

export const sampleWithRequiredData: IPublishedTm = {
  id: 15565,
};

export const sampleWithPartialData: IPublishedTm = {
  id: 19287,
  applicationNo: 11616,
  applicationDate: dayjs('2024-05-21'),
  agentName: 'babyish boohoo',
  proprietorAddress: 'brook',
  headOffice: 'CHENNAI',
  tmClass: 17032,
  deleted: false,
  usage: 'sturdy',
  createdDate: dayjs('2024-05-21T19:18'),
};

export const sampleWithFullData: IPublishedTm = {
  id: 4953,
  name: 'tank huzzah considering',
  details: 'before edge',
  applicationNo: 31211,
  applicationDate: dayjs('2024-05-22'),
  agentName: 'than toe instead',
  agentAddress: 'tiny lest',
  proprietorName: 'translation zowie',
  proprietorAddress: 'clean',
  headOffice: 'MUMBAI',
  imgUrl: 'zowie',
  tmClass: 30757,
  journalNo: 24717,
  deleted: false,
  usage: 'pigpen',
  associatedTms: 'celebrated',
  trademarkStatus: 'fit tag',
  createdDate: dayjs('2024-05-21T21:18'),
  modifiedDate: dayjs('2024-05-22T00:10'),
  renewalDate: dayjs('2024-05-21'),
  type: 'SOUNDMARK',
  pageNo: 9240,
};

export const sampleWithNewData: NewPublishedTm = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
