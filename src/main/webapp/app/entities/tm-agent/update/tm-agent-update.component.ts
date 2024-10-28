import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITmAgent } from '../tm-agent.model';
import { TmAgentService } from '../service/tm-agent.service';
import { TmAgentFormService, TmAgentFormGroup } from './tm-agent-form.service';

@Component({
  standalone: true,
  selector: 'jhi-tm-agent-update',
  templateUrl: './tm-agent-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TmAgentUpdateComponent implements OnInit {
  isSaving = false;
  tmAgent: ITmAgent | null = null;

  protected tmAgentService = inject(TmAgentService);
  protected tmAgentFormService = inject(TmAgentFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TmAgentFormGroup = this.tmAgentFormService.createTmAgentFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tmAgent }) => {
      this.tmAgent = tmAgent;
      if (tmAgent) {
        this.updateForm(tmAgent);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tmAgent = this.tmAgentFormService.getTmAgent(this.editForm);
    if (tmAgent.id !== null) {
      this.subscribeToSaveResponse(this.tmAgentService.update(tmAgent));
    } else {
      this.subscribeToSaveResponse(this.tmAgentService.create(tmAgent));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITmAgent>>): void {
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

  protected updateForm(tmAgent: ITmAgent): void {
    this.tmAgent = tmAgent;
    this.tmAgentFormService.resetForm(this.editForm, tmAgent);
  }
}
