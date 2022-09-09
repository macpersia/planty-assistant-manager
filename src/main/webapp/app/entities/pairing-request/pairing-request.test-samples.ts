import dayjs from 'dayjs/esm';

import { IPairingRequest, NewPairingRequest } from './pairing-request.model';

export const sampleWithRequiredData: IPairingRequest = {
  id: 99143,
};

export const sampleWithPartialData: IPairingRequest = {
  id: 82119,
  name: 'yellow Sports Baby',
  requestTime: dayjs('2018-07-30T21:02'),
  accepted: false,
  publicKey: 'alarm',
};

export const sampleWithFullData: IPairingRequest = {
  id: 56205,
  name: 'Account turn-key',
  verificationCode: 'synthesizing',
  requestTime: dayjs('2018-07-31T08:00'),
  accepted: false,
  sessionId: 'array input optical',
  publicKey: 'primary',
  login: 'system Product analyzer',
};

export const sampleWithNewData: NewPairingRequest = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
