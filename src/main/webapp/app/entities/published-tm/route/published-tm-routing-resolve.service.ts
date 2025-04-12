import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPublishedTm } from '../published-tm.model';
import { PublishedTmService } from '../service/published-tm.service';

const publishedTmResolve = (route: ActivatedRouteSnapshot): Observable<null | IPublishedTm> => {
  const id = route.params.id;
  if (id) {
    return inject(PublishedTmService)
      .find(id)
      .pipe(
        mergeMap((publishedTm: HttpResponse<IPublishedTm>) => {
          if (publishedTm.body) {
            return of(publishedTm.body);
          } 
            inject(Router).navigate(['404']);
            return EMPTY;
          
        }),
      );
  }
  return of(null);
};

export default publishedTmResolve;
