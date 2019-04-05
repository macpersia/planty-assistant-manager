import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { PlantyAssistantManagerSharedModule } from 'app/shared';
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
    imports: [PlantyAssistantManagerSharedModule, RouterModule.forChild(ENTITY_STATES)],
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
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PlantyAssistantManagerPairingRequestModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
