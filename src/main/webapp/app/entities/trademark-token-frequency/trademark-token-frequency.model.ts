import dayjs from 'dayjs/esm';

export interface ITrademarkTokenFrequency {
  id: number;
  frequency?: number | null;
  word?: string | null;
  createdDate?: dayjs.Dayjs | null;
  deleted?: boolean | null;
  modifiedDate?: dayjs.Dayjs | null;
}

export type NewTrademarkTokenFrequency = Omit<ITrademarkTokenFrequency, 'id'> & { id: null };
