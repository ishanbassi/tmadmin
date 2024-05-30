import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPhonetics } from '../phonetics.model';
import { PhoneticsService } from '../service/phonetics.service';

const phoneticsResolve = (route: ActivatedRouteSnapshot): Observable<null | IPhonetics> => {
  const id = route.params['id'];
  if (id) {
    return inject(PhoneticsService)
      .find(id)
      .pipe(
        mergeMap((phonetics: HttpResponse<IPhonetics>) => {
          if (phonetics.body) {
            return of(phonetics.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default phoneticsResolve;
