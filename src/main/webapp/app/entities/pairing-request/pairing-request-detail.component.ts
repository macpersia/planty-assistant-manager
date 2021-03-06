import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPairingRequest } from 'app/shared/model/pairing-request.model';

@Component({
    selector: 'pam-pairing-request-detail',
    templateUrl: './pairing-request-detail.component.html'
})
export class PairingRequestDetailComponent implements OnInit {
    pairingRequest: IPairingRequest;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ pairingRequest }) => {
            this.pairingRequest = pairingRequest;
        });
    }

    previousState() {
        window.history.back();
    }
}
