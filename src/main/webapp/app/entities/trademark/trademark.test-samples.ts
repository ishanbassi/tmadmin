import dayjs from 'dayjs/esm';

import { ITrademark, NewTrademark } from './trademark.model';

export const sampleWithRequiredData: ITrademark = {
  id: 17108,
};

export const sampleWithPartialData: ITrademark = {
  id: 26147,
  name: 'carefully duh airfreight',
  details: 'before considering',
  agentName: 'quickly uh-huh',
  agentAddress: 'scotch',
  proprietorAddress: 'market profitable clause',
  imgUrl: 'before restructure',
  tmClass: 2669,
  usage: 'speedily anxiously',
  associatedTms: 'which lamp somersault',
};

export const sampleWithFullData: ITrademark = {
  id: 3218,
  name: 'per',
  details: 'whether toad carefree',
  applicationNo: 29541,
  applicationDate: dayjs('2024-05-22'),
  agentName: 'boohoo honestly',
  agentAddress: 'sour',
  proprietorName: 'unless chives',
  proprietorAddress: 'source before',
  headOffice: 'MUMBAI',
  imgUrl: 'conflate',
  tmClass: 30600,
  journalNo: 7841,
  deleted: false,
  usage: 'insidious who considering',
  associatedTms: 'gadzooks bite',
  trademarkStatus: 'corrode incidentally zowie',
};

export const sampleWithNewData: NewTrademark = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
