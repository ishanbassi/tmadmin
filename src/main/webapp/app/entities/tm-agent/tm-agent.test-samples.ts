import dayjs from 'dayjs/esm';

import { ITmAgent, NewTmAgent } from './tm-agent.model';

export const sampleWithRequiredData: ITmAgent = {
  id: 31571,
};

export const sampleWithPartialData: ITmAgent = {
  id: 4816,
  firstName: 'Stephanie',
  lastName: 'Stroman',
  modifiedDate: dayjs('2024-06-02T13:35'),
  deleted: false,
  agentCode: 'ram',
};

export const sampleWithFullData: ITmAgent = {
  id: 25297,
  firstName: 'Vanessa',
  lastName: 'Boyle',
  address: 'weakly',
  createdDate: dayjs('2024-06-01T23:25'),
  modifiedDate: dayjs('2024-06-01T17:57'),
  deleted: true,
  companyName: 'repeat burial',
  agentCode: 'an',
  email: 'Maurice78@yahoo.com',
};

export const sampleWithNewData: NewTmAgent = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
