import { ITmAgent, NewTmAgent } from './tm-agent.model';

export const sampleWithRequiredData: ITmAgent = {
  id: 31842,
};

export const sampleWithPartialData: ITmAgent = {
  id: 31571,
};

export const sampleWithFullData: ITmAgent = {
  id: 2491,
  agentCode: 10386,
  firstName: 'Lilian',
  lastName: 'Dach',
  address: 'hmph',
};

export const sampleWithNewData: NewTmAgent = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
