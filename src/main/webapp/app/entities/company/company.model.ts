import dayjs from 'dayjs/esm';
import { IUserProfile } from 'app/entities/user-profile/user-profile.model';

export interface ICompany {
  id: number;
  type?: string | null;
  name?: string | null;
  cin?: string | null;
  gstin?: string | null;
  natureOfBusiness?: string | null;
  address?: string | null;
  state?: string | null;
  pincode?: string | null;
  city?: string | null;
  createdDate?: dayjs.Dayjs | null;
  modifiedDate?: dayjs.Dayjs | null;
  deleted?: boolean | null;
  user?: IUserProfile | null;
}

export type NewCompany = Omit<ICompany, 'id'> & { id: null };
