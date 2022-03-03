/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PlantyAssistantManagerTestModule } from '../../../test.module';
import { PairingRequestComponent } from 'app/entities/pairing-request/pairing-request.component';
import { PairingRequestService } from 'app/entities/pairing-request/pairing-request.service';
import { PairingRequest } from 'app/shared/model/pairing-request.model';

describe('Component Tests', () => {
    describe('PairingRequest Management Component', () => {
        let comp: PairingRequestComponent;
        let fixture: ComponentFixture<PairingRequestComponent>;
        let service: PairingRequestService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PlantyAssistantManagerTestModule],
                declarations: [PairingRequestComponent],
                providers: []
            })
                .overrideTemplate(PairingRequestComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(PairingRequestComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PairingRequestService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new PairingRequest(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.pairingRequests[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
