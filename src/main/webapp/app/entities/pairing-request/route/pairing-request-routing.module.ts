import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PairingRequestComponent } from '../list/pairing-request.component';
import { PairingRequestDetailComponent } from '../detail/pairing-request-detail.component';
import { PairingRequestUpdateComponent } from '../update/pairing-request-update.component';
import { PairingRequestRoutingResolveService } from './pairing-request-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const pairingRequestRoute: Routes = [
  {
    path: '',
    component: PairingRequestComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PairingRequestDetailComponent,
    resolve: {
      pairingRequest: PairingRequestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PairingRequestUpdateComponent,
    resolve: {
      pairingRequest: PairingRequestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PairingRequestUpdateComponent,
    resolve: {
      pairingRequest: PairingRequestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(pairingRequestRoute)],
  exports: [RouterModule],
})
export class PairingRequestRoutingModule {}
