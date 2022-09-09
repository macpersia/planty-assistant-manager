import dayjs from 'dayjs/esm';

export interface IPairingRequest {
  id?: number;
  name?: string | null;
  verificationCode?: string | null;
  requestTime?: dayjs.Dayjs | null;
  accepted?: boolean | null;
  sessionId?: string | null;
  publicKey?: string | null;
  login?: string | null;
}

export class PairingRequest implements IPairingRequest {
  constructor(
    public id?: number,
    public name?: string | null,
    public verificationCode?: string | null,
    public requestTime?: dayjs.Dayjs | null,
    public accepted?: boolean | null,
    public sessionId?: string | null,
    public publicKey?: string | null,
    public login?: string | null
  ) {
    this.accepted = this.accepted ?? false;
  }
}

export function getPairingRequestIdentifier(pairingRequest: IPairingRequest): number | undefined {
  return pairingRequest.id;
}
