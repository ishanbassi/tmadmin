import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITmAgent } from 'app/entities/tm-agent/tm-agent.model';
import { TmAgentService } from 'app/entities/tm-agent/service/tm-agent.service';
import { HeadOffice } from 'app/entities/enumerations/head-office.model';
import { TrademarkType } from 'app/entities/enumerations/trademark-type.model';
import { PublishedTmService } from '../service/published-tm.service';
import { IPublishedTm } from '../published-tm.model';
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
  trademarkTypeValues = Object.keys(TrademarkType);

  tmAgentsSharedCollection: ITmAgent[] = [];

  protected publishedTmService = inject(PublishedTmService);
  protected publishedTmFormService = inject(PublishedTmFormService);
  protected tmAgentService = inject(TmAgentService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PublishedTmFormGroup = this.publishedTmFormService.createPublishedTmFormGroup();

  compareTmAgent = (o1: ITmAgent | null, o2: ITmAgent | null): boolean => this.tmAgentService.compareTmAgent(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ publishedTm }) => {
      this.publishedTm = publishedTm;
      if (publishedTm) {
        this.updateForm(publishedTm);
      }

      this.loadRelationshipsOptions();
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

    this.tmAgentsSharedCollection = this.tmAgentService.addTmAgentToCollectionIfMissing<ITmAgent>(
      this.tmAgentsSharedCollection,
      publishedTm.tmAgent,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.tmAgentService
      .query()
      .pipe(map((res: HttpResponse<ITmAgent[]>) => res.body ?? []))
      .pipe(
        map((tmAgents: ITmAgent[]) => this.tmAgentService.addTmAgentToCollectionIfMissing<ITmAgent>(tmAgents, this.publishedTm?.tmAgent)),
      )
      .subscribe((tmAgents: ITmAgent[]) => (this.tmAgentsSharedCollection = tmAgents));
  }
}
