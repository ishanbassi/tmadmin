import { IPhonetics, NewPhonetics } from './phonetics.model';

export const sampleWithRequiredData: IPhonetics = {
  id: 22366,
};

export const sampleWithPartialData: IPhonetics = {
  id: 22140,
  sanitizedTm: 'yuck reproach',
  phoneticPk: 'once unsteady skeletal',
  phoneticSk: 'whose under',
  complete: true,
};

export const sampleWithFullData: IPhonetics = {
  id: 15405,
  sanitizedTm: 'vaguely',
  phoneticPk: 'brr fooey',
  phoneticSk: 'humble combine employ',
  complete: false,
};

export const sampleWithNewData: NewPhonetics = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
