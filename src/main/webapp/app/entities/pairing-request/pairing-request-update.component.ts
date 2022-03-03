import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { IPairingRequest } from 'app/shared/model/pairing-request.model';
import { PairingRequestService } from './pairing-request.service';

@Component({
    selector: 'pam-pairing-request-update',
    templateUrl: './pairing-request-update.component.html'
})
export class PairingRequestUpdateComponent implements OnInit {
    pairingRequest: IPairingRequest;
    isSaving: boolean;
    requestTime: string;

    constructor(protected pairingRequestService: PairingRequestService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ pairingRequest }) => {
            this.pairingRequest = pairingRequest;
            this.requestTime = this.pairingRequest.requestTime != null ? this.pairingRequest.requestTime.format(DATE_TIME_FORMAT) : null;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.pairingRequest.requestTime = this.requestTime != null ? moment(this.requestTime, DATE_TIME_FORMAT) : null;
        if (this.pairingRequest.id !== undefined) {
            this.subscribeToSaveResponse(this.pairingRequestService.update(this.pairingRequest));
        } else {
            this.subscribeToSaveResponse(this.pairingRequestService.create(this.pairingRequest));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IPairingRequest>>) {
        result.subscribe((res: HttpResponse<IPairingRequest>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
