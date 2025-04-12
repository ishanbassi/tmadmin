import dayjs from 'dayjs/esm';

import { ILead, NewLead } from './lead.model';

export const sampleWithRequiredData: ILead = {
  id: 5834,
};

export const sampleWithPartialData: ILead = {
  id: 258,
  fullName: 'but whoever but',
  phoneNumber: 'loudly honored likewise',
  selectedPackage: 'near correspondent',
  tmClass: 2181,
  comments: 'frantically',
  contactMethod: 'CALL',
  createdDate: dayjs('2025-04-11T20:51'),
  deleted: false,
  leadSource: 'falter indeed barge',
};

export const sampleWithFullData: ILead = {
  id: 9973,
  fullName: 'playfully psst',
  phoneNumber: 'cage potty',
  email: 'Meagan82@hotmail.com',
  city: 'Hermannhaven',
  brandName: 'lock on',
  selectedPackage: 'epic wherever coverall',
  tmClass: 8970,
  comments: 'whoever',
  contactMethod: 'EMAIL',
  createdDate: dayjs('2025-04-11T05:21'),
  modifiedDate: dayjs('2025-04-11T22:07'),
  deleted: true,
  status: 'DOCUMENTS_PENDING',
  leadSource: 'duh gosh',
};

export const sampleWithNewData: NewLead = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
