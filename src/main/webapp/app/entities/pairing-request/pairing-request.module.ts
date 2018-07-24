import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AssistantManagerSharedModule } from 'app/shared';
import {
    PairingRequestComponent,
    PairingRequestDetailComponent,
    PairingRequestUpdateComponent,
    PairingRequestDeletePopupComponent,
    PairingRequestDeleteDialogComponent,
    pairingRequestRoute,
    pairingRequestPopupRoute
} from './';

const ENTITY_STATES = [...pairingRequestRoute, ...pairingRequestPopupRoute];

@NgModule({
    imports: [AssistantManagerSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        PairingRequestComponent,
        PairingRequestDetailComponent,
        PairingRequestUpdateComponent,
        PairingRequestDeleteDialogComponent,
        PairingRequestDeletePopupComponent
    ],
    entryComponents: [
        PairingRequestComponent,
        PairingRequestUpdateComponent,
        PairingRequestDeleteDialogComponent,
        PairingRequestDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AssistantManagerPairingRequestModule {}
