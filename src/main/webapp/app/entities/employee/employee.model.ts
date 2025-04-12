import dayjs from 'dayjs/esm';

export interface IEmployee {
  id: number;
  fullName?: string | null;
  phoneNumber?: string | null;
  email?: string | null;
  createdDate?: dayjs.Dayjs | null;
  modifiedDate?: dayjs.Dayjs | null;
  deleted?: boolean | null;
  designation?: string | null;
  joiningDate?: dayjs.Dayjs | null;
}

export type NewEmployee = Omit<IEmployee, 'id'> & { id: null };
