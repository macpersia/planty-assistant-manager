import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IPairingRequest } from 'app/shared/model/pairing-request.model';
import { Principal } from 'app/core';
import { PairingRequestService } from './pairing-request.service';

@Component({
    selector: 'pam-pairing-request',
    templateUrl: './pairing-request.component.html'
})
export class PairingRequestComponent implements OnInit, OnDestroy {
    pairingRequests: IPairingRequest[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private pairingRequestService: PairingRequestService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {}

    loadAll() {
        this.pairingRequestService.query().subscribe(
            (res: HttpResponse<IPairingRequest[]>) => {
                this.pairingRequests = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInPairingRequests();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IPairingRequest) {
        return item.id;
    }

    registerChangeInPairingRequests() {
        this.eventSubscriber = this.eventManager.subscribe('pairingRequestListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
