import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUserEventsTracking } from '../user-events-tracking.model';
import { UserEventsTrackingService } from '../service/user-events-tracking.service';

const userEventsTrackingResolve = (route: ActivatedRouteSnapshot): Observable<null | IUserEventsTracking> => {
  const id = route.params.id;
  if (id) {
    return inject(UserEventsTrackingService)
      .find(id)
      .pipe(
        mergeMap((userEventsTracking: HttpResponse<IUserEventsTracking>) => {
          if (userEventsTracking.body) {
            return of(userEventsTracking.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default userEventsTrackingResolve;
