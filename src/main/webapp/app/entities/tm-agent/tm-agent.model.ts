import dayjs from 'dayjs/esm';

export interface ITmAgent {
  id: number;
  agentCode?: number | null;
  firstName?: string | null;
  lastName?: string | null;
  address?: string | null;
  createdDate?: dayjs.Dayjs | null;
  modifiedDate?: dayjs.Dayjs | null;
}

export type NewTmAgent = Omit<ITmAgent, 'id'> & { id: null };
