import './vendor.ts';

import { NgModule, Injector } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { Ng2Webstorage, LocalStorageService, SessionStorageService } from 'ngx-webstorage';
import { JhiEventManager } from 'ng-jhipster';

import { AuthInterceptor } from './blocks/interceptor/auth.interceptor';
import { AuthExpiredInterceptor } from './blocks/interceptor/auth-expired.interceptor';
import { ErrorHandlerInterceptor } from './blocks/interceptor/errorhandler.interceptor';
import { NotificationInterceptor } from './blocks/interceptor/notification.interceptor';
import { PlantyAssistantManagerSharedModule } from 'app/shared';
import { PlantyAssistantManagerCoreModule } from 'app/core';
import { PlantyAssistantManagerAppRoutingModule } from './app-routing.module';
import { PlantyAssistantManagerHomeModule } from './home/home.module';
import { PlantyAssistantManagerAccountModule } from './account/account.module';
import { PlantyAssistantManagerEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { PamMainComponent, NavbarComponent, FooterComponent, PageRibbonComponent, ActiveMenuDirective, ErrorComponent } from './layouts';

@NgModule({
    imports: [
        BrowserModule,
        PlantyAssistantManagerAppRoutingModule,
        Ng2Webstorage.forRoot({ prefix: 'pam', separator: '-' }),
        PlantyAssistantManagerSharedModule,
        PlantyAssistantManagerCoreModule,
        PlantyAssistantManagerHomeModule,
        PlantyAssistantManagerAccountModule,
        PlantyAssistantManagerEntityModule
        // jhipster-needle-angular-add-module JHipster will add new module here
    ],
    declarations: [PamMainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, ActiveMenuDirective, FooterComponent],
    providers: [
        {
            provide: HTTP_INTERCEPTORS,
            useClass: AuthInterceptor,
            multi: true,
            deps: [LocalStorageService, SessionStorageService]
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: AuthExpiredInterceptor,
            multi: true,
            deps: [Injector]
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: ErrorHandlerInterceptor,
            multi: true,
            deps: [JhiEventManager]
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: NotificationInterceptor,
            multi: true,
            deps: [Injector]
        }
    ],
    bootstrap: [PamMainComponent]
})
export class PlantyAssistantManagerAppModule {}
