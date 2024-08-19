import dayjs from 'dayjs/esm';

import { IPublishedTm, NewPublishedTm } from './published-tm.model';

export const sampleWithRequiredData: IPublishedTm = {
  id: 31411,
};

export const sampleWithPartialData: IPublishedTm = {
  id: 2076,
  name: 'issue forecast',
  details: 'loathsome',
  applicationNo: 13283,
  applicationDate: dayjs('2024-05-21'),
  agentName: 'prostrate seldom',
  proprietorAddress: 'time',
  headOffice: 'KOLKATA',
  imgUrl: 'till sand meh',
  usage: 'authentic',
  trademarkStatus: 'REGISTERED',
  modifiedDate: dayjs('2024-05-21T20:47'),
};

export const sampleWithFullData: IPublishedTm = {
  id: 17849,
  name: 'once blissfully',
  details: 'throbbing inasmuch',
  applicationNo: 18756,
  applicationDate: dayjs('2024-05-21'),
  agentName: 'scale',
  agentAddress: 'ick',
  proprietorName: 'from',
  proprietorAddress: 'unfinished against whoa',
  headOffice: 'KOLKATA',
  imgUrl: 'exactly',
  tmClass: 16044,
  journalNo: 1663,
  deleted: true,
  usage: 'wiretap undelete knavishly',
  associatedTms: 'before',
  trademarkStatus: 'PUBLISHED',
  createdDate: dayjs('2024-05-21T17:16'),
  modifiedDate: dayjs('2024-05-21T21:45'),
};

export const sampleWithNewData: NewPublishedTm = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
