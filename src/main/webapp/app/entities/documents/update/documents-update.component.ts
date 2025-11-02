import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITrademark } from 'app/entities/trademark/trademark.model';
import { TrademarkService } from 'app/entities/trademark/service/trademark.service';
import { DocumentType } from 'app/entities/enumerations/document-type.model';
import { DocumentStatus } from 'app/entities/enumerations/document-status.model';
import { DocumentsService } from '../service/documents.service';
import { IDocuments } from '../documents.model';
import { DocumentsFormGroup, DocumentsFormService } from './documents-form.service';

@Component({
  selector: 'jhi-documents-update',
  templateUrl: './documents-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DocumentsUpdateComponent implements OnInit {
  isSaving = false;
  documents: IDocuments | null = null;
  documentTypeValues = Object.keys(DocumentType);
  documentStatusValues = Object.keys(DocumentStatus);

  trademarksSharedCollection: ITrademark[] = [];

  protected documentsService = inject(DocumentsService);
  protected documentsFormService = inject(DocumentsFormService);
  protected trademarkService = inject(TrademarkService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DocumentsFormGroup = this.documentsFormService.createDocumentsFormGroup();

  compareTrademark = (o1: ITrademark | null, o2: ITrademark | null): boolean => this.trademarkService.compareTrademark(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documents }) => {
      this.documents = documents;
      if (documents) {
        this.updateForm(documents);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const documents = this.documentsFormService.getDocuments(this.editForm);
    if (documents.id !== null) {
      this.subscribeToSaveResponse(this.documentsService.update(documents));
    } else {
      this.subscribeToSaveResponse(this.documentsService.create(documents));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDocuments>>): void {
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

  protected updateForm(documents: IDocuments): void {
    this.documents = documents;
    this.documentsFormService.resetForm(this.editForm, documents);

    this.trademarksSharedCollection = this.trademarkService.addTrademarkToCollectionIfMissing<ITrademark>(
      this.trademarksSharedCollection,
      documents.trademark,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.trademarkService
      .query()
      .pipe(map((res: HttpResponse<ITrademark[]>) => res.body ?? []))
      .pipe(
        map((trademarks: ITrademark[]) =>
          this.trademarkService.addTrademarkToCollectionIfMissing<ITrademark>(trademarks, this.documents?.trademark),
        ),
      )
      .subscribe((trademarks: ITrademark[]) => (this.trademarksSharedCollection = trademarks));
  }
}
