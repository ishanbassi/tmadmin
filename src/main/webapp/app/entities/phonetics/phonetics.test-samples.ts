import { IPhonetics, NewPhonetics } from './phonetics.model';

export const sampleWithRequiredData: IPhonetics = {
  id: 5281,
};

export const sampleWithPartialData: IPhonetics = {
  id: 8247,
  sanitizedTm: 'ah',
  phoneticPk: 'profane',
};

export const sampleWithFullData: IPhonetics = {
  id: 15238,
  sanitizedTm: 'behind swear',
  phoneticPk: 'developmental enthusiastically',
  phoneticSk: 'obedient cod',
  complete: false,
};

export const sampleWithNewData: NewPhonetics = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
