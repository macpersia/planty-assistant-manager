import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPairingRequest } from '../pairing-request.model';
import { PairingRequestService } from '../service/pairing-request.service';
import { PairingRequestDeleteDialogComponent } from '../delete/pairing-request-delete-dialog.component';

@Component({
  selector: 'pam-pairing-request',
  templateUrl: './pairing-request.component.html',
})
export class PairingRequestComponent implements OnInit {
  pairingRequests?: IPairingRequest[];
  isLoading = false;

  constructor(protected pairingRequestService: PairingRequestService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.pairingRequestService.query().subscribe({
      next: (res: HttpResponse<IPairingRequest[]>) => {
        this.isLoading = false;
        this.pairingRequests = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IPairingRequest): number {
    return item.id!;
  }

  delete(pairingRequest: IPairingRequest): void {
    const modalRef = this.modalService.open(PairingRequestDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.pairingRequest = pairingRequest;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
