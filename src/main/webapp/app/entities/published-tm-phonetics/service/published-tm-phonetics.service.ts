import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPublishedTmPhonetics, NewPublishedTmPhonetics } from '../published-tm-phonetics.model';

export type PartialUpdatePublishedTmPhonetics = Partial<IPublishedTmPhonetics> & Pick<IPublishedTmPhonetics, 'id'>;

export type EntityResponseType = HttpResponse<IPublishedTmPhonetics>;
export type EntityArrayResponseType = HttpResponse<IPublishedTmPhonetics[]>;

@Injectable({ providedIn: 'root' })
export class PublishedTmPhoneticsService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/published-tm-phonetics');

  create(publishedTmPhonetics: NewPublishedTmPhonetics): Observable<EntityResponseType> {
    return this.http.post<IPublishedTmPhonetics>(this.resourceUrl, publishedTmPhonetics, { observe: 'response' });
  }

  update(publishedTmPhonetics: IPublishedTmPhonetics): Observable<EntityResponseType> {
    return this.http.put<IPublishedTmPhonetics>(
      `${this.resourceUrl}/${this.getPublishedTmPhoneticsIdentifier(publishedTmPhonetics)}`,
      publishedTmPhonetics,
      { observe: 'response' },
    );
  }

  partialUpdate(publishedTmPhonetics: PartialUpdatePublishedTmPhonetics): Observable<EntityResponseType> {
    return this.http.patch<IPublishedTmPhonetics>(
      `${this.resourceUrl}/${this.getPublishedTmPhoneticsIdentifier(publishedTmPhonetics)}`,
      publishedTmPhonetics,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPublishedTmPhonetics>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPublishedTmPhonetics[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPublishedTmPhoneticsIdentifier(publishedTmPhonetics: Pick<IPublishedTmPhonetics, 'id'>): number {
    return publishedTmPhonetics.id;
  }

  comparePublishedTmPhonetics(o1: Pick<IPublishedTmPhonetics, 'id'> | null, o2: Pick<IPublishedTmPhonetics, 'id'> | null): boolean {
    return o1 && o2 ? this.getPublishedTmPhoneticsIdentifier(o1) === this.getPublishedTmPhoneticsIdentifier(o2) : o1 === o2;
  }

  addPublishedTmPhoneticsToCollectionIfMissing<Type extends Pick<IPublishedTmPhonetics, 'id'>>(
    publishedTmPhoneticsCollection: Type[],
    ...publishedTmPhoneticsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const publishedTmPhonetics: Type[] = publishedTmPhoneticsToCheck.filter(isPresent);
    if (publishedTmPhonetics.length > 0) {
      const publishedTmPhoneticsCollectionIdentifiers = publishedTmPhoneticsCollection.map(publishedTmPhoneticsItem =>
        this.getPublishedTmPhoneticsIdentifier(publishedTmPhoneticsItem),
      );
      const publishedTmPhoneticsToAdd = publishedTmPhonetics.filter(publishedTmPhoneticsItem => {
        const publishedTmPhoneticsIdentifier = this.getPublishedTmPhoneticsIdentifier(publishedTmPhoneticsItem);
        if (publishedTmPhoneticsCollectionIdentifiers.includes(publishedTmPhoneticsIdentifier)) {
          return false;
        }
        publishedTmPhoneticsCollectionIdentifiers.push(publishedTmPhoneticsIdentifier);
        return true;
      });
      return [...publishedTmPhoneticsToAdd, ...publishedTmPhoneticsCollection];
    }
    return publishedTmPhoneticsCollection;
  }
}
