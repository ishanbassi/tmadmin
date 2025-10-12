import dayjs from 'dayjs/esm';

import { IDocuments, NewDocuments } from './documents.model';

export const sampleWithRequiredData: IDocuments = {
  id: 23499,
};

export const sampleWithPartialData: IDocuments = {
  id: 463,
  documentType: 'SIGNED_POA',
  fileName: 'enthusiastically inside patiently',
  deleted: false,
  status: 'till overheard',
};

export const sampleWithFullData: IDocuments = {
  id: 13607,
  documentType: 'ADDRESS_PROOF',
  fileContentType: 'phew',
  fileName: 'inasmuch',
  fileUrl: 'hmph drat',
  createdDate: dayjs('2025-06-13T14:27'),
  modifiedDate: dayjs('2025-06-13T10:51'),
  deleted: false,
  status: 'phew hello',
};

export const sampleWithNewData: NewDocuments = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
