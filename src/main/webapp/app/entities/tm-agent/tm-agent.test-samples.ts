import dayjs from 'dayjs/esm';

import { ITmAgent, NewTmAgent } from './tm-agent.model';

export const sampleWithRequiredData: ITmAgent = {
  id: 4816,
};

export const sampleWithPartialData: ITmAgent = {
  id: 961,
  address: 'astride',
  createdDate: dayjs('2024-06-02T09:51'),
  modifiedDate: dayjs('2024-06-01T16:33'),
  email: 'Alexandrine_Hintz@yahoo.com',
};

export const sampleWithFullData: ITmAgent = {
  id: 29568,
  fullName: 'pink weary versus',
  address: 'concerning produce brr',
  createdDate: dayjs('2024-06-02T12:35'),
  modifiedDate: dayjs('2024-06-02T07:31'),
  deleted: false,
  companyName: 'boo',
  agentCode: 'times',
  email: 'Veda.Okuneva90@yahoo.com',
};

export const sampleWithNewData: NewTmAgent = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
