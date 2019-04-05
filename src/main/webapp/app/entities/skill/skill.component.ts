import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ISkill } from 'app/shared/model/skill.model';
import { Principal } from 'app/core';
import { SkillService } from './skill.service';

@Component({
    selector: 'pam-skill',
    templateUrl: './skill.component.html'
})
export class SkillComponent implements OnInit, OnDestroy {
    skills: ISkill[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private skillService: SkillService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {}

    loadAll() {
        this.skillService.query().subscribe(
            (res: HttpResponse<ISkill[]>) => {
                this.skills = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInSkills();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ISkill) {
        return item.id;
    }

    registerChangeInSkills() {
        this.eventSubscriber = this.eventManager.subscribe('skillListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
