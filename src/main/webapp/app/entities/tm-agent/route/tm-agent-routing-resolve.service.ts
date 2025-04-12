import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITmAgent } from '../tm-agent.model';
import { TmAgentService } from '../service/tm-agent.service';

const tmAgentResolve = (route: ActivatedRouteSnapshot): Observable<null | ITmAgent> => {
  const id = route.params.id;
  if (id) {
    return inject(TmAgentService)
      .find(id)
      .pipe(
        mergeMap((tmAgent: HttpResponse<ITmAgent>) => {
          if (tmAgent.body) {
            return of(tmAgent.body);
          } 
            inject(Router).navigate(['404']);
            return EMPTY;
          
        }),
      );
  }
  return of(null);
};

export default tmAgentResolve;
