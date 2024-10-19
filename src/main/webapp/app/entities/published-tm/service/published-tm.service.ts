import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPublishedTm, NewPublishedTm } from '../published-tm.model';

export type PartialUpdatePublishedTm = Partial<IPublishedTm> & Pick<IPublishedTm, 'id'>;

type RestOf<T extends IPublishedTm | NewPublishedTm> = Omit<T, 'applicationDate' | 'createdDate' | 'modifiedDate'> & {
  applicationDate?: string | null;
  createdDate?: string | null;
  modifiedDate?: string | null;
};

export type RestPublishedTm = RestOf<IPublishedTm>;

export type NewRestPublishedTm = RestOf<NewPublishedTm>;

export type PartialUpdateRestPublishedTm = RestOf<PartialUpdatePublishedTm>;

export type EntityResponseType = HttpResponse<IPublishedTm>;
export type EntityArrayResponseType = HttpResponse<IPublishedTm[]>;

@Injectable({ providedIn: 'root' })
export class PublishedTmService {
  
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/published-tms');
  protected exportUrl = this.applicationConfigService.getEndpointFor('api/matching/trademarks');

  create(publishedTm: NewPublishedTm): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(publishedTm);
    return this.http
      .post<RestPublishedTm>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(publishedTm: IPublishedTm): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(publishedTm);
    return this.http
      .put<RestPublishedTm>(`${this.resourceUrl}/${this.getPublishedTmIdentifier(publishedTm)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(publishedTm: PartialUpdatePublishedTm): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(publishedTm);
    return this.http
      .patch<RestPublishedTm>(`${this.resourceUrl}/${this.getPublishedTmIdentifier(publishedTm)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestPublishedTm>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPublishedTm[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPublishedTmIdentifier(publishedTm: Pick<IPublishedTm, 'id'>): number {
    return publishedTm.id;
  }

  comparePublishedTm(o1: Pick<IPublishedTm, 'id'> | null, o2: Pick<IPublishedTm, 'id'> | null): boolean {
    return o1 && o2 ? this.getPublishedTmIdentifier(o1) === this.getPublishedTmIdentifier(o2) : o1 === o2;
  }

  addPublishedTmToCollectionIfMissing<Type extends Pick<IPublishedTm, 'id'>>(
    publishedTmCollection: Type[],
    ...publishedTmsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const publishedTms: Type[] = publishedTmsToCheck.filter(isPresent);
    if (publishedTms.length > 0) {
      const publishedTmCollectionIdentifiers = publishedTmCollection.map(publishedTmItem => this.getPublishedTmIdentifier(publishedTmItem));
      const publishedTmsToAdd = publishedTms.filter(publishedTmItem => {
        const publishedTmIdentifier = this.getPublishedTmIdentifier(publishedTmItem);
        if (publishedTmCollectionIdentifiers.includes(publishedTmIdentifier)) {
          return false;
        }
        publishedTmCollectionIdentifiers.push(publishedTmIdentifier);
        return true;
      });
      return [...publishedTmsToAdd, ...publishedTmCollection];
    }
    return publishedTmCollection;
  }

  protected convertDateFromClient<T extends IPublishedTm | NewPublishedTm | PartialUpdatePublishedTm>(publishedTm: T): RestOf<T> {
    return {
      ...publishedTm,
      applicationDate: publishedTm.applicationDate?.format(DATE_FORMAT) ?? null,
      createdDate: publishedTm.createdDate?.toJSON() ?? null,
      modifiedDate: publishedTm.modifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restPublishedTm: RestPublishedTm): IPublishedTm {
    return {
      ...restPublishedTm,
      applicationDate: restPublishedTm.applicationDate ? dayjs(restPublishedTm.applicationDate) : undefined,
      createdDate: restPublishedTm.createdDate ? dayjs(restPublishedTm.createdDate) : undefined,
      modifiedDate: restPublishedTm.modifiedDate ? dayjs(restPublishedTm.modifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestPublishedTm>): HttpResponse<IPublishedTm> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestPublishedTm[]>): HttpResponse<IPublishedTm[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }

  download(req?: any): Observable<HttpResponse<Blob>> {
    const options = createRequestOption(req);
    return this.http
      .get<Blob>(this.exportUrl+"/download/2176", { observe: 'response', responseType: 'blob' as 'json' });
  }
}
