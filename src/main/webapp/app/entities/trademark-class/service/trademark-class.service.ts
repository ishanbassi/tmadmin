import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITrademarkClass, NewTrademarkClass } from '../trademark-class.model';

export type PartialUpdateTrademarkClass = Partial<ITrademarkClass> & Pick<ITrademarkClass, 'id'>;

type RestOf<T extends ITrademarkClass | NewTrademarkClass> = Omit<T, 'createdDate' | 'modifiedDate'> & {
  createdDate?: string | null;
  modifiedDate?: string | null;
};

export type RestTrademarkClass = RestOf<ITrademarkClass>;

export type NewRestTrademarkClass = RestOf<NewTrademarkClass>;

export type PartialUpdateRestTrademarkClass = RestOf<PartialUpdateTrademarkClass>;

export type EntityResponseType = HttpResponse<ITrademarkClass>;
export type EntityArrayResponseType = HttpResponse<ITrademarkClass[]>;

@Injectable({ providedIn: 'root' })
export class TrademarkClassService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/trademark-classes');

  create(trademarkClass: NewTrademarkClass): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(trademarkClass);
    return this.http
      .post<RestTrademarkClass>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(trademarkClass: ITrademarkClass): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(trademarkClass);
    return this.http
      .put<RestTrademarkClass>(`${this.resourceUrl}/${this.getTrademarkClassIdentifier(trademarkClass)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(trademarkClass: PartialUpdateTrademarkClass): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(trademarkClass);
    return this.http
      .patch<RestTrademarkClass>(`${this.resourceUrl}/${this.getTrademarkClassIdentifier(trademarkClass)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestTrademarkClass>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestTrademarkClass[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTrademarkClassIdentifier(trademarkClass: Pick<ITrademarkClass, 'id'>): number {
    return trademarkClass.id;
  }

  compareTrademarkClass(o1: Pick<ITrademarkClass, 'id'> | null, o2: Pick<ITrademarkClass, 'id'> | null): boolean {
    return o1 && o2 ? this.getTrademarkClassIdentifier(o1) === this.getTrademarkClassIdentifier(o2) : o1 === o2;
  }

  addTrademarkClassToCollectionIfMissing<Type extends Pick<ITrademarkClass, 'id'>>(
    trademarkClassCollection: Type[],
    ...trademarkClassesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const trademarkClasses: Type[] = trademarkClassesToCheck.filter(isPresent);
    if (trademarkClasses.length > 0) {
      const trademarkClassCollectionIdentifiers = trademarkClassCollection.map(trademarkClassItem =>
        this.getTrademarkClassIdentifier(trademarkClassItem),
      );
      const trademarkClassesToAdd = trademarkClasses.filter(trademarkClassItem => {
        const trademarkClassIdentifier = this.getTrademarkClassIdentifier(trademarkClassItem);
        if (trademarkClassCollectionIdentifiers.includes(trademarkClassIdentifier)) {
          return false;
        }
        trademarkClassCollectionIdentifiers.push(trademarkClassIdentifier);
        return true;
      });
      return [...trademarkClassesToAdd, ...trademarkClassCollection];
    }
    return trademarkClassCollection;
  }

  protected convertDateFromClient<T extends ITrademarkClass | NewTrademarkClass | PartialUpdateTrademarkClass>(
    trademarkClass: T,
  ): RestOf<T> {
    return {
      ...trademarkClass,
      createdDate: trademarkClass.createdDate?.toJSON() ?? null,
      modifiedDate: trademarkClass.modifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restTrademarkClass: RestTrademarkClass): ITrademarkClass {
    return {
      ...restTrademarkClass,
      createdDate: restTrademarkClass.createdDate ? dayjs(restTrademarkClass.createdDate) : undefined,
      modifiedDate: restTrademarkClass.modifiedDate ? dayjs(restTrademarkClass.modifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestTrademarkClass>): HttpResponse<ITrademarkClass> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestTrademarkClass[]>): HttpResponse<ITrademarkClass[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
