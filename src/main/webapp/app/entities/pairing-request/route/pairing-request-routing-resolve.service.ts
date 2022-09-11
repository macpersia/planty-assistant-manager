import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPairingRequest } from '../pairing-request.model';
import { PairingRequestService } from '../service/pairing-request.service';

@Injectable({ providedIn: 'root' })
export class PairingRequestRoutingResolveService implements Resolve<IPairingRequest | null> {
  constructor(protected service: PairingRequestService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPairingRequest | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((pairingRequest: HttpResponse<IPairingRequest>) => {
          if (pairingRequest.body) {
            return of(pairingRequest.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
