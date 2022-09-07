import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IPairingRequest, PairingRequest } from '../pairing-request.model';
import { PairingRequestService } from '../service/pairing-request.service';

import { PairingRequestRoutingResolveService } from './pairing-request-routing-resolve.service';

describe('PairingRequest routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: PairingRequestRoutingResolveService;
  let service: PairingRequestService;
  let resultPairingRequest: IPairingRequest | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(PairingRequestRoutingResolveService);
    service = TestBed.inject(PairingRequestService);
    resultPairingRequest = undefined;
  });

  describe('resolve', () => {
    it('should return IPairingRequest returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPairingRequest = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPairingRequest).toEqual({ id: 123 });
    });

    it('should return new IPairingRequest if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPairingRequest = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultPairingRequest).toEqual(new PairingRequest());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as PairingRequest })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPairingRequest = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPairingRequest).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
