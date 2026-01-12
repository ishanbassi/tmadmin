import dayjs from 'dayjs/esm';
import { IUserProfile } from 'app/entities/user-profile/user-profile.model';

export interface IUserEventsTracking {
  id: number;
  eventType?: string | null;
  pageName?: string | null;
  deviceType?: string | null;
  createdDate?: dayjs.Dayjs | null;
  deleted?: boolean | null;
  modifiedDate?: dayjs.Dayjs | null;
  userProfile?: Pick<IUserProfile, 'id'> | null;
}

export type NewUserEventsTracking = Omit<IUserEventsTracking, 'id'> & { id: null };
