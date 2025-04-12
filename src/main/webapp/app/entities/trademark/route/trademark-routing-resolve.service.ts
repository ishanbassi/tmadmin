import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITrademark } from '../trademark.model';
import { TrademarkService } from '../service/trademark.service';

const trademarkResolve = (route: ActivatedRouteSnapshot): Observable<null | ITrademark> => {
  const id = route.params.id;
  if (id) {
    return inject(TrademarkService)
      .find(id)
      .pipe(
        mergeMap((trademark: HttpResponse<ITrademark>) => {
          if (trademark.body) {
            return of(trademark.body);
          } 
            inject(Router).navigate(['404']);
            return EMPTY;
          
        }),
      );
  }
  return of(null);
};

export default trademarkResolve;
