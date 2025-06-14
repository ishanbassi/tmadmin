import dayjs from 'dayjs/esm';
import { ITrademark } from 'app/entities/trademark/trademark.model';
import { IUserProfile } from 'app/entities/user-profile/user-profile.model';

export interface IDocuments {
  id: number;
  documentType?: string | null;
  fileUrl?: string | null;
  createdDate?: dayjs.Dayjs | null;
  modifiedDate?: dayjs.Dayjs | null;
  deleted?: boolean | null;
  trademark?: ITrademark | null;
  user?: IUserProfile | null;
}

export type NewDocuments = Omit<IDocuments, 'id'> & { id: null };
