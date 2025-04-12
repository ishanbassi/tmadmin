import { IPublishedTmPhonetics, NewPublishedTmPhonetics } from './published-tm-phonetics.model';

export const sampleWithRequiredData: IPublishedTmPhonetics = {
  id: 22249,
};

export const sampleWithPartialData: IPublishedTmPhonetics = {
  id: 13990,
  sanitizedTm: 'sheepishly scrawl',
  phoneticSk: 'beloved',
};

export const sampleWithFullData: IPublishedTmPhonetics = {
  id: 3615,
  sanitizedTm: 'consequently front badge',
  phoneticPk: 'thoroughly showy',
  phoneticSk: 'chill gee',
  complete: false,
};

export const sampleWithNewData: NewPublishedTmPhonetics = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
