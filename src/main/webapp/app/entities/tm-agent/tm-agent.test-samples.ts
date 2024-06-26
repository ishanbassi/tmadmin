import { ITmAgent, NewTmAgent } from './tm-agent.model';

export const sampleWithRequiredData: ITmAgent = {
  id: 27672,
};

export const sampleWithPartialData: ITmAgent = {
  id: 29891,
  agentCode: 14730,
  lastName: 'Ledner',
};

export const sampleWithFullData: ITmAgent = {
  id: 13992,
  agentCode: 8111,
  firstName: 'Jason',
  lastName: 'Stracke',
  address: 'trench virtuous',
};

export const sampleWithNewData: NewTmAgent = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
