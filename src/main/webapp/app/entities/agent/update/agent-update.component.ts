import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { AgentFormService, AgentFormGroup } from './agent-form.service';
import { IAgent } from '../agent.model';
import { AgentService } from '../service/agent.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'pam-agent-update',
  templateUrl: './agent-update.component.html',
})
export class AgentUpdateComponent implements OnInit {
  isSaving = false;
  agent: IAgent | null = null;

  usersSharedCollection: IUser[] = [];

  editForm: AgentFormGroup = this.agentFormService.createAgentFormGroup();

  constructor(
    protected agentService: AgentService,
    protected agentFormService: AgentFormService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ agent }) => {
      this.agent = agent;
      if (agent) {
        this.updateForm(agent);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const agent = this.agentFormService.getAgent(this.editForm);
    if (agent.id !== null) {
      this.subscribeToSaveResponse(this.agentService.update(agent));
    } else {
      this.subscribeToSaveResponse(this.agentService.create(agent));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAgent>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(agent: IAgent): void {
    this.agent = agent;
    this.agentFormService.resetForm(this.editForm, agent);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, agent.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.agent?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
