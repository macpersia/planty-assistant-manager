import { IUser } from 'app/entities/user/user.model';

export interface ISkill {
  id?: number;
  name?: string | null;
  agentSharing?: boolean | null;
  users?: IUser[] | null;
}

export class Skill implements ISkill {
  constructor(public id?: number, public name?: string | null, public agentSharing?: boolean | null, public users?: IUser[] | null) {
    this.agentSharing = this.agentSharing ?? false;
  }
}

export function getSkillIdentifier(skill: ISkill): number | undefined {
  return skill.id;
}
