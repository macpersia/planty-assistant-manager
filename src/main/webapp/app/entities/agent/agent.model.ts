import { IUser } from 'app/entities/user/user.model';

export interface IAgent {
  id: number;
  name?: string | null;
  publicKey?: string | null;
  sessionId?: string | null;
  user?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewAgent = Omit<IAgent, 'id'> & { id: null };
