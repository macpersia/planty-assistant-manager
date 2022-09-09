import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPairingRequest } from '../pairing-request.model';
import { PairingRequestService } from '../service/pairing-request.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './pairing-request-delete-dialog.component.html',
})
export class PairingRequestDeleteDialogComponent {
  pairingRequest?: IPairingRequest;

  constructor(protected pairingRequestService: PairingRequestService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pairingRequestService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
