import dayjs from 'dayjs/esm';

import { IPublishedTm, NewPublishedTm } from './published-tm.model';

export const sampleWithRequiredData: IPublishedTm = {
  id: 23335,
};

export const sampleWithPartialData: IPublishedTm = {
  id: 8464,
  agentAddress: 'freely able mysteriously',
  proprietorAddress: 'helplessly astride',
  headOffice: 'KOLKATA',
  deleted: true,
  associatedTms: 'terribly reflection',
};

export const sampleWithFullData: IPublishedTm = {
  id: 1755,
  name: 'past',
  details: 'ah geez',
  applicationNo: 30888,
  applicationDate: dayjs('2024-05-22'),
  agentName: 'constant suddenly',
  agentAddress: 'sequester naturally',
  proprietorName: 'adjective duck supposing',
  proprietorAddress: 'meanwhile vainly',
  headOffice: 'MUMBAI',
  imgUrl: 'secret',
  tmClass: 32119,
  journalNo: 4917,
  deleted: false,
  usage: 'flub before underneath',
  associatedTms: 'pace oh',
  trademarkStatus: 'PROVISIONAL',
};

export const sampleWithNewData: NewPublishedTm = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
