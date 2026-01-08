import { ITrademark } from 'app/entities/trademark/trademark.model';
import { TrademarkTokenType } from 'app/entities/enumerations/trademark-token-type.model';

export interface ITrademarkToken {
  id: number;
  tokenText?: string | null;
  tokenType?: keyof typeof TrademarkTokenType | null;
  position?: number | null;
  trademark?: Pick<ITrademark, 'id'> | null;
}

export type NewTrademarkToken = Omit<ITrademarkToken, 'id'> & { id: null };
