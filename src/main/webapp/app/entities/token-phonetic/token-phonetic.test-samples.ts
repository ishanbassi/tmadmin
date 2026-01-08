import { ITokenPhonetic, NewTokenPhonetic } from './token-phonetic.model';

export const sampleWithRequiredData: ITokenPhonetic = {
  id: 21436,
};

export const sampleWithPartialData: ITokenPhonetic = {
  id: 5231,
  algorithm: 'SOUNDEX',
};

export const sampleWithFullData: ITokenPhonetic = {
  id: 29937,
  algorithm: 'TRIPLE_METAPHONE',
  phoneticCode: 'if',
  secondaryPhoneticCode: 'boo a rotating',
};

export const sampleWithNewData: NewTokenPhonetic = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
