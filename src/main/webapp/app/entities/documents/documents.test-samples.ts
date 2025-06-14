import dayjs from 'dayjs/esm';

import { IDocuments, NewDocuments } from './documents.model';

export const sampleWithRequiredData: IDocuments = {
  id: 23499,
};

export const sampleWithPartialData: IDocuments = {
  id: 19957,
  documentType: 'since',
  createdDate: dayjs('2025-06-12T17:27'),
};

export const sampleWithFullData: IDocuments = {
  id: 13607,
  documentType: 'apud beautifully iterate',
  fileUrl: 'supposing',
  createdDate: dayjs('2025-06-13T07:06'),
  modifiedDate: dayjs('2025-06-13T04:20'),
  deleted: false,
};

export const sampleWithNewData: NewDocuments = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
