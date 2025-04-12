import dayjs from 'dayjs/esm';

import { ITrademark, NewTrademark } from './trademark.model';

export const sampleWithRequiredData: ITrademark = {
  id: 895,
};

export const sampleWithPartialData: ITrademark = {
  id: 2760,
  details: 'rapid',
  applicationNo: 10885,
  agentName: 'monsoon',
  agentAddress: 'reckless nestle mechanically',
  proprietorName: 'before',
  proprietorAddress: 'from quiet',
  tmClass: 20315,
  deleted: false,
  associatedTms: 'phooey pair position',
  createdDate: dayjs('2024-05-21T19:08'),
  modifiedDate: dayjs('2024-05-22T12:13'),
  pageNo: 26121,
};

export const sampleWithFullData: ITrademark = {
  id: 12584,
  name: 'hm',
  details: 'hence',
  applicationNo: 27834,
  applicationDate: dayjs('2024-05-22'),
  agentName: 'phew physical',
  agentAddress: 'across placebo unlike',
  proprietorName: 'hence ferociously certainly',
  proprietorAddress: 'open whether',
  headOffice: 'DELHI',
  imgUrl: 'unless athwart gadzooks',
  tmClass: 21199,
  journalNo: 6417,
  deleted: true,
  usage: 'sharply',
  associatedTms: 'what',
  trademarkStatus: 'regionalism boohoo hmph',
  createdDate: dayjs('2024-05-22T00:16'),
  modifiedDate: dayjs('2024-05-22T12:11'),
  renewalDate: dayjs('2024-05-22'),
  type: 'TRADEMARK',
  pageNo: 25398,
};

export const sampleWithNewData: NewTrademark = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
