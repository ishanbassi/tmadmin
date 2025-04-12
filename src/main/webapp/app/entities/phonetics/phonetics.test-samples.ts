import { IPhonetics, NewPhonetics } from './phonetics.model';

export const sampleWithRequiredData: IPhonetics = {
  id: 29354,
};

export const sampleWithPartialData: IPhonetics = {
  id: 6061,
  phoneticPk: 'how',
  phoneticSk: 'outlast',
  complete: false,
};

export const sampleWithFullData: IPhonetics = {
  id: 6115,
  sanitizedTm: 'dye',
  phoneticPk: 'hm brick whoa',
  phoneticSk: 'salty',
  complete: false,
};

export const sampleWithNewData: NewPhonetics = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
