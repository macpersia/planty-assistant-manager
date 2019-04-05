import { SpyObject } from './spyobject';
import { PamTrackerService } from 'app/core/tracker/tracker.service';

export class MockTrackerService extends SpyObject {
    constructor() {
        super(PamTrackerService);
    }

    connect() {}
}
