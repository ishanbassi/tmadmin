import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITrademarkToken } from '../trademark-token.model';
import { TrademarkTokenService } from '../service/trademark-token.service';

const trademarkTokenResolve = (route: ActivatedRouteSnapshot): Observable<null | ITrademarkToken> => {
  const id = route.params.id;
  if (id) {
    return inject(TrademarkTokenService)
      .find(id)
      .pipe(
        mergeMap((trademarkToken: HttpResponse<ITrademarkToken>) => {
          if (trademarkToken.body) {
            return of(trademarkToken.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default trademarkTokenResolve;
