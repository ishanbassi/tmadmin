import dayjs from 'dayjs/esm';
import { ITrademark } from 'app/entities/trademark/trademark.model';

export interface ITrademarkClass {
  id: number;
  code?: number | null;
  tmClass?: number | null;
  keyword?: string | null;
  title?: string | null;
  description?: string | null;
  createdDate?: dayjs.Dayjs | null;
  modifiedDate?: dayjs.Dayjs | null;
  deleted?: boolean | null;
  trademarks?: Pick<ITrademark, 'id'>[] | null;
}

export type NewTrademarkClass = Omit<ITrademarkClass, 'id'> & { id: null };
