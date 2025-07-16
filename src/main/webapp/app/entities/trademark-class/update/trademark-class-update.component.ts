import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITrademarkClass } from '../trademark-class.model';
import { TrademarkClassService } from '../service/trademark-class.service';
import { TrademarkClassFormGroup, TrademarkClassFormService } from './trademark-class-form.service';

@Component({
  selector: 'jhi-trademark-class-update',
  templateUrl: './trademark-class-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TrademarkClassUpdateComponent implements OnInit {
  isSaving = false;
  trademarkClass: ITrademarkClass | null = null;

  protected trademarkClassService = inject(TrademarkClassService);
  protected trademarkClassFormService = inject(TrademarkClassFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TrademarkClassFormGroup = this.trademarkClassFormService.createTrademarkClassFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ trademarkClass }) => {
      this.trademarkClass = trademarkClass;
      if (trademarkClass) {
        this.updateForm(trademarkClass);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const trademarkClass = this.trademarkClassFormService.getTrademarkClass(this.editForm);
    if (trademarkClass.id !== null) {
      this.subscribeToSaveResponse(this.trademarkClassService.update(trademarkClass));
    } else {
      this.subscribeToSaveResponse(this.trademarkClassService.create(trademarkClass));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITrademarkClass>>): void {
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

  protected updateForm(trademarkClass: ITrademarkClass): void {
    this.trademarkClass = trademarkClass;
    this.trademarkClassFormService.resetForm(this.editForm, trademarkClass);
  }
}
