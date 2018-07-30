import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAgent } from 'app/shared/model/agent.model';

@Component({
    selector: 'pam-agent-detail',
    templateUrl: './agent-detail.component.html'
})
export class AgentDetailComponent implements OnInit {
    agent: IAgent;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ agent }) => {
            this.agent = agent;
        });
    }

    previousState() {
        window.history.back();
    }
}
