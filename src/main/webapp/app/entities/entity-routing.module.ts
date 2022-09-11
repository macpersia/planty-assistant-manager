import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'agent',
        data: { pageTitle: 'plantyAssistantManagerApp.agent.home.title' },
        loadChildren: () => import('./agent/agent.module').then(m => m.AgentModule),
      },
      {
        path: 'pairing-request',
        data: { pageTitle: 'plantyAssistantManagerApp.pairingRequest.home.title' },
        loadChildren: () => import('./pairing-request/pairing-request.module').then(m => m.PairingRequestModule),
      },
      {
        path: 'skill',
        data: { pageTitle: 'plantyAssistantManagerApp.skill.home.title' },
        loadChildren: () => import('./skill/skill.module').then(m => m.SkillModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
