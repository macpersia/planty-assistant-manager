import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IPairingRequest } from 'app/shared/model/pairing-request.model';

type EntityResponseType = HttpResponse<IPairingRequest>;
type EntityArrayResponseType = HttpResponse<IPairingRequest[]>;

@Injectable({ providedIn: 'root' })
export class PairingRequestService {
    private resourceUrl = SERVER_API_URL + 'api/pairing-requests';

    constructor(private http: HttpClient) {}

    create(pairingRequest: IPairingRequest): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(pairingRequest);
        return this.http
            .post<IPairingRequest>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(pairingRequest: IPairingRequest): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(pairingRequest);
        return this.http
            .put<IPairingRequest>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: string): Observable<EntityResponseType> {
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

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(pairingRequest: IPairingRequest): IPairingRequest {
        const copy: IPairingRequest = Object.assign({}, pairingRequest, {
            requestTime:
                pairingRequest.requestTime != null && pairingRequest.requestTime.isValid() ? pairingRequest.requestTime.toJSON() : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.requestTime = res.body.requestTime != null ? moment(res.body.requestTime) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((pairingRequest: IPairingRequest) => {
            pairingRequest.requestTime = pairingRequest.requestTime != null ? moment(pairingRequest.requestTime) : null;
        });
        return res;
    }
}
