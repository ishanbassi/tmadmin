import dayjs from 'dayjs/esm';

import { ILead, NewLead } from './lead.model';

export const sampleWithRequiredData: ILead = {
  id: 12555,
};

export const sampleWithPartialData: ILead = {
  id: 25512,
  fullName: 'throbbing',
  phoneNumber: 'powerful',
  email: 'Janae_Weissnat-Runolfsson@gmail.com',
  city: 'Kassandraborough',
  brandName: 'newsletter',
  selectedPackage: 'silt fatally antagonize',
  createdDate: dayjs('2025-04-11T11:54'),
  deleted: false,
  leadSource: 'presell',
};

export const sampleWithFullData: ILead = {
  id: 26383,
  fullName: 'worthy',
  phoneNumber: 'crafty',
  email: 'Betty_Klocko72@hotmail.com',
  city: 'Blaine',
  brandName: 'lanky',
  selectedPackage: 'downshift',
  tmClass: 9996,
  comments: 'phooey suddenly monster',
  contactMethod: 'MESSAGE',
  createdDate: dayjs('2025-04-12T03:32'),
  modifiedDate: dayjs('2025-04-11T20:53'),
  deleted: true,
  status: 'NEW',
  leadSource: 'except with cruel',
};

export const sampleWithNewData: NewLead = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
