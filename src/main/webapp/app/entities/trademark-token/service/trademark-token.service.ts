import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITrademarkToken, NewTrademarkToken } from '../trademark-token.model';

export type PartialUpdateTrademarkToken = Partial<ITrademarkToken> & Pick<ITrademarkToken, 'id'>;

export type EntityResponseType = HttpResponse<ITrademarkToken>;
export type EntityArrayResponseType = HttpResponse<ITrademarkToken[]>;

@Injectable({ providedIn: 'root' })
export class TrademarkTokenService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/trademark-tokens');

  create(trademarkToken: NewTrademarkToken): Observable<EntityResponseType> {
    return this.http.post<ITrademarkToken>(this.resourceUrl, trademarkToken, { observe: 'response' });
  }

  update(trademarkToken: ITrademarkToken): Observable<EntityResponseType> {
    return this.http.put<ITrademarkToken>(`${this.resourceUrl}/${this.getTrademarkTokenIdentifier(trademarkToken)}`, trademarkToken, {
      observe: 'response',
    });
  }

  partialUpdate(trademarkToken: PartialUpdateTrademarkToken): Observable<EntityResponseType> {
    return this.http.patch<ITrademarkToken>(`${this.resourceUrl}/${this.getTrademarkTokenIdentifier(trademarkToken)}`, trademarkToken, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITrademarkToken>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITrademarkToken[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTrademarkTokenIdentifier(trademarkToken: Pick<ITrademarkToken, 'id'>): number {
    return trademarkToken.id;
  }

  compareTrademarkToken(o1: Pick<ITrademarkToken, 'id'> | null, o2: Pick<ITrademarkToken, 'id'> | null): boolean {
    return o1 && o2 ? this.getTrademarkTokenIdentifier(o1) === this.getTrademarkTokenIdentifier(o2) : o1 === o2;
  }

  addTrademarkTokenToCollectionIfMissing<Type extends Pick<ITrademarkToken, 'id'>>(
    trademarkTokenCollection: Type[],
    ...trademarkTokensToCheck: (Type | null | undefined)[]
  ): Type[] {
    const trademarkTokens: Type[] = trademarkTokensToCheck.filter(isPresent);
    if (trademarkTokens.length > 0) {
      const trademarkTokenCollectionIdentifiers = trademarkTokenCollection.map(trademarkTokenItem =>
        this.getTrademarkTokenIdentifier(trademarkTokenItem),
      );
      const trademarkTokensToAdd = trademarkTokens.filter(trademarkTokenItem => {
        const trademarkTokenIdentifier = this.getTrademarkTokenIdentifier(trademarkTokenItem);
        if (trademarkTokenCollectionIdentifiers.includes(trademarkTokenIdentifier)) {
          return false;
        }
        trademarkTokenCollectionIdentifiers.push(trademarkTokenIdentifier);
        return true;
      });
      return [...trademarkTokensToAdd, ...trademarkTokenCollection];
    }
    return trademarkTokenCollection;
  }
}
