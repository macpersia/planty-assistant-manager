import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { NgbDateAdapter } from '@ng-bootstrap/ng-bootstrap';

import { NgbDateMomentAdapter } from './util/datepicker-adapter';
import {
    PlantyAssistantManagerSharedLibsModule,
    PlantyAssistantManagerSharedCommonModule,
    PamLoginModalComponent,
    HasAnyAuthorityDirective
} from './';

@NgModule({
    imports: [PlantyAssistantManagerSharedLibsModule, PlantyAssistantManagerSharedCommonModule],
    declarations: [PamLoginModalComponent, HasAnyAuthorityDirective],
    providers: [{ provide: NgbDateAdapter, useClass: NgbDateMomentAdapter }],
    entryComponents: [PamLoginModalComponent],
    exports: [PlantyAssistantManagerSharedCommonModule, PamLoginModalComponent, HasAnyAuthorityDirective],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PlantyAssistantManagerSharedModule {}
