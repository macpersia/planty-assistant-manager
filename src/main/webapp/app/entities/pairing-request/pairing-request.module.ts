import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PairingRequestComponent } from './list/pairing-request.component';
import { PairingRequestDetailComponent } from './detail/pairing-request-detail.component';
import { PairingRequestUpdateComponent } from './update/pairing-request-update.component';
import { PairingRequestDeleteDialogComponent } from './delete/pairing-request-delete-dialog.component';
import { PairingRequestRoutingModule } from './route/pairing-request-routing.module';

@NgModule({
  imports: [SharedModule, PairingRequestRoutingModule],
  declarations: [
    PairingRequestComponent,
    PairingRequestDetailComponent,
    PairingRequestUpdateComponent,
    PairingRequestDeleteDialogComponent,
  ],
})
export class PairingRequestModule {}
