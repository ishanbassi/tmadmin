import dayjs from 'dayjs/esm';

import { ITrademark, NewTrademark } from './trademark.model';

export const sampleWithRequiredData: ITrademark = {
  id: 14850,
};

export const sampleWithPartialData: ITrademark = {
  id: 10413,
  applicationNo: 11215,
  agentName: 'grouchy flu that',
  proprietorName: 'electrify unexpectedly',
  proprietorAddress: 'boohoo',
  headOffice: 'KOLKATA',
  tmClass: 20712,
  journalNo: 28032,
  createdDate: dayjs('2024-05-21T16:26'),
  type: 'SOUNDMARK',
  email: 'Ebony32@hotmail.com',
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
  phoneNumber: 'debut brr athwart',
  email: 'Jessie_Corkery77@yahoo.com',
  organizationType: 'barring',
  normalizedName: 'meh safe',
};

export const sampleWithNewData: NewTrademark = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
