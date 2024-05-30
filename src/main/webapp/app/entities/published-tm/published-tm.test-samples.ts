import dayjs from 'dayjs/esm';

import { IPublishedTm, NewPublishedTm } from './published-tm.model';

export const sampleWithRequiredData: IPublishedTm = {
  id: 16810,
};

export const sampleWithPartialData: IPublishedTm = {
  id: 14830,
  proprietorName: 'who jovial which',
  headOffice: 'AHMEDABAD',
  imgUrl: 'instead cardigan',
  usage: 'although',
};

export const sampleWithFullData: IPublishedTm = {
  id: 3502,
  name: 'dirty till',
  details: 'nicely profess',
  applicationNo: 16494,
  applicationDate: dayjs('2024-05-22'),
  agentName: 'gamebird',
  agentAddress: 'dune bicker roadway',
  proprietorName: 'hint',
  proprietorAddress: 'whenever inspect',
  headOffice: 'MUMBAI',
  imgUrl: 'as bitterly',
  tmClass: 29874,
  journalNo: 8870,
  deleted: true,
  usage: 'woozy like chuckle',
  associatedTms: 'marxism',
};

export const sampleWithNewData: NewPublishedTm = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
