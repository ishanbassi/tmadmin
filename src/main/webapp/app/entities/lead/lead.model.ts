import dayjs from 'dayjs/esm';
import { IEmployee } from 'app/entities/employee/employee.model';
import { ContactMethod } from 'app/entities/enumerations/contact-method.model';
import { LeadStatus } from 'app/entities/enumerations/lead-status.model';

export interface ILead {
  id: number;
  fullName?: string | null;
  phoneNumber?: string | null;
  email?: string | null;
  city?: string | null;
  brandName?: string | null;
  selectedPackage?: string | null;
  tmClass?: number | null;
  comments?: string | null;
  contactMethod?: keyof typeof ContactMethod | null;
  createdDate?: dayjs.Dayjs | null;
  modifiedDate?: dayjs.Dayjs | null;
  deleted?: boolean | null;
  status?: keyof typeof LeadStatus | null;
  leadSource?: string | null;
  assignedTo?: Pick<IEmployee, 'id' | 'fullName'> | null;
}

export type NewLead = Omit<ILead, 'id'> & { id: null };
