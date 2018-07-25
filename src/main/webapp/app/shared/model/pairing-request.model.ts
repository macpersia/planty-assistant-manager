import { Moment } from 'moment';

export interface IPairingRequest {
    id?: string;
    name?: string;
    verificationCode?: string;
    requestTime?: Moment;
    accepted?: boolean;
    publicKey?: string;
}

export class PairingRequest implements IPairingRequest {
    constructor(
        public id?: string,
        public name?: string,
        public verificationCode?: string,
        public requestTime?: Moment,
        public accepted?: boolean,
        public publicKey?: string
    ) {
        this.accepted = false;
    }
}
