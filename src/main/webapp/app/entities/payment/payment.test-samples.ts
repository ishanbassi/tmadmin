import dayjs from 'dayjs/esm';

import { IPayment, NewPayment } from './payment.model';

export const sampleWithRequiredData: IPayment = {
  id: 4942,
};

export const sampleWithPartialData: IPayment = {
  id: 22419,
  gatewayPaymentId: 'kiddingly metal redact',
  status: 'um',
  modifiedDate: dayjs('2025-07-23T09:17'),
  gatewayOrderId: 'without',
  failureReason: 'shaft',
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
  deleted: true,
  modifiedDate: dayjs('2025-07-22T16:53'),
  orderId: 'concentration knowledgeable',
  gatewayOrderId: 'er while ah',
  failureReason: 'fooey always unto',
};

export const sampleWithNewData: NewPayment = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
