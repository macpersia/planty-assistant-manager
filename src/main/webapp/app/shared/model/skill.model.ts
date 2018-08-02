import { IUser } from 'app/core/user/user.model';

export interface ISkill {
    id?: number;
    name?: string;
    users?: IUser[];
}

export class Skill implements ISkill {
    constructor(public id?: number, public name?: string, public users?: IUser[]) {}
}
