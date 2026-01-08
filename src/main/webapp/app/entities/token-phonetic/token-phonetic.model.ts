import { ITrademarkToken } from 'app/entities/trademark-token/trademark-token.model';
import { PhoneticAlgorithmType } from 'app/entities/enumerations/phonetic-algorithm-type.model';

export interface ITokenPhonetic {
  id: number;
  algorithm?: keyof typeof PhoneticAlgorithmType | null;
  phoneticCode?: string | null;
  secondaryPhoneticCode?: string | null;
  trademarkToken?: Pick<ITrademarkToken, 'id'> | null;
}

export type NewTokenPhonetic = Omit<ITokenPhonetic, 'id'> & { id: null };
