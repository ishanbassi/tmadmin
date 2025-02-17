import dayjs from 'dayjs/esm';
import { ITmAgent } from 'app/entities/tm-agent/tm-agent.model';
import { HeadOffice } from 'app/entities/enumerations/head-office.model';
import { TrademarkType } from 'app/entities/enumerations/trademark-type.model';

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
  trademarkStatus?: string | null;
  createdDate?: dayjs.Dayjs | null;
  modifiedDate?: dayjs.Dayjs | null;
  renewalDate?: dayjs.Dayjs | null;
  type?: keyof typeof TrademarkType | null;
  tmAgent?: ITmAgent | null;
}

export type NewPublishedTm = Omit<IPublishedTm, 'id'> & { id: null };
