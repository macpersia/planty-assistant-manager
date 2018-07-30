export interface IAgent {
    id?: number;
    name?: string;
    publicKey?: string;
    sessionId?: string;
}

export class Agent implements IAgent {
    constructor(public id?: number, public name?: string, public publicKey?: string, public sessionId?: string) {}
}
