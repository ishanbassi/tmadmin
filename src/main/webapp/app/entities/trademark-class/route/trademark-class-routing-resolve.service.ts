import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITrademarkClass } from '../trademark-class.model';
import { TrademarkClassService } from '../service/trademark-class.service';

const trademarkClassResolve = (route: ActivatedRouteSnapshot): Observable<null | ITrademarkClass> => {
  const id = route.params.id;
  if (id) {
    return inject(TrademarkClassService)
      .find(id)
      .pipe(
        mergeMap((trademarkClass: HttpResponse<ITrademarkClass>) => {
          if (trademarkClass.body) {
            return of(trademarkClass.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default trademarkClassResolve;
