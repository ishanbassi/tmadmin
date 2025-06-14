import dayjs from 'dayjs/esm';

import { ICompany, NewCompany } from './company.model';

export const sampleWithRequiredData: ICompany = {
  id: 6800,
};

export const sampleWithPartialData: ICompany = {
  id: 32050,
  type: 'accompanist neatly frantically',
  cin: 'communicate testing impartial',
  natureOfBusiness: 'swelter extremely ashamed',
  state: 'atop',
  pincode: 'powerfully wee',
  createdDate: dayjs('2025-06-13T13:32'),
  modifiedDate: dayjs('2025-06-12T18:04'),
};

export const sampleWithFullData: ICompany = {
  id: 14111,
  type: 'clean',
  name: 'considering',
  cin: 'for joy among',
  gstin: 'once pleasure',
  natureOfBusiness: 'inside',
  address: 'slake',
  state: 'kowtow around mysteriously',
  pincode: 'uh-huh patiently following',
  city: 'Temple',
  createdDate: dayjs('2025-06-12T23:43'),
  modifiedDate: dayjs('2025-06-13T05:12'),
  deleted: false,
};

export const sampleWithNewData: NewCompany = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
