import dayjs from 'dayjs/esm';

import { IUserEventsTracking, NewUserEventsTracking } from './user-events-tracking.model';

export const sampleWithRequiredData: IUserEventsTracking = {
  id: 5270,
};

export const sampleWithPartialData: IUserEventsTracking = {
  id: 5709,
  createdDate: dayjs('2026-01-11T20:28'),
  modifiedDate: dayjs('2026-01-12T00:27'),
};

export const sampleWithFullData: IUserEventsTracking = {
  id: 9346,
  eventType: 'afore gently whoever',
  pageName: 'fiercely crossly',
  deviceType: 'unless duh',
  createdDate: dayjs('2026-01-11T11:25'),
  deleted: true,
  modifiedDate: dayjs('2026-01-12T04:29'),
};

export const sampleWithNewData: NewUserEventsTracking = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
