import dayjs from 'dayjs/esm';

import { ITrademark, NewTrademark } from './trademark.model';

export const sampleWithRequiredData: ITrademark = {
  id: 14850,
};

export const sampleWithPartialData: ITrademark = {
  id: 19482,
  applicationNo: 14738,
  agentName: 'ethyl drat strategy',
  proprietorName: 'who',
  proprietorAddress: 'memorable ew',
  headOffice: 'DELHI',
  tmClass: 26694,
  journalNo: 17002,
  createdDate: dayjs('2024-05-21T20:18'),
  type: 'TRADEMARK_WITH_IMAGE',
};

export const sampleWithFullData: ITrademark = {
  id: 24239,
  name: 'discourse readily except',
  details: 'warming',
  applicationNo: 15005,
  applicationDate: dayjs('2024-05-22'),
  agentName: 'eek meh incidentally',
  agentAddress: 'adviser',
  proprietorName: 'wherever where excellent',
  proprietorAddress: 'best',
  headOffice: 'CHENNAI',
  imgUrl: 'geez official',
  tmClass: 27748,
  journalNo: 5745,
  deleted: false,
  usage: 'inasmuch',
  associatedTms: 'fess passport',
  trademarkStatus: 'EXPIRED',
  createdDate: dayjs('2024-05-21T21:24'),
  modifiedDate: dayjs('2024-05-22T14:40'),
  renewalDate: dayjs('2024-05-22'),
  type: 'IMAGEMARK',
  pageNo: 27416,
  source: 'MEMBER_PORTAL',
};

export const sampleWithNewData: NewTrademark = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
