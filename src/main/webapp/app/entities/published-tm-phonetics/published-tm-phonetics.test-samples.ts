import { IPublishedTmPhonetics, NewPublishedTmPhonetics } from './published-tm-phonetics.model';

export const sampleWithRequiredData: IPublishedTmPhonetics = {
  id: 7045,
};

export const sampleWithPartialData: IPublishedTmPhonetics = {
  id: 16201,
  complete: false,
};

export const sampleWithFullData: IPublishedTmPhonetics = {
  id: 20627,
  sanitizedTm: 'upward enormously',
  phoneticPk: 'potable',
  phoneticSk: 'versus uh-huh',
  complete: false,
};

export const sampleWithNewData: NewPublishedTmPhonetics = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
