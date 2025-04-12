import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPublishedTm } from '../published-tm.model';
import { PublishedTmService } from '../service/published-tm.service';

@Component({
  templateUrl: './published-tm-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PublishedTmDeleteDialogComponent {
  publishedTm?: IPublishedTm;

  protected publishedTmService = inject(PublishedTmService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.publishedTmService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
