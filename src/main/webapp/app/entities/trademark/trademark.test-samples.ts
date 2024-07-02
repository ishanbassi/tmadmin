import dayjs from 'dayjs/esm';

import { ITrademark, NewTrademark } from './trademark.model';

export const sampleWithRequiredData: ITrademark = {
  id: 15180,
};

export const sampleWithPartialData: ITrademark = {
  id: 28703,
  details: 'wherever sensationalize',
  applicationNo: 10932,
  agentName: 'across funnel pish',
  proprietorName: 'energetic',
  proprietorAddress: 'phooey',
  imgUrl: 'haversack',
  tmClass: 11243,
  journalNo: 17374,
  deleted: true,
  trademarkStatus: 'PROVISIONAL',
};

export const sampleWithFullData: ITrademark = {
  id: 24970,
  name: 'thorny phooey pair',
  details: 'surface how',
  applicationNo: 14120,
  applicationDate: dayjs('2024-05-22'),
  agentName: 'lest',
  agentAddress: 'forebear',
  proprietorName: 'silent now',
  proprietorAddress: 'yet excepting whereas',
  headOffice: 'KOLKATA',
  imgUrl: 'yet',
  tmClass: 13658,
  journalNo: 8039,
  deleted: false,
  usage: 'even similarity crowded',
  associatedTms: 'unless athwart gadzooks',
  trademarkStatus: 'HEARING',
};

export const sampleWithNewData: NewTrademark = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
