import dayjs from 'dayjs/esm';

import { ITrademark, NewTrademark } from './trademark.model';

export const sampleWithRequiredData: ITrademark = {
  id: 12668,
};

export const sampleWithPartialData: ITrademark = {
  id: 20007,
  name: 'woot glamorous',
  applicationNo: 314,
  agentName: 'gadzooks reckless nestle',
  agentAddress: 'advantage before micromanage',
  proprietorAddress: 'quiet best',
  headOffice: 'CHENNAI',
  imgUrl: 'and below indeed',
  tmClass: 16163,
  usage: 'until',
  trademarkStatus: 'OPPOSED',
  modifiedDate: dayjs('2024-05-22T15:57'),
};

export const sampleWithFullData: ITrademark = {
  id: 17564,
  name: 'forebear',
  details: 'silent now',
  applicationNo: 32767,
  applicationDate: dayjs('2024-05-22'),
  agentName: 'ear',
  agentAddress: 'unlike nourish',
  proprietorName: 'ferociously',
  proprietorAddress: 'noisily open whether',
  headOffice: 'DELHI',
  imgUrl: 'unless athwart gadzooks',
  tmClass: 21199,
  journalNo: 6417,
  deleted: true,
  usage: 'sharply',
  associatedTms: 'what',
  trademarkStatus: 'PUBLISHED',
  createdDate: dayjs('2024-05-21T20:32'),
  modifiedDate: dayjs('2024-05-21T19:42'),
};

export const sampleWithNewData: NewTrademark = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
