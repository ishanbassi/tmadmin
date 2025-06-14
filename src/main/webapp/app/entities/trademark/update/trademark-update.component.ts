import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICompany } from 'app/entities/company/company.model';
import { CompanyService } from 'app/entities/company/service/company.service';
import { HeadOffice } from 'app/entities/enumerations/head-office.model';
import { TrademarkType } from 'app/entities/enumerations/trademark-type.model';
import { TrademarkService } from '../service/trademark.service';
import { ITrademark } from '../trademark.model';
import { TrademarkFormGroup, TrademarkFormService } from './trademark-form.service';

@Component({
  selector: 'jhi-trademark-update',
  templateUrl: './trademark-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TrademarkUpdateComponent implements OnInit {
  isSaving = false;
  trademark: ITrademark | null = null;
  headOfficeValues = Object.keys(HeadOffice);
  trademarkTypeValues = Object.keys(TrademarkType);

  companiesSharedCollection: ICompany[] = [];

  protected trademarkService = inject(TrademarkService);
  protected trademarkFormService = inject(TrademarkFormService);
  protected companyService = inject(CompanyService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TrademarkFormGroup = this.trademarkFormService.createTrademarkFormGroup();

  compareCompany = (o1: ICompany | null, o2: ICompany | null): boolean => this.companyService.compareCompany(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ trademark }) => {
      this.trademark = trademark;
      if (trademark) {
        this.updateForm(trademark);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const trademark = this.trademarkFormService.getTrademark(this.editForm);
    if (trademark.id !== null) {
      this.subscribeToSaveResponse(this.trademarkService.update(trademark));
    } else {
      this.subscribeToSaveResponse(this.trademarkService.create(trademark));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITrademark>>): void {
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

  protected updateForm(trademark: ITrademark): void {
    this.trademark = trademark;
    this.trademarkFormService.resetForm(this.editForm, trademark);

    this.companiesSharedCollection = this.companyService.addCompanyToCollectionIfMissing<ICompany>(
      this.companiesSharedCollection,
      trademark.company,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.companyService
      .query()
      .pipe(map((res: HttpResponse<ICompany[]>) => res.body ?? []))
      .pipe(
        map((companies: ICompany[]) => this.companyService.addCompanyToCollectionIfMissing<ICompany>(companies, this.trademark?.company)),
      )
      .subscribe((companies: ICompany[]) => (this.companiesSharedCollection = companies));
  }
}
