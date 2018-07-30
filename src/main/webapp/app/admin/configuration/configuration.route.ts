import { Route } from '@angular/router';

import { PamConfigurationComponent } from './configuration.component';

export const configurationRoute: Route = {
    path: 'pam-configuration',
    component: PamConfigurationComponent,
    data: {
        pageTitle: 'configuration.title'
    }
};
