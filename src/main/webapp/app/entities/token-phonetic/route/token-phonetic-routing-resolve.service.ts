import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITokenPhonetic } from '../token-phonetic.model';
import { TokenPhoneticService } from '../service/token-phonetic.service';

const tokenPhoneticResolve = (route: ActivatedRouteSnapshot): Observable<null | ITokenPhonetic> => {
  const id = route.params.id;
  if (id) {
    return inject(TokenPhoneticService)
      .find(id)
      .pipe(
        mergeMap((tokenPhonetic: HttpResponse<ITokenPhonetic>) => {
          if (tokenPhonetic.body) {
            return of(tokenPhonetic.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default tokenPhoneticResolve;
