import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IDocuments } from '../documents.model';
import { DocumentsService } from '../service/documents.service';

@Component({
  templateUrl: './documents-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class DocumentsDeleteDialogComponent {
  documents?: IDocuments;

  protected documentsService = inject(DocumentsService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.documentsService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
