import { ITrademark } from 'app/entities/trademark/trademark.model';

export interface IPhonetics {
  id: number;
  sanitizedTm?: string | null;
  phoneticPk?: string | null;
  phoneticSk?: string | null;
  complete?: boolean | null;
  trademark?: Pick<ITrademark, 'id'> | null;
}

export type NewPhonetics = Omit<IPhonetics, 'id'> & { id: null };
