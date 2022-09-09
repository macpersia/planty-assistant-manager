import { IUser } from 'app/entities/user/user.model';

export interface ISkill {
  id: number;
  name?: string | null;
  agentSharing?: boolean | null;
  users?: Pick<IUser, 'id' | 'login'>[] | null;
}

export type NewSkill = Omit<ISkill, 'id'> & { id: null };
