import dayjs from 'dayjs/esm';

import { ITrademark, NewTrademark } from './trademark.model';

export const sampleWithRequiredData: ITrademark = {
  id: 14850,
};

export const sampleWithPartialData: ITrademark = {
  id: 23274,
  applicationNo: 8202,
  agentName: 'drat',
  proprietorName: 'brand',
  proprietorAddress: 'notarize',
  headOffice: 'snarling accidentally adult',
  tmClass: 17329,
  journalNo: 23832,
  createdDate: dayjs('2024-05-21T22:56'),
  type: 'TRADEMARK',
  email: 'Berry_Watsica@hotmail.com',
  filingMode: 'hateful the psst',
  state: 'rapidly bewail',
};

export const sampleWithFullData: ITrademark = {
  id: 24239,
  name: 'discourse readily except',
  details: 'warming',
  applicationNo: 15005,
  applicationDate: dayjs('2024-05-22'),
  agentName: 'eek meh incidentally',
  agentAddress: 'adviser',
  proprietorName: 'wherever where excellent',
  proprietorAddress: 'best',
  headOffice: 'criminal rare baptise',
  imgUrl: 'than maestro astride',
  tmClass: 30777,
  journalNo: 13116,
  deleted: true,
  usage: 'supposing ouch bah',
  associatedTms: 'save even aftermath',
  trademarkStatus: 'readily who muddy',
  createdDate: dayjs('2024-05-21T21:20'),
  modifiedDate: dayjs('2024-05-21T20:17'),
  renewalDate: dayjs('2024-05-22'),
  type: 'IMAGEMARK',
  pageNo: 4695,
  source: 'SCRAPPER',
  phoneNumber: 'even as outside',
  email: 'Henri92@hotmail.com',
  organizationType: 'coop',
  normalizedName: 'broadcast darling',
  filingMode: 'barring sarong',
  state: 'anticodon',
  country: 'Uruguay',
};

export const sampleWithNewData: NewTrademark = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
