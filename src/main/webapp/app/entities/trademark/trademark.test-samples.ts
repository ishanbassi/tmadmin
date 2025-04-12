import dayjs from 'dayjs/esm';

import { ITrademark, NewTrademark } from './trademark.model';

export const sampleWithRequiredData: ITrademark = {
  id: 14850,
};

export const sampleWithPartialData: ITrademark = {
  id: 25950,
  applicationNo: 19482,
  agentName: 'extroverted chase',
  proprietorName: 'strategy napkin',
  proprietorAddress: 'vivid whenever bah',
  headOffice: 'CHENNAI',
  tmClass: 435,
  journalNo: 17329,
  createdDate: dayjs('2024-05-22T09:34'),
  type: 'IMAGEMARK',
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
  trademarkStatus: 'phew debut brr',
  createdDate: dayjs('2024-05-21T22:48'),
  modifiedDate: dayjs('2024-05-21T17:07'),
  renewalDate: dayjs('2024-05-22'),
  type: 'TRADEMARK',
  pageNo: 20941,
};

export const sampleWithNewData: NewTrademark = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
