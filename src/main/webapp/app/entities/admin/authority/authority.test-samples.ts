import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: '68cf5863-6fcc-440c-8511-6de8b0655561',
};

export const sampleWithPartialData: IAuthority = {
  name: 'bfe7c6f8-cd79-4c3b-a412-a746ec8d6667',
};

export const sampleWithFullData: IAuthority = {
  name: '2844774b-176b-4009-abb1-aab4529254b0',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
