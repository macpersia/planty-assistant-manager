import { IUser } from 'app/entities/user/user.model';

export interface IAgent {
  id?: number;
  name?: string | null;
  publicKey?: string | null;
  sessionId?: string | null;
  user?: IUser | null;
}

export class Agent implements IAgent {
  constructor(
    public id?: number,
    public name?: string | null,
    public publicKey?: string | null,
    public sessionId?: string | null,
    public user?: IUser | null
  ) {}
}

export function getAgentIdentifier(agent: IAgent): number | undefined {
  return agent.id;
}
