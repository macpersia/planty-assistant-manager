import { Route } from '@angular/router';

import { PamDocsComponent } from './docs.component';

export const docsRoute: Route = {
    path: 'docs',
    component: PamDocsComponent,
    data: {
        pageTitle: 'global.menu.admin.apidocs'
    }
};
