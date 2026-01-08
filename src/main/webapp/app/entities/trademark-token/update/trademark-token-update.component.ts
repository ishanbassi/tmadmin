import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITrademark } from 'app/entities/trademark/trademark.model';
import { TrademarkService } from 'app/entities/trademark/service/trademark.service';
import { TrademarkTokenType } from 'app/entities/enumerations/trademark-token-type.model';
import { TrademarkTokenService } from '../service/trademark-token.service';
import { ITrademarkToken } from '../trademark-token.model';
import { TrademarkTokenFormGroup, TrademarkTokenFormService } from './trademark-token-form.service';

@Component({
  selector: 'jhi-trademark-token-update',
  templateUrl: './trademark-token-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TrademarkTokenUpdateComponent implements OnInit {
  isSaving = false;
  trademarkToken: ITrademarkToken | null = null;
  trademarkTokenTypeValues = Object.keys(TrademarkTokenType);

  trademarksSharedCollection: ITrademark[] = [];

  protected trademarkTokenService = inject(TrademarkTokenService);
  protected trademarkTokenFormService = inject(TrademarkTokenFormService);
  protected trademarkService = inject(TrademarkService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TrademarkTokenFormGroup = this.trademarkTokenFormService.createTrademarkTokenFormGroup();

  compareTrademark = (o1: ITrademark | null, o2: ITrademark | null): boolean => this.trademarkService.compareTrademark(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ trademarkToken }) => {
      this.trademarkToken = trademarkToken;
      if (trademarkToken) {
        this.updateForm(trademarkToken);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const trademarkToken = this.trademarkTokenFormService.getTrademarkToken(this.editForm);
    if (trademarkToken.id !== null) {
      this.subscribeToSaveResponse(this.trademarkTokenService.update(trademarkToken));
    } else {
      this.subscribeToSaveResponse(this.trademarkTokenService.create(trademarkToken));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITrademarkToken>>): void {
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

  protected updateForm(trademarkToken: ITrademarkToken): void {
    this.trademarkToken = trademarkToken;
    this.trademarkTokenFormService.resetForm(this.editForm, trademarkToken);

    this.trademarksSharedCollection = this.trademarkService.addTrademarkToCollectionIfMissing<ITrademark>(
      this.trademarksSharedCollection,
      trademarkToken.trademark,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.trademarkService
      .query()
      .pipe(map((res: HttpResponse<ITrademark[]>) => res.body ?? []))
      .pipe(
        map((trademarks: ITrademark[]) =>
          this.trademarkService.addTrademarkToCollectionIfMissing<ITrademark>(trademarks, this.trademarkToken?.trademark),
        ),
      )
      .subscribe((trademarks: ITrademark[]) => (this.trademarksSharedCollection = trademarks));
  }
}
