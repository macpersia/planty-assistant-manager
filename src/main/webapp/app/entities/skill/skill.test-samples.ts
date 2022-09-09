import { ISkill, NewSkill } from './skill.model';

export const sampleWithRequiredData: ISkill = {
  id: 54014,
};

export const sampleWithPartialData: ISkill = {
  id: 68570,
  name: 'Montana',
  agentSharing: true,
};

export const sampleWithFullData: ISkill = {
  id: 94743,
  name: 'Street experiences Account',
  agentSharing: false,
};

export const sampleWithNewData: NewSkill = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
