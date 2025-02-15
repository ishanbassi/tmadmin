import dayjs from 'dayjs/esm';

import { IPublishedTm, NewPublishedTm } from './published-tm.model';

export const sampleWithRequiredData: IPublishedTm = {
  id: 16200,
};

export const sampleWithPartialData: IPublishedTm = {
  id: 29852,
  name: 'phew unnaturally provided',
  applicationDate: dayjs('2024-05-22'),
  agentName: 'along',
  agentAddress: 'complete',
  headOffice: 'DELHI',
  imgUrl: 'tank huzzah considering',
  journalNo: 15841,
  usage: 'brandish via atop',
  associatedTms: 'besides abnormally tightly',
  modifiedDate: dayjs('2024-05-22T04:27'),
};

export const sampleWithFullData: IPublishedTm = {
  id: 28118,
  name: 'immediately',
  details: 'of glaring',
  applicationNo: 10537,
  applicationDate: dayjs('2024-05-22'),
  agentName: 'immediately tut',
  agentAddress: 'acidly manipulate roughly',
  proprietorName: 'solicit where',
  proprietorAddress: 'inquisitively',
  headOffice: 'MUMBAI',
  imgUrl: 'fearless noisily out',
  tmClass: 25220,
  journalNo: 15829,
  deleted: true,
  usage: 'zowie',
  associatedTms: 'impact vanadyl',
  trademarkStatus: 'HEARING',
  createdDate: dayjs('2024-05-21T21:54'),
  modifiedDate: dayjs('2024-05-22T10:32'),
  renewalDate: dayjs('2024-05-22'),
  type: 'IMAGEMARK',
};

export const sampleWithNewData: NewPublishedTm = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
