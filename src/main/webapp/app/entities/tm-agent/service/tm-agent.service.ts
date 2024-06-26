import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITmAgent, NewTmAgent } from '../tm-agent.model';

export type PartialUpdateTmAgent = Partial<ITmAgent> & Pick<ITmAgent, 'id'>;

export type EntityResponseType = HttpResponse<ITmAgent>;
export type EntityArrayResponseType = HttpResponse<ITmAgent[]>;

@Injectable({ providedIn: 'root' })
export class TmAgentService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tm-agents');

  create(tmAgent: NewTmAgent): Observable<EntityResponseType> {
    return this.http.post<ITmAgent>(this.resourceUrl, tmAgent, { observe: 'response' });
  }

  update(tmAgent: ITmAgent): Observable<EntityResponseType> {
    return this.http.put<ITmAgent>(`${this.resourceUrl}/${this.getTmAgentIdentifier(tmAgent)}`, tmAgent, { observe: 'response' });
  }

  partialUpdate(tmAgent: PartialUpdateTmAgent): Observable<EntityResponseType> {
    return this.http.patch<ITmAgent>(`${this.resourceUrl}/${this.getTmAgentIdentifier(tmAgent)}`, tmAgent, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITmAgent>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITmAgent[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTmAgentIdentifier(tmAgent: Pick<ITmAgent, 'id'>): number {
    return tmAgent.id;
  }

  compareTmAgent(o1: Pick<ITmAgent, 'id'> | null, o2: Pick<ITmAgent, 'id'> | null): boolean {
    return o1 && o2 ? this.getTmAgentIdentifier(o1) === this.getTmAgentIdentifier(o2) : o1 === o2;
  }

  addTmAgentToCollectionIfMissing<Type extends Pick<ITmAgent, 'id'>>(
    tmAgentCollection: Type[],
    ...tmAgentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const tmAgents: Type[] = tmAgentsToCheck.filter(isPresent);
    if (tmAgents.length > 0) {
      const tmAgentCollectionIdentifiers = tmAgentCollection.map(tmAgentItem => this.getTmAgentIdentifier(tmAgentItem));
      const tmAgentsToAdd = tmAgents.filter(tmAgentItem => {
        const tmAgentIdentifier = this.getTmAgentIdentifier(tmAgentItem);
        if (tmAgentCollectionIdentifiers.includes(tmAgentIdentifier)) {
          return false;
        }
        tmAgentCollectionIdentifiers.push(tmAgentIdentifier);
        return true;
      });
      return [...tmAgentsToAdd, ...tmAgentCollection];
    }
    return tmAgentCollection;
  }
}
