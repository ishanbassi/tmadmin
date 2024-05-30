import { IPublishedTmPhonetics, NewPublishedTmPhonetics } from './published-tm-phonetics.model';

export const sampleWithRequiredData: IPublishedTmPhonetics = {
  id: 16699,
};

export const sampleWithPartialData: IPublishedTmPhonetics = {
  id: 20274,
  phoneticPk: 'traverse stupendous unnaturally',
};

export const sampleWithFullData: IPublishedTmPhonetics = {
  id: 4487,
  sanitizedTm: 'authentication',
  phoneticPk: 'randomise',
  phoneticSk: 'inaugurate shanghai needily',
  complete: false,
};

export const sampleWithNewData: NewPublishedTmPhonetics = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
