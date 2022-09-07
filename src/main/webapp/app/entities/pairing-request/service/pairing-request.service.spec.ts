import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPairingRequest, PairingRequest } from '../pairing-request.model';

import { PairingRequestService } from './pairing-request.service';

describe('PairingRequest Service', () => {
  let service: PairingRequestService;
  let httpMock: HttpTestingController;
  let elemDefault: IPairingRequest;
  let expectedResult: IPairingRequest | IPairingRequest[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PairingRequestService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      verificationCode: 'AAAAAAA',
      requestTime: currentDate,
      accepted: false,
      sessionId: 'AAAAAAA',
      publicKey: 'AAAAAAA',
      login: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          requestTime: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a PairingRequest', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          requestTime: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          requestTime: currentDate,
        },
        returnedFromService
      );

      service.create(new PairingRequest()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PairingRequest', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          verificationCode: 'BBBBBB',
          requestTime: currentDate.format(DATE_TIME_FORMAT),
          accepted: true,
          sessionId: 'BBBBBB',
          publicKey: 'BBBBBB',
          login: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          requestTime: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PairingRequest', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
          requestTime: currentDate.format(DATE_TIME_FORMAT),
          publicKey: 'BBBBBB',
        },
        new PairingRequest()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          requestTime: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PairingRequest', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          verificationCode: 'BBBBBB',
          requestTime: currentDate.format(DATE_TIME_FORMAT),
          accepted: true,
          sessionId: 'BBBBBB',
          publicKey: 'BBBBBB',
          login: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          requestTime: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a PairingRequest', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPairingRequestToCollectionIfMissing', () => {
      it('should add a PairingRequest to an empty array', () => {
        const pairingRequest: IPairingRequest = { id: 123 };
        expectedResult = service.addPairingRequestToCollectionIfMissing([], pairingRequest);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pairingRequest);
      });

      it('should not add a PairingRequest to an array that contains it', () => {
        const pairingRequest: IPairingRequest = { id: 123 };
        const pairingRequestCollection: IPairingRequest[] = [
          {
            ...pairingRequest,
          },
          { id: 456 },
        ];
        expectedResult = service.addPairingRequestToCollectionIfMissing(pairingRequestCollection, pairingRequest);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PairingRequest to an array that doesn't contain it", () => {
        const pairingRequest: IPairingRequest = { id: 123 };
        const pairingRequestCollection: IPairingRequest[] = [{ id: 456 }];
        expectedResult = service.addPairingRequestToCollectionIfMissing(pairingRequestCollection, pairingRequest);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pairingRequest);
      });

      it('should add only unique PairingRequest to an array', () => {
        const pairingRequestArray: IPairingRequest[] = [{ id: 123 }, { id: 456 }, { id: 43631 }];
        const pairingRequestCollection: IPairingRequest[] = [{ id: 123 }];
        expectedResult = service.addPairingRequestToCollectionIfMissing(pairingRequestCollection, ...pairingRequestArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const pairingRequest: IPairingRequest = { id: 123 };
        const pairingRequest2: IPairingRequest = { id: 456 };
        expectedResult = service.addPairingRequestToCollectionIfMissing([], pairingRequest, pairingRequest2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pairingRequest);
        expect(expectedResult).toContain(pairingRequest2);
      });

      it('should accept null and undefined values', () => {
        const pairingRequest: IPairingRequest = { id: 123 };
        expectedResult = service.addPairingRequestToCollectionIfMissing([], null, pairingRequest, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pairingRequest);
      });

      it('should return initial array if no PairingRequest is added', () => {
        const pairingRequestCollection: IPairingRequest[] = [{ id: 123 }];
        expectedResult = service.addPairingRequestToCollectionIfMissing(pairingRequestCollection, undefined, null);
        expect(expectedResult).toEqual(pairingRequestCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
