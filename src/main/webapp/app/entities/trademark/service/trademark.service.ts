import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITrademark, NewTrademark } from '../trademark.model';

export type PartialUpdateTrademark = Partial<ITrademark> & Pick<ITrademark, 'id'>;

type RestOf<T extends ITrademark | NewTrademark> = Omit<T, 'applicationDate' | 'createdDate' | 'modifiedDate'> & {
  applicationDate?: string | null;
  createdDate?: string | null;
  modifiedDate?: string | null;
};

export type RestTrademark = RestOf<ITrademark>;

export type NewRestTrademark = RestOf<NewTrademark>;

export type PartialUpdateRestTrademark = RestOf<PartialUpdateTrademark>;

export type EntityResponseType = HttpResponse<ITrademark>;
export type EntityArrayResponseType = HttpResponse<ITrademark[]>;

@Injectable({ providedIn: 'root' })
export class TrademarkService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/trademarks');

  create(trademark: NewTrademark): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(trademark);
    return this.http
      .post<RestTrademark>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(trademark: ITrademark): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(trademark);
    return this.http
      .put<RestTrademark>(`${this.resourceUrl}/${this.getTrademarkIdentifier(trademark)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(trademark: PartialUpdateTrademark): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(trademark);
    return this.http
      .patch<RestTrademark>(`${this.resourceUrl}/${this.getTrademarkIdentifier(trademark)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestTrademark>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestTrademark[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTrademarkIdentifier(trademark: Pick<ITrademark, 'id'>): number {
    return trademark.id;
  }

  compareTrademark(o1: Pick<ITrademark, 'id'> | null, o2: Pick<ITrademark, 'id'> | null): boolean {
    return o1 && o2 ? this.getTrademarkIdentifier(o1) === this.getTrademarkIdentifier(o2) : o1 === o2;
  }

  addTrademarkToCollectionIfMissing<Type extends Pick<ITrademark, 'id'>>(
    trademarkCollection: Type[],
    ...trademarksToCheck: (Type | null | undefined)[]
  ): Type[] {
    const trademarks: Type[] = trademarksToCheck.filter(isPresent);
    if (trademarks.length > 0) {
      const trademarkCollectionIdentifiers = trademarkCollection.map(trademarkItem => this.getTrademarkIdentifier(trademarkItem));
      const trademarksToAdd = trademarks.filter(trademarkItem => {
        const trademarkIdentifier = this.getTrademarkIdentifier(trademarkItem);
        if (trademarkCollectionIdentifiers.includes(trademarkIdentifier)) {
          return false;
        }
        trademarkCollectionIdentifiers.push(trademarkIdentifier);
        return true;
      });
      return [...trademarksToAdd, ...trademarkCollection];
    }
    return trademarkCollection;
  }

  protected convertDateFromClient<T extends ITrademark | NewTrademark | PartialUpdateTrademark>(trademark: T): RestOf<T> {
    return {
      ...trademark,
      applicationDate: trademark.applicationDate?.format(DATE_FORMAT) ?? null,
      createdDate: trademark.createdDate?.toJSON() ?? null,
      modifiedDate: trademark.modifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restTrademark: RestTrademark): ITrademark {
    return {
      ...restTrademark,
      applicationDate: restTrademark.applicationDate ? dayjs(restTrademark.applicationDate) : undefined,
      createdDate: restTrademark.createdDate ? dayjs(restTrademark.createdDate) : undefined,
      modifiedDate: restTrademark.modifiedDate ? dayjs(restTrademark.modifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestTrademark>): HttpResponse<ITrademark> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestTrademark[]>): HttpResponse<ITrademark[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
