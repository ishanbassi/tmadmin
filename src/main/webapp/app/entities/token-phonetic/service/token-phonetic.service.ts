import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITokenPhonetic, NewTokenPhonetic } from '../token-phonetic.model';

export type PartialUpdateTokenPhonetic = Partial<ITokenPhonetic> & Pick<ITokenPhonetic, 'id'>;

export type EntityResponseType = HttpResponse<ITokenPhonetic>;
export type EntityArrayResponseType = HttpResponse<ITokenPhonetic[]>;

@Injectable({ providedIn: 'root' })
export class TokenPhoneticService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/token-phonetics');

  create(tokenPhonetic: NewTokenPhonetic): Observable<EntityResponseType> {
    return this.http.post<ITokenPhonetic>(this.resourceUrl, tokenPhonetic, { observe: 'response' });
  }

  update(tokenPhonetic: ITokenPhonetic): Observable<EntityResponseType> {
    return this.http.put<ITokenPhonetic>(`${this.resourceUrl}/${this.getTokenPhoneticIdentifier(tokenPhonetic)}`, tokenPhonetic, {
      observe: 'response',
    });
  }

  partialUpdate(tokenPhonetic: PartialUpdateTokenPhonetic): Observable<EntityResponseType> {
    return this.http.patch<ITokenPhonetic>(`${this.resourceUrl}/${this.getTokenPhoneticIdentifier(tokenPhonetic)}`, tokenPhonetic, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITokenPhonetic>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITokenPhonetic[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTokenPhoneticIdentifier(tokenPhonetic: Pick<ITokenPhonetic, 'id'>): number {
    return tokenPhonetic.id;
  }

  compareTokenPhonetic(o1: Pick<ITokenPhonetic, 'id'> | null, o2: Pick<ITokenPhonetic, 'id'> | null): boolean {
    return o1 && o2 ? this.getTokenPhoneticIdentifier(o1) === this.getTokenPhoneticIdentifier(o2) : o1 === o2;
  }

  addTokenPhoneticToCollectionIfMissing<Type extends Pick<ITokenPhonetic, 'id'>>(
    tokenPhoneticCollection: Type[],
    ...tokenPhoneticsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const tokenPhonetics: Type[] = tokenPhoneticsToCheck.filter(isPresent);
    if (tokenPhonetics.length > 0) {
      const tokenPhoneticCollectionIdentifiers = tokenPhoneticCollection.map(tokenPhoneticItem =>
        this.getTokenPhoneticIdentifier(tokenPhoneticItem),
      );
      const tokenPhoneticsToAdd = tokenPhonetics.filter(tokenPhoneticItem => {
        const tokenPhoneticIdentifier = this.getTokenPhoneticIdentifier(tokenPhoneticItem);
        if (tokenPhoneticCollectionIdentifiers.includes(tokenPhoneticIdentifier)) {
          return false;
        }
        tokenPhoneticCollectionIdentifiers.push(tokenPhoneticIdentifier);
        return true;
      });
      return [...tokenPhoneticsToAdd, ...tokenPhoneticCollection];
    }
    return tokenPhoneticCollection;
  }
}
