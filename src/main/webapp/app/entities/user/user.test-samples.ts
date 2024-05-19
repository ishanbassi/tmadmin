import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 1387,
  login: 'MP40_c',
};

export const sampleWithPartialData: IUser = {
  id: 2529,
  login: 'We@d\\kMbMCg\\ugBxxX\\sj\\JqLL',
};

export const sampleWithFullData: IUser = {
  id: 11779,
  login: 'mQvxH_',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
