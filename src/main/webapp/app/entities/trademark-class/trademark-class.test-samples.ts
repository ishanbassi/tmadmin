import dayjs from 'dayjs/esm';

import { ITrademarkClass, NewTrademarkClass } from './trademark-class.model';

export const sampleWithRequiredData: ITrademarkClass = {
  id: 23408,
};

export const sampleWithPartialData: ITrademarkClass = {
  id: 30207,
  code: 22048,
  tmClass: 8266,
  title: 'especially gadzooks',
  description: 'dividend until what',
  modifiedDate: dayjs('2025-07-15T22:53'),
};

export const sampleWithFullData: ITrademarkClass = {
  id: 25544,
  code: 27210,
  tmClass: 16448,
  keyword: 'mechanically repurpose coliseum',
  title: 'while tooth',
  description: 'among queasily athwart',
  createdDate: dayjs('2025-07-15T11:00'),
  modifiedDate: dayjs('2025-07-16T04:06'),
  deleted: false,
};

export const sampleWithNewData: NewTrademarkClass = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
