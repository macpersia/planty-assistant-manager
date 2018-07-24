import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IPairingRequest } from 'app/shared/model/pairing-request.model';
import { PairingRequestService } from './pairing-request.service';

@Component({
    selector: 'jhi-pairing-request-update',
    templateUrl: './pairing-request-update.component.html'
})
export class PairingRequestUpdateComponent implements OnInit {
    private _pairingRequest: IPairingRequest;
    isSaving: boolean;
    requestTime: string;

    constructor(private pairingRequestService: PairingRequestService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ pairingRequest }) => {
            this.pairingRequest = pairingRequest;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.pairingRequest.requestTime = moment(this.requestTime, DATE_TIME_FORMAT);
        if (this.pairingRequest.id !== undefined) {
            this.subscribeToSaveResponse(this.pairingRequestService.update(this.pairingRequest));
        } else {
            this.subscribeToSaveResponse(this.pairingRequestService.create(this.pairingRequest));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IPairingRequest>>) {
        result.subscribe((res: HttpResponse<IPairingRequest>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get pairingRequest() {
        return this._pairingRequest;
    }

    set pairingRequest(pairingRequest: IPairingRequest) {
        this._pairingRequest = pairingRequest;
        this.requestTime = moment(pairingRequest.requestTime).format(DATE_TIME_FORMAT);
    }
}
