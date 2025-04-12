import dayjs from 'dayjs/esm';

import { ITmAgent, NewTmAgent } from './tm-agent.model';

export const sampleWithRequiredData: ITmAgent = {
  id: 17792,
};

export const sampleWithPartialData: ITmAgent = {
  id: 13664,
  address: 'if ram',
  createdDate: dayjs('2024-06-01T21:56'),
  companyName: 'opine questionably repeat',
  agentCode: 'evenly an successfully',
};

export const sampleWithFullData: ITmAgent = {
  id: 2836,
  fullName: 'buoyant whenever self-esteem',
  address: 'definite furthermore bad',
  createdDate: dayjs('2024-06-01T22:34'),
  modifiedDate: dayjs('2024-06-02T01:20'),
  deleted: true,
  companyName: 'concerning',
  agentCode: 'afore yowza politely',
  email: 'Adalberto37@yahoo.com',
};

export const sampleWithNewData: NewTmAgent = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
