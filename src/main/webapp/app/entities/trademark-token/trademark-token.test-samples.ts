import { ITrademarkToken, NewTrademarkToken } from './trademark-token.model';

export const sampleWithRequiredData: ITrademarkToken = {
  id: 23231,
};

export const sampleWithPartialData: ITrademarkToken = {
  id: 2371,
  tokenText: 'amongst busily knowingly',
};

export const sampleWithFullData: ITrademarkToken = {
  id: 4325,
  tokenText: 'consequently',
  tokenType: 'DESCRIPTIVE',
  position: 27858,
};

export const sampleWithNewData: NewTrademarkToken = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
