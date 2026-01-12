import dayjs from 'dayjs/esm';

import { ITrademarkTokenFrequency, NewTrademarkTokenFrequency } from './trademark-token-frequency.model';

export const sampleWithRequiredData: ITrademarkTokenFrequency = {
  id: 15765,
};

export const sampleWithPartialData: ITrademarkTokenFrequency = {
  id: 32737,
  frequency: 23970,
  word: 'fabricate why gosh',
  deleted: true,
  modifiedDate: dayjs('2026-01-11T17:09'),
};

export const sampleWithFullData: ITrademarkTokenFrequency = {
  id: 31907,
  frequency: 767,
  word: 'versus ah expensive',
  createdDate: dayjs('2026-01-11T17:13'),
  deleted: true,
  modifiedDate: dayjs('2026-01-11T23:41'),
};

export const sampleWithNewData: NewTrademarkTokenFrequency = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
