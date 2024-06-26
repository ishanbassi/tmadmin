export interface ITmAgent {
  id: number;
  agentCode?: number | null;
  firstName?: string | null;
  lastName?: string | null;
  address?: string | null;
}

export type NewTmAgent = Omit<ITmAgent, 'id'> & { id: null };
