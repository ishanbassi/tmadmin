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
import { TrademarkStatus } from 'app/entities/enumerations/trademark-status.model';
import { TrademarkService } from '../service/trademark.service';
import { ITrademark } from '../trademark.model';
import { TrademarkFormService, TrademarkFormGroup } from './trademark-form.service';

@Component({
  standalone: true,
  selector: 'jhi-trademark-update',
  templateUrl: './trademark-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TrademarkUpdateComponent implements OnInit {
  isSaving = false;
  trademark: ITrademark | null = null;
  headOfficeValues = Object.keys(HeadOffice);
  trademarkStatusValues = Object.keys(TrademarkStatus);

  tmAgentsSharedCollection: ITmAgent[] = [];

  protected trademarkService = inject(TrademarkService);
  protected trademarkFormService = inject(TrademarkFormService);
  protected tmAgentService = inject(TmAgentService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TrademarkFormGroup = this.trademarkFormService.createTrademarkFormGroup();

  compareTmAgent = (o1: ITmAgent | null, o2: ITmAgent | null): boolean => this.tmAgentService.compareTmAgent(o1, o2);

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

    this.tmAgentsSharedCollection = this.tmAgentService.addTmAgentToCollectionIfMissing<ITmAgent>(
      this.tmAgentsSharedCollection,
      trademark.tmAgent,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.tmAgentService
      .query()
      .pipe(map((res: HttpResponse<ITmAgent[]>) => res.body ?? []))
      .pipe(map((tmAgents: ITmAgent[]) => this.tmAgentService.addTmAgentToCollectionIfMissing<ITmAgent>(tmAgents, this.trademark?.tmAgent)))
      .subscribe((tmAgents: ITmAgent[]) => (this.tmAgentsSharedCollection = tmAgents));
  }
}
