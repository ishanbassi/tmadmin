import dayjs from 'dayjs/esm';

import { IPublishedTm, NewPublishedTm } from './published-tm.model';

export const sampleWithRequiredData: IPublishedTm = {
  id: 14839,
};

export const sampleWithPartialData: IPublishedTm = {
  id: 16200,
  name: 'beneath',
  details: 'regurgitate near',
  agentName: 'next which',
  agentAddress: 'poorly drat',
  proprietorName: 'dry test the',
  tmClass: 17849,
  deleted: true,
  associatedTms: 'eek kindheartedly throbbing',
  trademarkStatus: 'minus scientific',
  modifiedDate: dayjs('2024-05-22T13:17'),
  renewalDate: dayjs('2024-05-21'),
  type: 'IMAGEMARK',
  pageNo: 26630,
};

export const sampleWithFullData: IPublishedTm = {
  id: 32566,
  name: 'professionalize',
  details: 'of',
  applicationNo: 13728,
  applicationDate: dayjs('2024-05-22'),
  agentName: 'meanwhile even boatload',
  agentAddress: 'gosh wiretap undelete',
  proprietorName: 'aquaplane',
  proprietorAddress: 'stealthily bleakly',
  headOffice: 'DELHI',
  imgUrl: 'afore than pfft',
  tmClass: 10342,
  journalNo: 1308,
  deleted: false,
  usage: 'equally without',
  associatedTms: 'valiantly stipulate',
  trademarkStatus: 'slope',
  createdDate: dayjs('2024-05-22T01:46'),
  modifiedDate: dayjs('2024-05-22T10:20'),
  renewalDate: dayjs('2024-05-22'),
  type: 'SOUNDMARK',
  pageNo: 32326,
};

export const sampleWithNewData: NewPublishedTm = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
