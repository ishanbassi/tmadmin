import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 23428,
  login: '7k@hu-\\&J\\WO\\<oaJCIw\\,NlX3N2',
};

export const sampleWithPartialData: IUser = {
  id: 27186,
  login: 'Eym@L5uS',
};

export const sampleWithFullData: IUser = {
  id: 5070,
  login: '17jRY',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
