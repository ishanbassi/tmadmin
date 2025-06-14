import dayjs from 'dayjs/esm';

import { IPublishedTm, NewPublishedTm } from './published-tm.model';

export const sampleWithRequiredData: IPublishedTm = {
  id: 31979,
};

export const sampleWithPartialData: IPublishedTm = {
  id: 5070,
  details: 'solemnly that even',
  headOffice: 'DELHI',
  tmClass: 28559,
  journalNo: 8282,
  deleted: false,
  usage: 'about minion ecliptic',
  associatedTms: 'regularly',
  trademarkStatus: 'when flustered',
  createdDate: dayjs('2024-05-21T22:53'),
  renewalDate: dayjs('2024-05-21'),
  type: 'SOUNDMARK',
};

export const sampleWithFullData: IPublishedTm = {
  id: 24414,
  name: 'confound aha storyboard',
  details: 'joshingly than',
  applicationNo: 27432,
  applicationDate: dayjs('2024-05-22'),
  agentName: 'furthermore shudder',
  agentAddress: 'anaesthetise bowling',
  proprietorName: 'yesterday tributary ideal',
  proprietorAddress: 'so',
  headOffice: 'DELHI',
  imgUrl: 'beneath aha',
  tmClass: 4988,
  journalNo: 15572,
  deleted: true,
  usage: 'oof',
  associatedTms: 'fatherly',
  trademarkStatus: 'physically near',
  createdDate: dayjs('2024-05-22T00:28'),
  modifiedDate: dayjs('2024-05-22T07:28'),
  renewalDate: dayjs('2024-05-22'),
  type: 'TRADEMARK',
  pageNo: 5010,
};

export const sampleWithNewData: NewPublishedTm = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
