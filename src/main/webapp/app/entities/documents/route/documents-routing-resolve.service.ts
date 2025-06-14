import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDocuments } from '../documents.model';
import { DocumentsService } from '../service/documents.service';

const documentsResolve = (route: ActivatedRouteSnapshot): Observable<null | IDocuments> => {
  const id = route.params.id;
  if (id) {
    return inject(DocumentsService)
      .find(id)
      .pipe(
        mergeMap((documents: HttpResponse<IDocuments>) => {
          if (documents.body) {
            return of(documents.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default documentsResolve;
