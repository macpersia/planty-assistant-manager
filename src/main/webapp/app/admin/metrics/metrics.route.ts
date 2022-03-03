import { Route } from '@angular/router';

import { PamMetricsMonitoringComponent } from './metrics.component';

export const metricsRoute: Route = {
    path: 'pam-metrics',
    component: PamMetricsMonitoringComponent,
    data: {
        pageTitle: 'metrics.title'
    }
};
