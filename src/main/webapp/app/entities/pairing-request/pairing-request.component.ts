import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IPairingRequest } from 'app/shared/model/pairing-request.model';
import { AccountService } from 'app/core';
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
        protected pairingRequestService: PairingRequestService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService
    ) {}

    loadAll() {
        this.pairingRequestService
            .query()
            .pipe(
                filter((res: HttpResponse<IPairingRequest[]>) => res.ok),
                map((res: HttpResponse<IPairingRequest[]>) => res.body)
            )
            .subscribe(
                (res: IPairingRequest[]) => {
                    this.pairingRequests = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
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

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
