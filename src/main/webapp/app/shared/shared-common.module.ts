import { NgModule } from '@angular/core';

import { PlantyAssistantManagerSharedLibsModule, FindLanguageFromKeyPipe, PamAlertComponent, PamAlertErrorComponent } from './';

@NgModule({
    imports: [PlantyAssistantManagerSharedLibsModule],
    declarations: [FindLanguageFromKeyPipe, PamAlertComponent, PamAlertErrorComponent],
    exports: [PlantyAssistantManagerSharedLibsModule, FindLanguageFromKeyPipe, PamAlertComponent, PamAlertErrorComponent]
})
export class PlantyAssistantManagerSharedCommonModule {}
