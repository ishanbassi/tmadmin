import { IPublishedTm } from 'app/entities/published-tm/published-tm.model';

export interface IPublishedTmPhonetics {
  id: number;
  sanitizedTm?: string | null;
  phoneticPk?: string | null;
  phoneticSk?: string | null;
  complete?: boolean | null;
  publishedTm?: Pick<IPublishedTm, 'id'> | null;
}

export type NewPublishedTmPhonetics = Omit<IPublishedTmPhonetics, 'id'> & { id: null };
