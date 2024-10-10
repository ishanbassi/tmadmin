import dayjs from 'dayjs/esm';

export interface ITmAgent {
  id: number;
  firstName?: string | null;
  lastName?: string | null;
  address?: string | null;
  createdDate?: dayjs.Dayjs | null;
  modifiedDate?: dayjs.Dayjs | null;
  deleted?: boolean | null;
  companyName?: string | null;
  agentCode?: string | null;
  email?: string | null;
}

export type NewTmAgent = Omit<ITmAgent, 'id'> & { id: null };
