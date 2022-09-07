import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IPairingRequest, PairingRequest } from '../pairing-request.model';
import { PairingRequestService } from '../service/pairing-request.service';

@Component({
  selector: 'pam-pairing-request-update',
  templateUrl: './pairing-request-update.component.html',
})
export class PairingRequestUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
    verificationCode: [],
    requestTime: [],
    accepted: [],
    sessionId: [],
    publicKey: [],
    login: [],
  });

  constructor(
    protected pairingRequestService: PairingRequestService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pairingRequest }) => {
      if (pairingRequest.id === undefined) {
        const today = dayjs().startOf('day');
        pairingRequest.requestTime = today;
      }

      this.updateForm(pairingRequest);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pairingRequest = this.createFromForm();
    if (pairingRequest.id !== undefined) {
      this.subscribeToSaveResponse(this.pairingRequestService.update(pairingRequest));
    } else {
      this.subscribeToSaveResponse(this.pairingRequestService.create(pairingRequest));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPairingRequest>>): void {
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

  protected updateForm(pairingRequest: IPairingRequest): void {
    this.editForm.patchValue({
      id: pairingRequest.id,
      name: pairingRequest.name,
      verificationCode: pairingRequest.verificationCode,
      requestTime: pairingRequest.requestTime ? pairingRequest.requestTime.format(DATE_TIME_FORMAT) : null,
      accepted: pairingRequest.accepted,
      sessionId: pairingRequest.sessionId,
      publicKey: pairingRequest.publicKey,
      login: pairingRequest.login,
    });
  }

  protected createFromForm(): IPairingRequest {
    return {
      ...new PairingRequest(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      verificationCode: this.editForm.get(['verificationCode'])!.value,
      requestTime: this.editForm.get(['requestTime'])!.value
        ? dayjs(this.editForm.get(['requestTime'])!.value, DATE_TIME_FORMAT)
        : undefined,
      accepted: this.editForm.get(['accepted'])!.value,
      sessionId: this.editForm.get(['sessionId'])!.value,
      publicKey: this.editForm.get(['publicKey'])!.value,
      login: this.editForm.get(['login'])!.value,
    };
  }
}
