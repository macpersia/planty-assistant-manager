export interface IAgent {
    id?: number;
    name?: string;
    publicKey?: string;
    sessionId?: string;
    userLogin?: string;
    userId?: number;
}

export class Agent implements IAgent {
    constructor(
        public id?: number,
        public name?: string,
        public publicKey?: string,
        public sessionId?: string,
        public userLogin?: string,
        public userId?: number
    ) {}
}
