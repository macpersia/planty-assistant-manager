import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { AssistantManagerAgentModule } from './agent/agent.module';
import { AssistantManagerPairingRequestModule } from './pairing-request/pairing-request.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        AssistantManagerAgentModule,
        AssistantManagerPairingRequestModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AssistantManagerEntityModule {}
