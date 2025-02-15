import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 23428,
  login: 'i@eJbtoJ\\,XN0y\\?5SjZ\\{9M',
};

export const sampleWithPartialData: IUser = {
  id: 12822,
  login: 't@m-n\\#Fwq5n\\MtK\\bAD-Hf\\sp7',
};

export const sampleWithFullData: IUser = {
  id: 19569,
  login: '39F',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
