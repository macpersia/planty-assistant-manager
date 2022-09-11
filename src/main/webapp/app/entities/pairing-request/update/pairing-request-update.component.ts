import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { PairingRequestFormService, PairingRequestFormGroup } from './pairing-request-form.service';
import { IPairingRequest } from '../pairing-request.model';
import { PairingRequestService } from '../service/pairing-request.service';

@Component({
  selector: 'pam-pairing-request-update',
  templateUrl: './pairing-request-update.component.html',
})
export class PairingRequestUpdateComponent implements OnInit {
  isSaving = false;
  pairingRequest: IPairingRequest | null = null;

  editForm: PairingRequestFormGroup = this.pairingRequestFormService.createPairingRequestFormGroup();

  constructor(
    protected pairingRequestService: PairingRequestService,
    protected pairingRequestFormService: PairingRequestFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pairingRequest }) => {
      this.pairingRequest = pairingRequest;
      if (pairingRequest) {
        this.updateForm(pairingRequest);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pairingRequest = this.pairingRequestFormService.getPairingRequest(this.editForm);
    if (pairingRequest.id !== null) {
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
    this.pairingRequest = pairingRequest;
    this.pairingRequestFormService.resetForm(this.editForm, pairingRequest);
  }
}
