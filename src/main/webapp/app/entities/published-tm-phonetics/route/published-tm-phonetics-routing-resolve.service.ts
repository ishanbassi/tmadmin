import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPublishedTmPhonetics } from '../published-tm-phonetics.model';
import { PublishedTmPhoneticsService } from '../service/published-tm-phonetics.service';

const publishedTmPhoneticsResolve = (route: ActivatedRouteSnapshot): Observable<null | IPublishedTmPhonetics> => {
  const id = route.params.id;
  if (id) {
    return inject(PublishedTmPhoneticsService)
      .find(id)
      .pipe(
        mergeMap((publishedTmPhonetics: HttpResponse<IPublishedTmPhonetics>) => {
          if (publishedTmPhonetics.body) {
            return of(publishedTmPhonetics.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default publishedTmPhoneticsResolve;
