import dayjs from 'dayjs/esm';

import { ITrademark, NewTrademark } from './trademark.model';

export const sampleWithRequiredData: ITrademark = {
  id: 14255,
};

export const sampleWithPartialData: ITrademark = {
  id: 28233,
  name: 'velocity',
  agentName: 'yahoo where',
  proprietorName: 'federate',
  proprietorAddress: 'pessimistic wrongly signature',
  tmClass: 9827,
  journalNo: 8095,
  usage: 'condense anenst scope',
  associatedTms: 'than step at',
  renewalDate: dayjs('2024-05-21'),
  type: 'SOUNDMARK',
  pageNo: 21648,
};

export const sampleWithFullData: ITrademark = {
  id: 15430,
  name: 'yearly boohoo decision',
  details: 'failing',
  applicationNo: 22389,
  applicationDate: dayjs('2024-05-22'),
  agentName: 'petticoat which',
  agentAddress: 'but partridge',
  proprietorName: 'bleakly',
  proprietorAddress: 'fowl substitution',
  headOffice: 'AHMEDABAD',
  imgUrl: 'acidly',
  tmClass: 16065,
  journalNo: 11146,
  deleted: true,
  usage: 'lend babyish',
  associatedTms: 'though',
  trademarkStatus: 'until incidentally decide',
  createdDate: dayjs('2024-05-21T20:53'),
  modifiedDate: dayjs('2024-05-22T07:57'),
  renewalDate: dayjs('2024-05-22'),
  type: 'TRADEMARK',
  pageNo: 241,
};

export const sampleWithNewData: NewTrademark = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
