import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IAgent } from 'app/shared/model/agent.model';
import { Principal } from 'app/core';
import { AgentService } from './agent.service';

@Component({
    selector: 'jhi-agent',
    templateUrl: './agent.component.html'
})
export class AgentComponent implements OnInit, OnDestroy {
    agents: IAgent[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private agentService: AgentService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {}

    loadAll() {
        this.agentService.query().subscribe(
            (res: HttpResponse<IAgent[]>) => {
                this.agents = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInAgents();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IAgent) {
        return item.id;
    }

    registerChangeInAgents() {
        this.eventSubscriber = this.eventManager.subscribe('agentListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
