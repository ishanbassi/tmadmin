import dayjs from 'dayjs/esm';

import { ITmAgent, NewTmAgent } from './tm-agent.model';

export const sampleWithRequiredData: ITmAgent = {
  id: 3639,
};

export const sampleWithPartialData: ITmAgent = {
  id: 18587,
  fullName: 'though',
  createdDate: dayjs('2024-06-02T10:35'),
  modifiedDate: dayjs('2024-06-01T21:32'),
  companyName: 'whether knitting yowza',
  email: 'Karley_Crist@gmail.com',
};

export const sampleWithFullData: ITmAgent = {
  id: 29330,
  fullName: 'gee inquisitively',
  address: 'entrench anti impure',
  createdDate: dayjs('2024-06-02T05:04'),
  modifiedDate: dayjs('2024-06-02T15:07'),
  deleted: true,
  companyName: 'scrabble',
  agentCode: 'by',
  email: 'Eileen_Swift@hotmail.com',
};

export const sampleWithNewData: NewTmAgent = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
