import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AgentComponent } from '../list/agent.component';
import { AgentDetailComponent } from '../detail/agent-detail.component';
import { AgentUpdateComponent } from '../update/agent-update.component';
import { AgentRoutingResolveService } from './agent-routing-resolve.service';

const agentRoute: Routes = [
  {
    path: '',
    component: AgentComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AgentDetailComponent,
    resolve: {
      agent: AgentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AgentUpdateComponent,
    resolve: {
      agent: AgentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AgentUpdateComponent,
    resolve: {
      agent: AgentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(agentRoute)],
  exports: [RouterModule],
})
export class AgentRoutingModule {}
