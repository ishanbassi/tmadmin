import dayjs from 'dayjs/esm';
import { ITrademark } from 'app/entities/trademark/trademark.model';
import { IUserProfile } from 'app/entities/user-profile/user-profile.model';
import { PaymentPurpose } from 'app/entities/enumerations/payment-purpose.model';

export interface IPayment {
  id: number;
  gateway?: string | null;
  gatewayPaymentId?: string | null;
  amount?: number | null;
  currency?: string | null;
  status?: string | null;
  paymentMethod?: string | null;
  createdDate?: dayjs.Dayjs | null;
  deleted?: boolean | null;
  modifiedDate?: dayjs.Dayjs | null;
  orderId?: string | null;
  gatewayOrderId?: string | null;
  failureReason?: string | null;
  purpose?: keyof typeof PaymentPurpose | null;
  trademark?: Pick<ITrademark, 'id'> | null;
  userProfile?: Pick<IUserProfile, 'id'> | null;
}

export type NewPayment = Omit<IPayment, 'id'> & { id: null };
