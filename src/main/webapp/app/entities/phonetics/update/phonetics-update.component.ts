import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITrademark } from 'app/entities/trademark/trademark.model';
import { TrademarkService } from 'app/entities/trademark/service/trademark.service';
import { IPhonetics } from '../phonetics.model';
import { PhoneticsService } from '../service/phonetics.service';
import { PhoneticsFormGroup, PhoneticsFormService } from './phonetics-form.service';

@Component({
  standalone: true,
  selector: 'jhi-phonetics-update',
  templateUrl: './phonetics-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PhoneticsUpdateComponent implements OnInit {
  isSaving = false;
  phonetics: IPhonetics | null = null;

  trademarksSharedCollection: ITrademark[] = [];

  protected phoneticsService = inject(PhoneticsService);
  protected phoneticsFormService = inject(PhoneticsFormService);
  protected trademarkService = inject(TrademarkService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PhoneticsFormGroup = this.phoneticsFormService.createPhoneticsFormGroup();

  compareTrademark = (o1: ITrademark | null, o2: ITrademark | null): boolean => this.trademarkService.compareTrademark(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ phonetics }) => {
      this.phonetics = phonetics;
      if (phonetics) {
        this.updateForm(phonetics);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const phonetics = this.phoneticsFormService.getPhonetics(this.editForm);
    if (phonetics.id !== null) {
      this.subscribeToSaveResponse(this.phoneticsService.update(phonetics));
    } else {
      this.subscribeToSaveResponse(this.phoneticsService.create(phonetics));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPhonetics>>): void {
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

  protected updateForm(phonetics: IPhonetics): void {
    this.phonetics = phonetics;
    this.phoneticsFormService.resetForm(this.editForm, phonetics);

    this.trademarksSharedCollection = this.trademarkService.addTrademarkToCollectionIfMissing<ITrademark>(
      this.trademarksSharedCollection,
      phonetics.trademark,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.trademarkService
      .query()
      .pipe(map((res: HttpResponse<ITrademark[]>) => res.body ?? []))
      .pipe(
        map((trademarks: ITrademark[]) =>
          this.trademarkService.addTrademarkToCollectionIfMissing<ITrademark>(trademarks, this.phonetics?.trademark),
        ),
      )
      .subscribe((trademarks: ITrademark[]) => (this.trademarksSharedCollection = trademarks));
  }
}
