/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { AssistantManagerTestModule } from '../../../test.module';
import { PairingRequestDeleteDialogComponent } from 'app/entities/pairing-request/pairing-request-delete-dialog.component';
import { PairingRequestService } from 'app/entities/pairing-request/pairing-request.service';

describe('Component Tests', () => {
    describe('PairingRequest Management Delete Component', () => {
        let comp: PairingRequestDeleteDialogComponent;
        let fixture: ComponentFixture<PairingRequestDeleteDialogComponent>;
        let service: PairingRequestService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [AssistantManagerTestModule],
                declarations: [PairingRequestDeleteDialogComponent]
            })
                .overrideTemplate(PairingRequestDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(PairingRequestDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PairingRequestService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it(
                'Should call delete service on confirmDelete',
                inject(
                    [],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(of({}));

                        // WHEN
                        comp.confirmDelete('123');
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith('123');
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });
});
