import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { ContactMethod } from 'app/entities/enumerations/contact-method.model';
import { LeadStatus } from 'app/entities/enumerations/lead-status.model';
import { LeadService } from '../service/lead.service';
import { ILead } from '../lead.model';
import { LeadFormService, LeadFormGroup } from './lead-form.service';

@Component({
  standalone: true,
  selector: 'jhi-lead-update',
  templateUrl: './lead-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class LeadUpdateComponent implements OnInit {
  isSaving = false;
  lead: ILead | null = null;
  contactMethodValues = Object.keys(ContactMethod);
  leadStatusValues = Object.keys(LeadStatus);

  employeesSharedCollection: IEmployee[] = [];

  protected leadService = inject(LeadService);
  protected leadFormService = inject(LeadFormService);
  protected employeeService = inject(EmployeeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: LeadFormGroup = this.leadFormService.createLeadFormGroup();

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lead }) => {
      this.lead = lead;
      if (lead) {
        this.updateForm(lead);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const lead = this.leadFormService.getLead(this.editForm);
    if (lead.id !== null) {
      this.subscribeToSaveResponse(this.leadService.update(lead));
    } else {
      this.subscribeToSaveResponse(this.leadService.create(lead));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILead>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(lead: ILead): void {
    this.lead = lead;
    this.leadFormService.resetForm(this.editForm, lead);

    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
      this.employeesSharedCollection,
      lead.assignedTo,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) => this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, this.lead?.assignedTo)),
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));
  }
}
