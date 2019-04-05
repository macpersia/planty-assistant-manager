import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IAgent } from 'app/shared/model/agent.model';
import { AgentService } from './agent.service';
import { IUser, UserService } from 'app/core';

@Component({
    selector: 'pam-agent-update',
    templateUrl: './agent-update.component.html'
})
export class AgentUpdateComponent implements OnInit {
    private _agent: IAgent;
    isSaving: boolean;

    users: IUser[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private agentService: AgentService,
        private userService: UserService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ agent }) => {
            this.agent = agent;
        });
        this.userService.query().subscribe(
            (res: HttpResponse<IUser[]>) => {
                this.users = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.agent.id !== undefined) {
            this.subscribeToSaveResponse(this.agentService.update(this.agent));
        } else {
            this.subscribeToSaveResponse(this.agentService.create(this.agent));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IAgent>>) {
        result.subscribe((res: HttpResponse<IAgent>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }
    get agent() {
        return this._agent;
    }

    set agent(agent: IAgent) {
        this._agent = agent;
    }
}
