import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPairingRequest, NewPairingRequest } from '../pairing-request.model';

export type PartialUpdatePairingRequest = Partial<IPairingRequest> & Pick<IPairingRequest, 'id'>;

type RestOf<T extends IPairingRequest | NewPairingRequest> = Omit<T, 'requestTime'> & {
  requestTime?: string | null;
};

export type RestPairingRequest = RestOf<IPairingRequest>;

export type NewRestPairingRequest = RestOf<NewPairingRequest>;

export type PartialUpdateRestPairingRequest = RestOf<PartialUpdatePairingRequest>;

export type EntityResponseType = HttpResponse<IPairingRequest>;
export type EntityArrayResponseType = HttpResponse<IPairingRequest[]>;

@Injectable({ providedIn: 'root' })
export class PairingRequestService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/pairing-requests');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(pairingRequest: NewPairingRequest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pairingRequest);
    return this.http
      .post<RestPairingRequest>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(pairingRequest: IPairingRequest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pairingRequest);
    return this.http
      .put<RestPairingRequest>(`${this.resourceUrl}/${this.getPairingRequestIdentifier(pairingRequest)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(pairingRequest: PartialUpdatePairingRequest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pairingRequest);
    return this.http
      .patch<RestPairingRequest>(`${this.resourceUrl}/${this.getPairingRequestIdentifier(pairingRequest)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestPairingRequest>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPairingRequest[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPairingRequestIdentifier(pairingRequest: Pick<IPairingRequest, 'id'>): number {
    return pairingRequest.id;
  }

  comparePairingRequest(o1: Pick<IPairingRequest, 'id'> | null, o2: Pick<IPairingRequest, 'id'> | null): boolean {
    return o1 && o2 ? this.getPairingRequestIdentifier(o1) === this.getPairingRequestIdentifier(o2) : o1 === o2;
  }

  addPairingRequestToCollectionIfMissing<Type extends Pick<IPairingRequest, 'id'>>(
    pairingRequestCollection: Type[],
    ...pairingRequestsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const pairingRequests: Type[] = pairingRequestsToCheck.filter(isPresent);
    if (pairingRequests.length > 0) {
      const pairingRequestCollectionIdentifiers = pairingRequestCollection.map(
        pairingRequestItem => this.getPairingRequestIdentifier(pairingRequestItem)!
      );
      const pairingRequestsToAdd = pairingRequests.filter(pairingRequestItem => {
        const pairingRequestIdentifier = this.getPairingRequestIdentifier(pairingRequestItem);
        if (pairingRequestCollectionIdentifiers.includes(pairingRequestIdentifier)) {
          return false;
        }
        pairingRequestCollectionIdentifiers.push(pairingRequestIdentifier);
        return true;
      });
      return [...pairingRequestsToAdd, ...pairingRequestCollection];
    }
    return pairingRequestCollection;
  }

  protected convertDateFromClient<T extends IPairingRequest | NewPairingRequest | PartialUpdatePairingRequest>(
    pairingRequest: T
  ): RestOf<T> {
    return {
      ...pairingRequest,
      requestTime: pairingRequest.requestTime?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restPairingRequest: RestPairingRequest): IPairingRequest {
    return {
      ...restPairingRequest,
      requestTime: restPairingRequest.requestTime ? dayjs(restPairingRequest.requestTime) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestPairingRequest>): HttpResponse<IPairingRequest> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestPairingRequest[]>): HttpResponse<IPairingRequest[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
