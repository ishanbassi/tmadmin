import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: 'b5e1cf43-6b9e-4e3e-afda-89ba8f4881ca',
};

export const sampleWithPartialData: IAuthority = {
  name: '4f3f864b-6032-4357-b24a-4d96d6116973',
};

export const sampleWithFullData: IAuthority = {
  name: 'bf17911c-f378-4a84-9877-5d6678227e0b',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
