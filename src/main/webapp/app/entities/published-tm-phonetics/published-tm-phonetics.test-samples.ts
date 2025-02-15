import { IPublishedTmPhonetics, NewPublishedTmPhonetics } from './published-tm-phonetics.model';

export const sampleWithRequiredData: IPublishedTmPhonetics = {
  id: 25657,
};

export const sampleWithPartialData: IPublishedTmPhonetics = {
  id: 19077,
  sanitizedTm: 'which',
  complete: true,
};

export const sampleWithFullData: IPublishedTmPhonetics = {
  id: 47,
  sanitizedTm: 'step-mother lest',
  phoneticPk: 'regulate hurtful gleefully',
  phoneticSk: 'scornful urgently cram',
  complete: false,
};

export const sampleWithNewData: NewPublishedTmPhonetics = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
