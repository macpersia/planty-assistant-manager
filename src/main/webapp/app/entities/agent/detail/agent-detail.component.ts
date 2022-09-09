import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAgent } from '../agent.model';

@Component({
  selector: 'pam-agent-detail',
  templateUrl: './agent-detail.component.html',
})
export class AgentDetailComponent implements OnInit {
  agent: IAgent | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ agent }) => {
      this.agent = agent;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
