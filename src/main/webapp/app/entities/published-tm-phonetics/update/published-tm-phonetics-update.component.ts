import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPublishedTm } from 'app/entities/published-tm/published-tm.model';
import { PublishedTmService } from 'app/entities/published-tm/service/published-tm.service';
import { IPublishedTmPhonetics } from '../published-tm-phonetics.model';
import { PublishedTmPhoneticsService } from '../service/published-tm-phonetics.service';
import { PublishedTmPhoneticsFormGroup, PublishedTmPhoneticsFormService } from './published-tm-phonetics-form.service';

@Component({
  standalone: true,
  selector: 'jhi-published-tm-phonetics-update',
  templateUrl: './published-tm-phonetics-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PublishedTmPhoneticsUpdateComponent implements OnInit {
  isSaving = false;
  publishedTmPhonetics: IPublishedTmPhonetics | null = null;

  publishedTmsSharedCollection: IPublishedTm[] = [];

  protected publishedTmPhoneticsService = inject(PublishedTmPhoneticsService);
  protected publishedTmPhoneticsFormService = inject(PublishedTmPhoneticsFormService);
  protected publishedTmService = inject(PublishedTmService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PublishedTmPhoneticsFormGroup = this.publishedTmPhoneticsFormService.createPublishedTmPhoneticsFormGroup();

  comparePublishedTm = (o1: IPublishedTm | null, o2: IPublishedTm | null): boolean => this.publishedTmService.comparePublishedTm(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ publishedTmPhonetics }) => {
      this.publishedTmPhonetics = publishedTmPhonetics;
      if (publishedTmPhonetics) {
        this.updateForm(publishedTmPhonetics);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const publishedTmPhonetics = this.publishedTmPhoneticsFormService.getPublishedTmPhonetics(this.editForm);
    if (publishedTmPhonetics.id !== null) {
      this.subscribeToSaveResponse(this.publishedTmPhoneticsService.update(publishedTmPhonetics));
    } else {
      this.subscribeToSaveResponse(this.publishedTmPhoneticsService.create(publishedTmPhonetics));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPublishedTmPhonetics>>): void {
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

  protected updateForm(publishedTmPhonetics: IPublishedTmPhonetics): void {
    this.publishedTmPhonetics = publishedTmPhonetics;
    this.publishedTmPhoneticsFormService.resetForm(this.editForm, publishedTmPhonetics);

    this.publishedTmsSharedCollection = this.publishedTmService.addPublishedTmToCollectionIfMissing<IPublishedTm>(
      this.publishedTmsSharedCollection,
      publishedTmPhonetics.publishedTm,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.publishedTmService
      .query()
      .pipe(map((res: HttpResponse<IPublishedTm[]>) => res.body ?? []))
      .pipe(
        map((publishedTms: IPublishedTm[]) =>
          this.publishedTmService.addPublishedTmToCollectionIfMissing<IPublishedTm>(publishedTms, this.publishedTmPhonetics?.publishedTm),
        ),
      )
      .subscribe((publishedTms: IPublishedTm[]) => (this.publishedTmsSharedCollection = publishedTms));
  }
}
