import dayjs from 'dayjs/esm';

import { IPayment, NewPayment } from './payment.model';

export const sampleWithRequiredData: IPayment = {
  id: 4942,
};

export const sampleWithPartialData: IPayment = {
  id: 26826,
  gatewayPaymentId: 'consequently',
  status: 'unique',
  deleted: true,
};

export const sampleWithFullData: IPayment = {
  id: 28239,
  gateway: 'hourly unwieldy',
  gatewayPaymentId: 'because',
  amount: 5500.46,
  currency: 'baa plastic likewise',
  status: 'connect',
  paymentMethod: 'after especially',
  createdDate: dayjs('2025-07-22T21:54'),
  modifiedDate: dayjs('2025-07-22T23:26'),
  deleted: true,
};

export const sampleWithNewData: NewPayment = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
