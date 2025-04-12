import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILead } from '../lead.model';
import { LeadService } from '../service/lead.service';

const leadResolve = (route: ActivatedRouteSnapshot): Observable<null | ILead> => {
  const id = route.params['id'];
  if (id) {
    return inject(LeadService)
      .find(id)
      .pipe(
        mergeMap((lead: HttpResponse<ILead>) => {
          if (lead.body) {
            return of(lead.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default leadResolve;
