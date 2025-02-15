import dayjs from 'dayjs/esm';

import { ITrademark, NewTrademark } from './trademark.model';

export const sampleWithRequiredData: ITrademark = {
  id: 31789,
};

export const sampleWithPartialData: ITrademark = {
  id: 15701,
  name: 'from',
  details: 'slowly gently how',
  agentAddress: 'schematise courtroom hmph',
  proprietorAddress: 'except bleakly although',
  headOffice: 'MUMBAI',
  journalNo: 22676,
  deleted: true,
  associatedTms: 'chiffonier while which',
  trademarkStatus: 'aged',
  type: 'TRADEMARK',
};

export const sampleWithFullData: ITrademark = {
  id: 4144,
  name: 'probable',
  details: 'aw',
  applicationNo: 30797,
  applicationDate: dayjs('2024-05-21'),
  agentName: 'failing',
  agentAddress: 'given productive final',
  proprietorName: 'hm newsstand',
  proprietorAddress: 'aside hmph provided',
  headOffice: 'CHENNAI',
  imgUrl: 'now lend babyish',
  tmClass: 6782,
  journalNo: 24059,
  deleted: false,
  usage: 'usually reword off',
  associatedTms: 'which',
  trademarkStatus: 'certainly frantically',
  createdDate: dayjs('2024-05-22T06:26'),
  modifiedDate: dayjs('2024-05-22T00:23'),
  renewalDate: dayjs('2024-05-22'),
  type: 'IMAGEMARK',
};

export const sampleWithNewData: NewTrademark = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
