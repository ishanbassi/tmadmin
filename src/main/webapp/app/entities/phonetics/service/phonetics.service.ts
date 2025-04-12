import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPhonetics, NewPhonetics } from '../phonetics.model';

export type PartialUpdatePhonetics = Partial<IPhonetics> & Pick<IPhonetics, 'id'>;

export type EntityResponseType = HttpResponse<IPhonetics>;
export type EntityArrayResponseType = HttpResponse<IPhonetics[]>;

@Injectable({ providedIn: 'root' })
export class PhoneticsService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/phonetics');

  create(phonetics: NewPhonetics): Observable<EntityResponseType> {
    return this.http.post<IPhonetics>(this.resourceUrl, phonetics, { observe: 'response' });
  }

  update(phonetics: IPhonetics): Observable<EntityResponseType> {
    return this.http.put<IPhonetics>(`${this.resourceUrl}/${this.getPhoneticsIdentifier(phonetics)}`, phonetics, { observe: 'response' });
  }

  partialUpdate(phonetics: PartialUpdatePhonetics): Observable<EntityResponseType> {
    return this.http.patch<IPhonetics>(`${this.resourceUrl}/${this.getPhoneticsIdentifier(phonetics)}`, phonetics, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPhonetics>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPhonetics[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPhoneticsIdentifier(phonetics: Pick<IPhonetics, 'id'>): number {
    return phonetics.id;
  }

  comparePhonetics(o1: Pick<IPhonetics, 'id'> | null, o2: Pick<IPhonetics, 'id'> | null): boolean {
    return o1 && o2 ? this.getPhoneticsIdentifier(o1) === this.getPhoneticsIdentifier(o2) : o1 === o2;
  }

  addPhoneticsToCollectionIfMissing<Type extends Pick<IPhonetics, 'id'>>(
    phoneticsCollection: Type[],
    ...phoneticsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const phonetics: Type[] = phoneticsToCheck.filter(isPresent);
    if (phonetics.length > 0) {
      const phoneticsCollectionIdentifiers = phoneticsCollection.map(phoneticsItem => this.getPhoneticsIdentifier(phoneticsItem));
      const phoneticsToAdd = phonetics.filter(phoneticsItem => {
        const phoneticsIdentifier = this.getPhoneticsIdentifier(phoneticsItem);
        if (phoneticsCollectionIdentifiers.includes(phoneticsIdentifier)) {
          return false;
        }
        phoneticsCollectionIdentifiers.push(phoneticsIdentifier);
        return true;
      });
      return [...phoneticsToAdd, ...phoneticsCollection];
    }
    return phoneticsCollection;
  }
}
