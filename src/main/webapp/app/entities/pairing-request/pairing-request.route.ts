import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { PairingRequest } from 'app/shared/model/pairing-request.model';
import { PairingRequestService } from './pairing-request.service';
import { PairingRequestComponent } from './pairing-request.component';
import { PairingRequestDetailComponent } from './pairing-request-detail.component';
import { PairingRequestUpdateComponent } from './pairing-request-update.component';
import { PairingRequestDeletePopupComponent } from './pairing-request-delete-dialog.component';
import { IPairingRequest } from 'app/shared/model/pairing-request.model';

@Injectable({ providedIn: 'root' })
export class PairingRequestResolve implements Resolve<IPairingRequest> {
    constructor(private service: PairingRequestService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IPairingRequest> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<PairingRequest>) => response.ok),
                map((pairingRequest: HttpResponse<PairingRequest>) => pairingRequest.body)
            );
        }
        return of(new PairingRequest());
    }
}

export const pairingRequestRoute: Routes = [
    {
        path: '',
        component: PairingRequestComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'plantyAssistantManagerApp.pairingRequest.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: PairingRequestDetailComponent,
        resolve: {
            pairingRequest: PairingRequestResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'plantyAssistantManagerApp.pairingRequest.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: PairingRequestUpdateComponent,
        resolve: {
            pairingRequest: PairingRequestResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'plantyAssistantManagerApp.pairingRequest.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: PairingRequestUpdateComponent,
        resolve: {
            pairingRequest: PairingRequestResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'plantyAssistantManagerApp.pairingRequest.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const pairingRequestPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: PairingRequestDeletePopupComponent,
        resolve: {
            pairingRequest: PairingRequestResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'plantyAssistantManagerApp.pairingRequest.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
