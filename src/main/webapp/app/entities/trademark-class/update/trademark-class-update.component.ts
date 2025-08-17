import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITrademark } from 'app/entities/trademark/trademark.model';
import { TrademarkService } from 'app/entities/trademark/service/trademark.service';
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

  trademarksSharedCollection: ITrademark[] = [];

  protected trademarkClassService = inject(TrademarkClassService);
  protected trademarkClassFormService = inject(TrademarkClassFormService);
  protected trademarkService = inject(TrademarkService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TrademarkClassFormGroup = this.trademarkClassFormService.createTrademarkClassFormGroup();

  compareTrademark = (o1: ITrademark | null, o2: ITrademark | null): boolean => this.trademarkService.compareTrademark(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ trademarkClass }) => {
      this.trademarkClass = trademarkClass;
      if (trademarkClass) {
        this.updateForm(trademarkClass);
      }

      this.loadRelationshipsOptions();
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

    this.trademarksSharedCollection = this.trademarkService.addTrademarkToCollectionIfMissing<ITrademark>(
      this.trademarksSharedCollection,
      ...(trademarkClass.trademarks ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.trademarkService
      .query()
      .pipe(map((res: HttpResponse<ITrademark[]>) => res.body ?? []))
      .pipe(
        map((trademarks: ITrademark[]) =>
          this.trademarkService.addTrademarkToCollectionIfMissing<ITrademark>(trademarks, ...(this.trademarkClass?.trademarks ?? [])),
        ),
      )
      .subscribe((trademarks: ITrademark[]) => (this.trademarksSharedCollection = trademarks));
  }
}
