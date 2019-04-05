import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISkill } from 'app/shared/model/skill.model';
import { SkillService } from './skill.service';

@Component({
    selector: 'pam-skill-delete-dialog',
    templateUrl: './skill-delete-dialog.component.html'
})
export class SkillDeleteDialogComponent {
    skill: ISkill;

    constructor(protected skillService: SkillService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.skillService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'skillListModification',
                content: 'Deleted an skill'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'pam-skill-delete-popup',
    template: ''
})
export class SkillDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ skill }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(SkillDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.skill = skill;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/skill', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/skill', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
