import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITrademarkTokenFrequency } from '../trademark-token-frequency.model';
import { TrademarkTokenFrequencyService } from '../service/trademark-token-frequency.service';

const trademarkTokenFrequencyResolve = (route: ActivatedRouteSnapshot): Observable<null | ITrademarkTokenFrequency> => {
  const id = route.params.id;
  if (id) {
    return inject(TrademarkTokenFrequencyService)
      .find(id)
      .pipe(
        mergeMap((trademarkTokenFrequency: HttpResponse<ITrademarkTokenFrequency>) => {
          if (trademarkTokenFrequency.body) {
            return of(trademarkTokenFrequency.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default trademarkTokenFrequencyResolve;
