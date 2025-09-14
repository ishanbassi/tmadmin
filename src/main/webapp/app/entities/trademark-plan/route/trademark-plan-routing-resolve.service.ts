import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITrademarkPlan } from '../trademark-plan.model';
import { TrademarkPlanService } from '../service/trademark-plan.service';

const trademarkPlanResolve = (route: ActivatedRouteSnapshot): Observable<null | ITrademarkPlan> => {
  const id = route.params.id;
  if (id) {
    return inject(TrademarkPlanService)
      .find(id)
      .pipe(
        mergeMap((trademarkPlan: HttpResponse<ITrademarkPlan>) => {
          if (trademarkPlan.body) {
            return of(trademarkPlan.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default trademarkPlanResolve;
