import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { HeadOffice } from 'app/entities/enumerations/head-office.model';
import { TrademarkStatus } from 'app/entities/enumerations/trademark-status.model';
import { IPublishedTm } from '../published-tm.model';
import { PublishedTmService } from '../service/published-tm.service';
import { PublishedTmFormService, PublishedTmFormGroup } from './published-tm-form.service';

@Component({
  standalone: true,
  selector: 'jhi-published-tm-update',
  templateUrl: './published-tm-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PublishedTmUpdateComponent implements OnInit {
  isSaving = false;
  publishedTm: IPublishedTm | null = null;
  headOfficeValues = Object.keys(HeadOffice);
  trademarkStatusValues = Object.keys(TrademarkStatus);

  protected publishedTmService = inject(PublishedTmService);
  protected publishedTmFormService = inject(PublishedTmFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PublishedTmFormGroup = this.publishedTmFormService.createPublishedTmFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ publishedTm }) => {
      this.publishedTm = publishedTm;
      if (publishedTm) {
        this.updateForm(publishedTm);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const publishedTm = this.publishedTmFormService.getPublishedTm(this.editForm);
    if (publishedTm.id !== null) {
      this.subscribeToSaveResponse(this.publishedTmService.update(publishedTm));
    } else {
      this.subscribeToSaveResponse(this.publishedTmService.create(publishedTm));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPublishedTm>>): void {
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

  protected updateForm(publishedTm: IPublishedTm): void {
    this.publishedTm = publishedTm;
    this.publishedTmFormService.resetForm(this.editForm, publishedTm);
  }
}
