import dayjs from 'dayjs/esm';
import { ITrademark } from 'app/entities/trademark/trademark.model';

export interface IDocuments {
  id: number;
  documentType?: string | null;
  fileUrl?: string | null;
  createdDate?: dayjs.Dayjs | null;
  modifiedDate?: dayjs.Dayjs | null;
  deleted?: boolean | null;
  trademark?: Pick<ITrademark, 'id'> | null;
}

export type NewDocuments = Omit<IDocuments, 'id'> & { id: null };
