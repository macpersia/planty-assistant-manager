import dayjs from 'dayjs/esm';

export interface IPairingRequest {
  id: number;
  name?: string | null;
  verificationCode?: string | null;
  requestTime?: dayjs.Dayjs | null;
  accepted?: boolean | null;
  sessionId?: string | null;
  publicKey?: string | null;
  login?: string | null;
}

export type NewPairingRequest = Omit<IPairingRequest, 'id'> & { id: null };
