import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITrademarkTokenFrequency } from '../trademark-token-frequency.model';
import { TrademarkTokenFrequencyService } from '../service/trademark-token-frequency.service';
import { TrademarkTokenFrequencyFormGroup, TrademarkTokenFrequencyFormService } from './trademark-token-frequency-form.service';

@Component({
  selector: 'jhi-trademark-token-frequency-update',
  templateUrl: './trademark-token-frequency-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TrademarkTokenFrequencyUpdateComponent implements OnInit {
  isSaving = false;
  trademarkTokenFrequency: ITrademarkTokenFrequency | null = null;

  protected trademarkTokenFrequencyService = inject(TrademarkTokenFrequencyService);
  protected trademarkTokenFrequencyFormService = inject(TrademarkTokenFrequencyFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TrademarkTokenFrequencyFormGroup = this.trademarkTokenFrequencyFormService.createTrademarkTokenFrequencyFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ trademarkTokenFrequency }) => {
      this.trademarkTokenFrequency = trademarkTokenFrequency;
      if (trademarkTokenFrequency) {
        this.updateForm(trademarkTokenFrequency);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const trademarkTokenFrequency = this.trademarkTokenFrequencyFormService.getTrademarkTokenFrequency(this.editForm);
    if (trademarkTokenFrequency.id !== null) {
      this.subscribeToSaveResponse(this.trademarkTokenFrequencyService.update(trademarkTokenFrequency));
    } else {
      this.subscribeToSaveResponse(this.trademarkTokenFrequencyService.create(trademarkTokenFrequency));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITrademarkTokenFrequency>>): void {
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

  protected updateForm(trademarkTokenFrequency: ITrademarkTokenFrequency): void {
    this.trademarkTokenFrequency = trademarkTokenFrequency;
    this.trademarkTokenFrequencyFormService.resetForm(this.editForm, trademarkTokenFrequency);
  }
}
