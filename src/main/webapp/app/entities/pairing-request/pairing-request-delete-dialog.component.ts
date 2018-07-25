import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPairingRequest } from 'app/shared/model/pairing-request.model';
import { PairingRequestService } from './pairing-request.service';

@Component({
    selector: 'jhi-pairing-request-delete-dialog',
    templateUrl: './pairing-request-delete-dialog.component.html'
})
export class PairingRequestDeleteDialogComponent {
    pairingRequest: IPairingRequest;

    constructor(
        private pairingRequestService: PairingRequestService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.pairingRequestService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'pairingRequestListModification',
                content: 'Deleted an pairingRequest'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-pairing-request-delete-popup',
    template: ''
})
export class PairingRequestDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ pairingRequest }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(PairingRequestDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.pairingRequest = pairingRequest;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
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
