import dayjs from 'dayjs/esm';

import { ITmAgent, NewTmAgent } from './tm-agent.model';

export const sampleWithRequiredData: ITmAgent = {
  id: 17792,
};

export const sampleWithPartialData: ITmAgent = {
  id: 13664,
  firstName: 'Josefa',
  lastName: 'Turcotte',
  modifiedDate: dayjs('2024-06-02T00:22'),
  deleted: false,
};

export const sampleWithFullData: ITmAgent = {
  id: 3929,
  agentCode: 'aw phew regulate',
  firstName: 'Tressie',
  lastName: 'Mraz',
  address: 'unloose evenly',
  createdDate: dayjs('2024-06-02T04:02'),
  modifiedDate: dayjs('2024-06-02T07:30'),
  deleted: false,
  companyName: 'lynx contrast spiritual',
};

export const sampleWithNewData: NewTmAgent = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
