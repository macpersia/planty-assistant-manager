import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../pairing-request.test-samples';

import { PairingRequestFormService } from './pairing-request-form.service';

describe('PairingRequest Form Service', () => {
  let service: PairingRequestFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PairingRequestFormService);
  });

  describe('Service methods', () => {
    describe('createPairingRequestFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPairingRequestFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            verificationCode: expect.any(Object),
            requestTime: expect.any(Object),
            accepted: expect.any(Object),
            sessionId: expect.any(Object),
            publicKey: expect.any(Object),
            login: expect.any(Object),
          })
        );
      });

      it('passing IPairingRequest should create a new form with FormGroup', () => {
        const formGroup = service.createPairingRequestFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            verificationCode: expect.any(Object),
            requestTime: expect.any(Object),
            accepted: expect.any(Object),
            sessionId: expect.any(Object),
            publicKey: expect.any(Object),
            login: expect.any(Object),
          })
        );
      });
    });

    describe('getPairingRequest', () => {
      it('should return NewPairingRequest for default PairingRequest initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPairingRequestFormGroup(sampleWithNewData);

        const pairingRequest = service.getPairingRequest(formGroup) as any;

        expect(pairingRequest).toMatchObject(sampleWithNewData);
      });

      it('should return NewPairingRequest for empty PairingRequest initial value', () => {
        const formGroup = service.createPairingRequestFormGroup();

        const pairingRequest = service.getPairingRequest(formGroup) as any;

        expect(pairingRequest).toMatchObject({});
      });

      it('should return IPairingRequest', () => {
        const formGroup = service.createPairingRequestFormGroup(sampleWithRequiredData);

        const pairingRequest = service.getPairingRequest(formGroup) as any;

        expect(pairingRequest).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPairingRequest should not enable id FormControl', () => {
        const formGroup = service.createPairingRequestFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPairingRequest should disable id FormControl', () => {
        const formGroup = service.createPairingRequestFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
