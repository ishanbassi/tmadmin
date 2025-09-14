import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITrademarkPlan } from '../trademark-plan.model';
import { TrademarkPlanService } from '../service/trademark-plan.service';
import { TrademarkPlanFormGroup, TrademarkPlanFormService } from './trademark-plan-form.service';

@Component({
  selector: 'jhi-trademark-plan-update',
  templateUrl: './trademark-plan-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TrademarkPlanUpdateComponent implements OnInit {
  isSaving = false;
  trademarkPlan: ITrademarkPlan | null = null;

  protected trademarkPlanService = inject(TrademarkPlanService);
  protected trademarkPlanFormService = inject(TrademarkPlanFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TrademarkPlanFormGroup = this.trademarkPlanFormService.createTrademarkPlanFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ trademarkPlan }) => {
      this.trademarkPlan = trademarkPlan;
      if (trademarkPlan) {
        this.updateForm(trademarkPlan);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const trademarkPlan = this.trademarkPlanFormService.getTrademarkPlan(this.editForm);
    if (trademarkPlan.id !== null) {
      this.subscribeToSaveResponse(this.trademarkPlanService.update(trademarkPlan));
    } else {
      this.subscribeToSaveResponse(this.trademarkPlanService.create(trademarkPlan));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITrademarkPlan>>): void {
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

  protected updateForm(trademarkPlan: ITrademarkPlan): void {
    this.trademarkPlan = trademarkPlan;
    this.trademarkPlanFormService.resetForm(this.editForm, trademarkPlan);
  }
}
