import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PairingRequestFormService } from './pairing-request-form.service';
import { PairingRequestService } from '../service/pairing-request.service';
import { IPairingRequest } from '../pairing-request.model';

import { PairingRequestUpdateComponent } from './pairing-request-update.component';

describe('PairingRequest Management Update Component', () => {
  let comp: PairingRequestUpdateComponent;
  let fixture: ComponentFixture<PairingRequestUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let pairingRequestFormService: PairingRequestFormService;
  let pairingRequestService: PairingRequestService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PairingRequestUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(PairingRequestUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PairingRequestUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    pairingRequestFormService = TestBed.inject(PairingRequestFormService);
    pairingRequestService = TestBed.inject(PairingRequestService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const pairingRequest: IPairingRequest = { id: 456 };

      activatedRoute.data = of({ pairingRequest });
      comp.ngOnInit();

      expect(comp.pairingRequest).toEqual(pairingRequest);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPairingRequest>>();
      const pairingRequest = { id: 123 };
      jest.spyOn(pairingRequestFormService, 'getPairingRequest').mockReturnValue(pairingRequest);
      jest.spyOn(pairingRequestService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pairingRequest });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pairingRequest }));
      saveSubject.complete();

      // THEN
      expect(pairingRequestFormService.getPairingRequest).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(pairingRequestService.update).toHaveBeenCalledWith(expect.objectContaining(pairingRequest));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPairingRequest>>();
      const pairingRequest = { id: 123 };
      jest.spyOn(pairingRequestFormService, 'getPairingRequest').mockReturnValue({ id: null });
      jest.spyOn(pairingRequestService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pairingRequest: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pairingRequest }));
      saveSubject.complete();

      // THEN
      expect(pairingRequestFormService.getPairingRequest).toHaveBeenCalled();
      expect(pairingRequestService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPairingRequest>>();
      const pairingRequest = { id: 123 };
      jest.spyOn(pairingRequestService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pairingRequest });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(pairingRequestService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
