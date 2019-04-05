import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { of, throwError } from 'rxjs';

import { PlantyAssistantManagerTestModule } from '../../../test.module';
import { PamMetricsMonitoringComponent } from 'app/admin/metrics/metrics.component';
import { PamMetricsService } from 'app/admin/metrics/metrics.service';

describe('Component Tests', () => {
    describe('PamMetricsMonitoringComponent', () => {
        let comp: PamMetricsMonitoringComponent;
        let fixture: ComponentFixture<PamMetricsMonitoringComponent>;
        let service: PamMetricsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PlantyAssistantManagerTestModule],
                declarations: [PamMetricsMonitoringComponent]
            })
                .overrideTemplate(PamMetricsMonitoringComponent, '')
                .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PamMetricsMonitoringComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PamMetricsService);
        });

        describe('refresh', () => {
            it('should call refresh on init', () => {
                // GIVEN
                const response = {
                    timers: {
                        service: 'test',
                        unrelatedKey: 'test'
                    },
                    gauges: {
                        'jcache.statistics': {
                            value: 2
                        },
                        unrelatedKey: 'test'
                    }
                };
                spyOn(service, 'getMetrics').and.returnValue(of(response));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.getMetrics).toHaveBeenCalled();
            });
        });
    });
});
