import { IPhonetics, NewPhonetics } from './phonetics.model';

export const sampleWithRequiredData: IPhonetics = {
  id: 14278,
};

export const sampleWithPartialData: IPhonetics = {
  id: 4588,
  sanitizedTm: 'ingratiate',
  phoneticSk: 'along linseed next',
  complete: true,
};

export const sampleWithFullData: IPhonetics = {
  id: 31653,
  sanitizedTm: 'successfully fibre gosh',
  phoneticPk: 'spotless',
  phoneticSk: 'embed zowie',
  complete: false,
};

export const sampleWithNewData: NewPhonetics = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
