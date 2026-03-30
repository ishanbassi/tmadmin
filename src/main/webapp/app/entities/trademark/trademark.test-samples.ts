import dayjs from 'dayjs/esm';

import { ITrademark, NewTrademark } from './trademark.model';

export const sampleWithRequiredData: ITrademark = {
  id: 14850,
};

export const sampleWithPartialData: ITrademark = {
  id: 23274,
  applicationNo: 8202,
  agentName: 'drat',
  proprietorName: 'brand',
  proprietorAddress: 'notarize',
  headOffice: 'KOLKATA',
  tmClass: 9272,
  journalNo: 9061,
  createdDate: dayjs('2024-05-22T07:22'),
  type: 'TRADEMARK_WITH_IMAGE',
  email: 'Johnathan_Block@hotmail.com',
  filingMode: 'bah unbearably while',
  state: 'better catalog',
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
  type: 'TRADEMARK_WITH_IMAGE',
  pageNo: 20941,
  source: 'SCRAPPER',
  phoneNumber: 'curry',
  email: 'Favian_Barton16@gmail.com',
  organizationType: 'substantiate',
  normalizedName: 'safe import',
  filingMode: 'sheepishly',
  state: 'misguided',
  country: 'Kazakhstan',
};

export const sampleWithNewData: NewTrademark = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
