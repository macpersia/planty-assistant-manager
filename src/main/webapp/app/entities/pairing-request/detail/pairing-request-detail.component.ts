import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPairingRequest } from '../pairing-request.model';

@Component({
  selector: 'pam-pairing-request-detail',
  templateUrl: './pairing-request-detail.component.html',
})
export class PairingRequestDetailComponent implements OnInit {
  pairingRequest: IPairingRequest | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pairingRequest }) => {
      this.pairingRequest = pairingRequest;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
