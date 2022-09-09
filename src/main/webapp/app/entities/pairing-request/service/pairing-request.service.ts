import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPairingRequest, getPairingRequestIdentifier } from '../pairing-request.model';

export type EntityResponseType = HttpResponse<IPairingRequest>;
export type EntityArrayResponseType = HttpResponse<IPairingRequest[]>;

@Injectable({ providedIn: 'root' })
export class PairingRequestService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/pairing-requests');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(pairingRequest: IPairingRequest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pairingRequest);
    return this.http
      .post<IPairingRequest>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(pairingRequest: IPairingRequest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pairingRequest);
    return this.http
      .put<IPairingRequest>(`${this.resourceUrl}/${getPairingRequestIdentifier(pairingRequest) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(pairingRequest: IPairingRequest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pairingRequest);
    return this.http
      .patch<IPairingRequest>(`${this.resourceUrl}/${getPairingRequestIdentifier(pairingRequest) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPairingRequest>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPairingRequest[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPairingRequestToCollectionIfMissing(
    pairingRequestCollection: IPairingRequest[],
    ...pairingRequestsToCheck: (IPairingRequest | null | undefined)[]
  ): IPairingRequest[] {
    const pairingRequests: IPairingRequest[] = pairingRequestsToCheck.filter(isPresent);
    if (pairingRequests.length > 0) {
      const pairingRequestCollectionIdentifiers = pairingRequestCollection.map(
        pairingRequestItem => getPairingRequestIdentifier(pairingRequestItem)!
      );
      const pairingRequestsToAdd = pairingRequests.filter(pairingRequestItem => {
        const pairingRequestIdentifier = getPairingRequestIdentifier(pairingRequestItem);
        if (pairingRequestIdentifier == null || pairingRequestCollectionIdentifiers.includes(pairingRequestIdentifier)) {
          return false;
        }
        pairingRequestCollectionIdentifiers.push(pairingRequestIdentifier);
        return true;
      });
      return [...pairingRequestsToAdd, ...pairingRequestCollection];
    }
    return pairingRequestCollection;
  }

  protected convertDateFromClient(pairingRequest: IPairingRequest): IPairingRequest {
    return Object.assign({}, pairingRequest, {
      requestTime: pairingRequest.requestTime?.isValid() ? pairingRequest.requestTime.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.requestTime = res.body.requestTime ? dayjs(res.body.requestTime) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((pairingRequest: IPairingRequest) => {
        pairingRequest.requestTime = pairingRequest.requestTime ? dayjs(pairingRequest.requestTime) : undefined;
      });
    }
    return res;
  }
}
