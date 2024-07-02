import dayjs from 'dayjs/esm';

import { IPublishedTm, NewPublishedTm } from './published-tm.model';

export const sampleWithRequiredData: IPublishedTm = {
  id: 19875,
};

export const sampleWithPartialData: IPublishedTm = {
  id: 29171,
  name: 'woot from',
  applicationNo: 25308,
  applicationDate: dayjs('2024-05-21'),
  agentName: 'upon',
  agentAddress: 'near adolescent weakly',
  proprietorName: 'bandwidth once shoemaker',
  imgUrl: 'via answer limply',
  tmClass: 2826,
  journalNo: 20307,
  trademarkStatus: 'REJECTED',
};

export const sampleWithFullData: IPublishedTm = {
  id: 19858,
  name: 'shrilly masquerade',
  details: 'whoever chubby daintily',
  applicationNo: 1516,
  applicationDate: dayjs('2024-05-21'),
  agentName: 'from',
  agentAddress: 'unfinished against whoa',
  proprietorName: 'declaration drat',
  proprietorAddress: 'tidy against but',
  headOffice: 'CHENNAI',
  imgUrl: 'past which er',
  tmClass: 3158,
  journalNo: 20535,
  deleted: true,
  usage: 'pro pooh',
  associatedTms: 'lest hash',
  trademarkStatus: 'REJECTED',
};

export const sampleWithNewData: NewPublishedTm = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
