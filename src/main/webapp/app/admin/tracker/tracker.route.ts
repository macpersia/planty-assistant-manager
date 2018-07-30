import { Route } from '@angular/router';

import { PamTrackerComponent } from './tracker.component';

export const trackerRoute: Route = {
    path: 'pam-tracker',
    component: PamTrackerComponent,
    data: {
        pageTitle: 'tracker.title'
    }
};
