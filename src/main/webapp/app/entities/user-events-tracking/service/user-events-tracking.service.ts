import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUserEventsTracking, NewUserEventsTracking } from '../user-events-tracking.model';

export type PartialUpdateUserEventsTracking = Partial<IUserEventsTracking> & Pick<IUserEventsTracking, 'id'>;

type RestOf<T extends IUserEventsTracking | NewUserEventsTracking> = Omit<T, 'createdDate' | 'modifiedDate'> & {
  createdDate?: string | null;
  modifiedDate?: string | null;
};

export type RestUserEventsTracking = RestOf<IUserEventsTracking>;

export type NewRestUserEventsTracking = RestOf<NewUserEventsTracking>;

export type PartialUpdateRestUserEventsTracking = RestOf<PartialUpdateUserEventsTracking>;

export type EntityResponseType = HttpResponse<IUserEventsTracking>;
export type EntityArrayResponseType = HttpResponse<IUserEventsTracking[]>;

@Injectable({ providedIn: 'root' })
export class UserEventsTrackingService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/user-events-trackings');

  create(userEventsTracking: NewUserEventsTracking): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userEventsTracking);
    return this.http
      .post<RestUserEventsTracking>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(userEventsTracking: IUserEventsTracking): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userEventsTracking);
    return this.http
      .put<RestUserEventsTracking>(`${this.resourceUrl}/${this.getUserEventsTrackingIdentifier(userEventsTracking)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(userEventsTracking: PartialUpdateUserEventsTracking): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userEventsTracking);
    return this.http
      .patch<RestUserEventsTracking>(`${this.resourceUrl}/${this.getUserEventsTrackingIdentifier(userEventsTracking)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestUserEventsTracking>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestUserEventsTracking[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getUserEventsTrackingIdentifier(userEventsTracking: Pick<IUserEventsTracking, 'id'>): number {
    return userEventsTracking.id;
  }

  compareUserEventsTracking(o1: Pick<IUserEventsTracking, 'id'> | null, o2: Pick<IUserEventsTracking, 'id'> | null): boolean {
    return o1 && o2 ? this.getUserEventsTrackingIdentifier(o1) === this.getUserEventsTrackingIdentifier(o2) : o1 === o2;
  }

  addUserEventsTrackingToCollectionIfMissing<Type extends Pick<IUserEventsTracking, 'id'>>(
    userEventsTrackingCollection: Type[],
    ...userEventsTrackingsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const userEventsTrackings: Type[] = userEventsTrackingsToCheck.filter(isPresent);
    if (userEventsTrackings.length > 0) {
      const userEventsTrackingCollectionIdentifiers = userEventsTrackingCollection.map(userEventsTrackingItem =>
        this.getUserEventsTrackingIdentifier(userEventsTrackingItem),
      );
      const userEventsTrackingsToAdd = userEventsTrackings.filter(userEventsTrackingItem => {
        const userEventsTrackingIdentifier = this.getUserEventsTrackingIdentifier(userEventsTrackingItem);
        if (userEventsTrackingCollectionIdentifiers.includes(userEventsTrackingIdentifier)) {
          return false;
        }
        userEventsTrackingCollectionIdentifiers.push(userEventsTrackingIdentifier);
        return true;
      });
      return [...userEventsTrackingsToAdd, ...userEventsTrackingCollection];
    }
    return userEventsTrackingCollection;
  }

  protected convertDateFromClient<T extends IUserEventsTracking | NewUserEventsTracking | PartialUpdateUserEventsTracking>(
    userEventsTracking: T,
  ): RestOf<T> {
    return {
      ...userEventsTracking,
      createdDate: userEventsTracking.createdDate?.toJSON() ?? null,
      modifiedDate: userEventsTracking.modifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restUserEventsTracking: RestUserEventsTracking): IUserEventsTracking {
    return {
      ...restUserEventsTracking,
      createdDate: restUserEventsTracking.createdDate ? dayjs(restUserEventsTracking.createdDate) : undefined,
      modifiedDate: restUserEventsTracking.modifiedDate ? dayjs(restUserEventsTracking.modifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestUserEventsTracking>): HttpResponse<IUserEventsTracking> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestUserEventsTracking[]>): HttpResponse<IUserEventsTracking[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
