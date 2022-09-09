import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPairingRequest } from '../pairing-request.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../pairing-request.test-samples';

import { PairingRequestService, RestPairingRequest } from './pairing-request.service';

const requireRestSample: RestPairingRequest = {
  ...sampleWithRequiredData,
  requestTime: sampleWithRequiredData.requestTime?.toJSON(),
};

describe('PairingRequest Service', () => {
  let service: PairingRequestService;
  let httpMock: HttpTestingController;
  let expectedResult: IPairingRequest | IPairingRequest[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PairingRequestService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a PairingRequest', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const pairingRequest = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(pairingRequest).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PairingRequest', () => {
      const pairingRequest = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(pairingRequest).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PairingRequest', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PairingRequest', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PairingRequest', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPairingRequestToCollectionIfMissing', () => {
      it('should add a PairingRequest to an empty array', () => {
        const pairingRequest: IPairingRequest = sampleWithRequiredData;
        expectedResult = service.addPairingRequestToCollectionIfMissing([], pairingRequest);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pairingRequest);
      });

      it('should not add a PairingRequest to an array that contains it', () => {
        const pairingRequest: IPairingRequest = sampleWithRequiredData;
        const pairingRequestCollection: IPairingRequest[] = [
          {
            ...pairingRequest,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPairingRequestToCollectionIfMissing(pairingRequestCollection, pairingRequest);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PairingRequest to an array that doesn't contain it", () => {
        const pairingRequest: IPairingRequest = sampleWithRequiredData;
        const pairingRequestCollection: IPairingRequest[] = [sampleWithPartialData];
        expectedResult = service.addPairingRequestToCollectionIfMissing(pairingRequestCollection, pairingRequest);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pairingRequest);
      });

      it('should add only unique PairingRequest to an array', () => {
        const pairingRequestArray: IPairingRequest[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const pairingRequestCollection: IPairingRequest[] = [sampleWithRequiredData];
        expectedResult = service.addPairingRequestToCollectionIfMissing(pairingRequestCollection, ...pairingRequestArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const pairingRequest: IPairingRequest = sampleWithRequiredData;
        const pairingRequest2: IPairingRequest = sampleWithPartialData;
        expectedResult = service.addPairingRequestToCollectionIfMissing([], pairingRequest, pairingRequest2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pairingRequest);
        expect(expectedResult).toContain(pairingRequest2);
      });

      it('should accept null and undefined values', () => {
        const pairingRequest: IPairingRequest = sampleWithRequiredData;
        expectedResult = service.addPairingRequestToCollectionIfMissing([], null, pairingRequest, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pairingRequest);
      });

      it('should return initial array if no PairingRequest is added', () => {
        const pairingRequestCollection: IPairingRequest[] = [sampleWithRequiredData];
        expectedResult = service.addPairingRequestToCollectionIfMissing(pairingRequestCollection, undefined, null);
        expect(expectedResult).toEqual(pairingRequestCollection);
      });
    });

    describe('comparePairingRequest', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePairingRequest(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePairingRequest(entity1, entity2);
        const compareResult2 = service.comparePairingRequest(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePairingRequest(entity1, entity2);
        const compareResult2 = service.comparePairingRequest(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePairingRequest(entity1, entity2);
        const compareResult2 = service.comparePairingRequest(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
