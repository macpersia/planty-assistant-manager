/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PlantyAssistantManagerTestModule } from '../../../test.module';
import { PairingRequestDetailComponent } from 'app/entities/pairing-request/pairing-request-detail.component';
import { PairingRequest } from 'app/shared/model/pairing-request.model';

describe('Component Tests', () => {
    describe('PairingRequest Management Detail Component', () => {
        let comp: PairingRequestDetailComponent;
        let fixture: ComponentFixture<PairingRequestDetailComponent>;
        const route = ({ data: of({ pairingRequest: new PairingRequest(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PlantyAssistantManagerTestModule],
                declarations: [PairingRequestDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(PairingRequestDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(PairingRequestDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.pairingRequest).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
