import dayjs from 'dayjs/esm';
import { ILead } from 'app/entities/lead/lead.model';
import { IUserProfile } from 'app/entities/user-profile/user-profile.model';

export interface IPayment {
  id: number;
  gateway?: string | null;
  gatewayPaymentId?: string | null;
  amount?: number | null;
  currency?: string | null;
  status?: string | null;
  paymentMethod?: string | null;
  createdDate?: dayjs.Dayjs | null;
  modifiedDate?: dayjs.Dayjs | null;
  deleted?: boolean | null;
  lead?: Pick<ILead, 'id'> | null;
  user?: Pick<IUserProfile, 'id'> | null;
}

export type NewPayment = Omit<IPayment, 'id'> & { id: null };
