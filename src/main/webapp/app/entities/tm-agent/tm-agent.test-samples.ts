import dayjs from 'dayjs/esm';

import { ITmAgent, NewTmAgent } from './tm-agent.model';

export const sampleWithRequiredData: ITmAgent = {
  id: 18240,
};

export const sampleWithPartialData: ITmAgent = {
  id: 23711,
  address: 'gadzooks',
  createdDate: dayjs('2024-06-01T19:37'),
};

export const sampleWithFullData: ITmAgent = {
  id: 3929,
  agentCode: 25910,
  firstName: 'Eli',
  lastName: 'Howell',
  address: 'hurtful',
  createdDate: dayjs('2024-06-02T14:06'),
  modifiedDate: dayjs('2024-06-02T15:45'),
};

export const sampleWithNewData: NewTmAgent = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
