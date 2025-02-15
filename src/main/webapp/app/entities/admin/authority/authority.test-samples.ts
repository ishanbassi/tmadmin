import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: '6c566c4c-16eb-4656-91f7-68d934276cd6',
};

export const sampleWithPartialData: IAuthority = {
  name: '24741609-bab5-495b-9ba9-e436cb141297',
};

export const sampleWithFullData: IAuthority = {
  name: 'd3225a11-d8c8-480a-a039-57797c7d5a58',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
