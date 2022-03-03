import { Moment } from 'moment';

export interface IPairingRequest {
    id?: number;
    name?: string;
    verificationCode?: string;
    requestTime?: Moment;
    accepted?: boolean;
    sessionId?: string;
    publicKey?: string;
    login?: string;
}

export class PairingRequest implements IPairingRequest {
    constructor(
        public id?: number,
        public name?: string,
        public verificationCode?: string,
        public requestTime?: Moment,
        public accepted?: boolean,
        public sessionId?: string,
        public publicKey?: string,
        public login?: string
    ) {
        this.accepted = this.accepted || false;
    }
}
