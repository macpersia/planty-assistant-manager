/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { AssistantManagerTestModule } from '../../../test.module';
import { PairingRequestUpdateComponent } from 'app/entities/pairing-request/pairing-request-update.component';
import { PairingRequestService } from 'app/entities/pairing-request/pairing-request.service';
import { PairingRequest } from 'app/shared/model/pairing-request.model';

describe('Component Tests', () => {
    describe('PairingRequest Management Update Component', () => {
        let comp: PairingRequestUpdateComponent;
        let fixture: ComponentFixture<PairingRequestUpdateComponent>;
        let service: PairingRequestService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [AssistantManagerTestModule],
                declarations: [PairingRequestUpdateComponent]
            })
                .overrideTemplate(PairingRequestUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(PairingRequestUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PairingRequestService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new PairingRequest('123');
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.pairingRequest = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new PairingRequest();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.pairingRequest = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
