import dayjs from 'dayjs/esm';
import { ITrademark } from 'app/entities/trademark/trademark.model';
import { DocumentType } from 'app/entities/enumerations/document-type.model';
import { DocumentStatus } from 'app/entities/enumerations/document-status.model';

export interface IDocuments {
  id: number;
  documentType?: keyof typeof DocumentType | null;
  fileContentType?: string | null;
  fileName?: string | null;
  fileUrl?: string | null;
  createdDate?: dayjs.Dayjs | null;
  modifiedDate?: dayjs.Dayjs | null;
  deleted?: boolean | null;
  status?: keyof typeof DocumentStatus | null;
  trademark?: Pick<ITrademark, 'id'> | null;
}

export type NewDocuments = Omit<IDocuments, 'id'> & { id: null };
