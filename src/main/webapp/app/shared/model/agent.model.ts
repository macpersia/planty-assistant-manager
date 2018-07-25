export interface IAgent {
    id?: string;
    name?: string;
    publicKey?: string;
}

export class Agent implements IAgent {
    constructor(public id?: string, public name?: string, public publicKey?: string) {}
}
