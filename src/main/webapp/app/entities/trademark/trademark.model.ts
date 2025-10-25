import dayjs from 'dayjs/esm';
import { ILead } from 'app/entities/lead/lead.model';
import { IUserProfile } from 'app/entities/user-profile/user-profile.model';
import { ITrademarkPlan } from 'app/entities/trademark-plan/trademark-plan.model';
import { ITrademarkClass } from 'app/entities/trademark-class/trademark-class.model';
import { HeadOffice } from 'app/entities/enumerations/head-office.model';
import { TrademarkStatus } from 'app/entities/enumerations/trademark-status.model';
import { TrademarkType } from 'app/entities/enumerations/trademark-type.model';
import { TrademarkSource } from 'app/entities/enumerations/trademark-source.model';

export interface ITrademark {
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
  createdDate?: dayjs.Dayjs | null;
  modifiedDate?: dayjs.Dayjs | null;
  renewalDate?: dayjs.Dayjs | null;
  type?: keyof typeof TrademarkType | null;
  pageNo?: number | null;
  source?: keyof typeof TrademarkSource | null;
  lead?: Pick<ILead, 'id'> | null;
  user?: Pick<IUserProfile, 'id'> | null;
  trademarkPlan?: Pick<ITrademarkPlan, 'id'> | null;
  trademarkClasses?: Pick<ITrademarkClass, 'id'>[] | null;
}

export type NewTrademark = Omit<ITrademark, 'id'> & { id: null };
