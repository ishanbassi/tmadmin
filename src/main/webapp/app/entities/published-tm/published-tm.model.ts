import dayjs from 'dayjs/esm';
import { HeadOffice } from 'app/entities/enumerations/head-office.model';
import { TrademarkStatus } from 'app/entities/enumerations/trademark-status.model';

export interface IPublishedTm {
  id: number;
  name?: string | null;
  details?: string | null;
  applicationNo?: number | null;
  applicationDate?: dayjs.Dayjs | null;
  agentName?: string | null;
  agentAddress?: string | null;
  proprietorName?: string | null;
  proprietorAddress?: string | null;
  headOffice?: keyof typeof HeadOffice | null;
  imgUrl?: string | null;
  tmClass?: number | null;
  journalNo?: number | null;
  deleted?: boolean | null;
  usage?: string | null;
  associatedTms?: string | null;
  trademarkStatus?: keyof typeof TrademarkStatus | null;
}

export type NewPublishedTm = Omit<IPublishedTm, 'id'> & { id: null };
