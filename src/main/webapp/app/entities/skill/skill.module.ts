import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PlantyAssistantManagerSharedModule } from 'app/shared';
import { PlantyAssistantManagerAdminModule } from 'app/admin/admin.module';
import {
    SkillComponent,
    SkillDetailComponent,
    SkillUpdateComponent,
    SkillDeletePopupComponent,
    SkillDeleteDialogComponent,
    skillRoute,
    skillPopupRoute
} from './';

const ENTITY_STATES = [...skillRoute, ...skillPopupRoute];

@NgModule({
    imports: [PlantyAssistantManagerSharedModule, PlantyAssistantManagerAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [SkillComponent, SkillDetailComponent, SkillUpdateComponent, SkillDeleteDialogComponent, SkillDeletePopupComponent],
    entryComponents: [SkillComponent, SkillUpdateComponent, SkillDeleteDialogComponent, SkillDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PlantyAssistantManagerSkillModule {}