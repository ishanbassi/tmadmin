import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITmAgent, NewTmAgent } from '../tm-agent.model';

export type PartialUpdateTmAgent = Partial<ITmAgent> & Pick<ITmAgent, 'id'>;

type RestOf<T extends ITmAgent | NewTmAgent> = Omit<T, 'createdDate' | 'modifiedDate'> & {
  createdDate?: string | null;
  modifiedDate?: string | null;
};

export type RestTmAgent = RestOf<ITmAgent>;

export type NewRestTmAgent = RestOf<NewTmAgent>;

export type PartialUpdateRestTmAgent = RestOf<PartialUpdateTmAgent>;

export type EntityResponseType = HttpResponse<ITmAgent>;
export type EntityArrayResponseType = HttpResponse<ITmAgent[]>;

@Injectable({ providedIn: 'root' })
export class TmAgentService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tm-agents');

  create(tmAgent: NewTmAgent): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tmAgent);
    return this.http
      .post<RestTmAgent>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(tmAgent: ITmAgent): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tmAgent);
    return this.http
      .put<RestTmAgent>(`${this.resourceUrl}/${this.getTmAgentIdentifier(tmAgent)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(tmAgent: PartialUpdateTmAgent): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tmAgent);
    return this.http
      .patch<RestTmAgent>(`${this.resourceUrl}/${this.getTmAgentIdentifier(tmAgent)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestTmAgent>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestTmAgent[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
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

  protected convertDateFromClient<T extends ITmAgent | NewTmAgent | PartialUpdateTmAgent>(tmAgent: T): RestOf<T> {
    return {
      ...tmAgent,
      createdDate: tmAgent.createdDate?.toJSON() ?? null,
      modifiedDate: tmAgent.modifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restTmAgent: RestTmAgent): ITmAgent {
    return {
      ...restTmAgent,
      createdDate: restTmAgent.createdDate ? dayjs(restTmAgent.createdDate) : undefined,
      modifiedDate: restTmAgent.modifiedDate ? dayjs(restTmAgent.modifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestTmAgent>): HttpResponse<ITmAgent> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestTmAgent[]>): HttpResponse<ITmAgent[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
