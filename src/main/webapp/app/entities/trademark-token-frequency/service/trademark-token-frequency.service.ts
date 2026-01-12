import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITrademarkTokenFrequency, NewTrademarkTokenFrequency } from '../trademark-token-frequency.model';

export type PartialUpdateTrademarkTokenFrequency = Partial<ITrademarkTokenFrequency> & Pick<ITrademarkTokenFrequency, 'id'>;

type RestOf<T extends ITrademarkTokenFrequency | NewTrademarkTokenFrequency> = Omit<T, 'createdDate' | 'modifiedDate'> & {
  createdDate?: string | null;
  modifiedDate?: string | null;
};

export type RestTrademarkTokenFrequency = RestOf<ITrademarkTokenFrequency>;

export type NewRestTrademarkTokenFrequency = RestOf<NewTrademarkTokenFrequency>;

export type PartialUpdateRestTrademarkTokenFrequency = RestOf<PartialUpdateTrademarkTokenFrequency>;

export type EntityResponseType = HttpResponse<ITrademarkTokenFrequency>;
export type EntityArrayResponseType = HttpResponse<ITrademarkTokenFrequency[]>;

@Injectable({ providedIn: 'root' })
export class TrademarkTokenFrequencyService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/trademark-token-frequencies');

  create(trademarkTokenFrequency: NewTrademarkTokenFrequency): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(trademarkTokenFrequency);
    return this.http
      .post<RestTrademarkTokenFrequency>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(trademarkTokenFrequency: ITrademarkTokenFrequency): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(trademarkTokenFrequency);
    return this.http
      .put<RestTrademarkTokenFrequency>(`${this.resourceUrl}/${this.getTrademarkTokenFrequencyIdentifier(trademarkTokenFrequency)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(trademarkTokenFrequency: PartialUpdateTrademarkTokenFrequency): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(trademarkTokenFrequency);
    return this.http
      .patch<RestTrademarkTokenFrequency>(
        `${this.resourceUrl}/${this.getTrademarkTokenFrequencyIdentifier(trademarkTokenFrequency)}`,
        copy,
        { observe: 'response' },
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestTrademarkTokenFrequency>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestTrademarkTokenFrequency[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTrademarkTokenFrequencyIdentifier(trademarkTokenFrequency: Pick<ITrademarkTokenFrequency, 'id'>): number {
    return trademarkTokenFrequency.id;
  }

  compareTrademarkTokenFrequency(
    o1: Pick<ITrademarkTokenFrequency, 'id'> | null,
    o2: Pick<ITrademarkTokenFrequency, 'id'> | null,
  ): boolean {
    return o1 && o2 ? this.getTrademarkTokenFrequencyIdentifier(o1) === this.getTrademarkTokenFrequencyIdentifier(o2) : o1 === o2;
  }

  addTrademarkTokenFrequencyToCollectionIfMissing<Type extends Pick<ITrademarkTokenFrequency, 'id'>>(
    trademarkTokenFrequencyCollection: Type[],
    ...trademarkTokenFrequenciesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const trademarkTokenFrequencies: Type[] = trademarkTokenFrequenciesToCheck.filter(isPresent);
    if (trademarkTokenFrequencies.length > 0) {
      const trademarkTokenFrequencyCollectionIdentifiers = trademarkTokenFrequencyCollection.map(trademarkTokenFrequencyItem =>
        this.getTrademarkTokenFrequencyIdentifier(trademarkTokenFrequencyItem),
      );
      const trademarkTokenFrequenciesToAdd = trademarkTokenFrequencies.filter(trademarkTokenFrequencyItem => {
        const trademarkTokenFrequencyIdentifier = this.getTrademarkTokenFrequencyIdentifier(trademarkTokenFrequencyItem);
        if (trademarkTokenFrequencyCollectionIdentifiers.includes(trademarkTokenFrequencyIdentifier)) {
          return false;
        }
        trademarkTokenFrequencyCollectionIdentifiers.push(trademarkTokenFrequencyIdentifier);
        return true;
      });
      return [...trademarkTokenFrequenciesToAdd, ...trademarkTokenFrequencyCollection];
    }
    return trademarkTokenFrequencyCollection;
  }

  protected convertDateFromClient<T extends ITrademarkTokenFrequency | NewTrademarkTokenFrequency | PartialUpdateTrademarkTokenFrequency>(
    trademarkTokenFrequency: T,
  ): RestOf<T> {
    return {
      ...trademarkTokenFrequency,
      createdDate: trademarkTokenFrequency.createdDate?.toJSON() ?? null,
      modifiedDate: trademarkTokenFrequency.modifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restTrademarkTokenFrequency: RestTrademarkTokenFrequency): ITrademarkTokenFrequency {
    return {
      ...restTrademarkTokenFrequency,
      createdDate: restTrademarkTokenFrequency.createdDate ? dayjs(restTrademarkTokenFrequency.createdDate) : undefined,
      modifiedDate: restTrademarkTokenFrequency.modifiedDate ? dayjs(restTrademarkTokenFrequency.modifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestTrademarkTokenFrequency>): HttpResponse<ITrademarkTokenFrequency> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestTrademarkTokenFrequency[]>): HttpResponse<ITrademarkTokenFrequency[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
