import { IAgent, NewAgent } from './agent.model';

export const sampleWithRequiredData: IAgent = {
  id: 33038,
};

export const sampleWithPartialData: IAgent = {
  id: 63123,
  name: 'Dam Money',
};

export const sampleWithFullData: IAgent = {
  id: 91041,
  name: 'multi-byte',
  publicKey: 'Branch groupware index',
  sessionId: 'drive',
};

export const sampleWithNewData: NewAgent = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
