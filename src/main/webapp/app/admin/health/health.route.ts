import { Route } from '@angular/router';

import { PamHealthCheckComponent } from './health.component';

export const healthRoute: Route = {
    path: 'pam-health',
    component: PamHealthCheckComponent,
    data: {
        pageTitle: 'health.title'
    }
};
