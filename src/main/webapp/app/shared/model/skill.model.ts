import { IUser } from 'app/core/user/user.model';

export interface ISkill {
    id?: number;
    name?: string;
    agentSharing?: boolean;
    users?: IUser[];
}

export class Skill implements ISkill {
    constructor(public id?: number, public name?: string, public agentSharing?: boolean, public users?: IUser[]) {
        this.agentSharing = this.agentSharing || false;
    }
}
