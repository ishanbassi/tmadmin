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
  trademarkStatus: 'in',
  modifiedDate: dayjs('2024-05-21T22:40'),
};

export const sampleWithFullData: ITrademark = {
  id: 31593,
  name: 'weepy doubtfully',
  details: 'aside fahrenheit',
  applicationNo: 13962,
  applicationDate: dayjs('2024-05-21'),
  agentName: 'elf yet unlucky',
  agentAddress: 'and',
  proprietorName: 'yawningly famously',
  proprietorAddress: 'gee',
  headOffice: 'KOLKATA',
  imgUrl: 'gadzooks',
  tmClass: 21199,
  journalNo: 6417,
  deleted: true,
  usage: 'sharply',
  associatedTms: 'what',
  trademarkStatus: 'regionalism boohoo hmph',
  createdDate: dayjs('2024-05-22T00:16'),
  modifiedDate: dayjs('2024-05-22T12:11'),
};

export const sampleWithNewData: NewTrademark = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
