import dayjs from 'dayjs/esm';

import { ILead, NewLead } from './lead.model';

export const sampleWithRequiredData: ILead = {
  id: 4691,
};

export const sampleWithPartialData: ILead = {
  id: 3793,
  email: 'Dejuan_Hudson93@gmail.com',
  city: 'Port Tremayne',
  selectedPackage: 'terribly finger phooey',
  tmClass: 11484,
  modifiedDate: dayjs('2025-04-11T13:13'),
  status: 'CONVERTED',
  leadSource: 'outside replicate',
};

export const sampleWithFullData: ILead = {
  id: 27075,
  fullName: 'hospitalization catalog',
  phoneNumber: 'meanwhile',
  email: 'Ruben.Lesch40@gmail.com',
  city: 'Gloverbury',
  brandName: 'upon sinful',
  selectedPackage: 'gee querulous utterly',
  tmClass: 23188,
  comments: 'slap until awkwardly',
  contactMethod: 'CALL',
  createdDate: dayjs('2025-04-11T21:32'),
  modifiedDate: dayjs('2025-04-11T04:40'),
  deleted: false,
  status: 'LOST',
  leadSource: 'once rust',
};

export const sampleWithNewData: NewLead = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
