import dayjs from 'dayjs/esm';

import { ITrademarkPlan, NewTrademarkPlan } from './trademark-plan.model';

export const sampleWithRequiredData: ITrademarkPlan = {
  id: 7217,
};

export const sampleWithPartialData: ITrademarkPlan = {
  id: 16708,
  name: 'gah who',
  fees: 25712.4,
  notes: 'after because',
};

export const sampleWithFullData: ITrademarkPlan = {
  id: 31129,
  name: 'unaware circumference',
  fees: 6053.45,
  notes: 'accentuate yippee freezing',
  createdDate: dayjs('2025-09-14T02:32'),
  deleted: true,
  modifiedDate: dayjs('2025-09-13T18:13'),
};

export const sampleWithNewData: NewTrademarkPlan = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
