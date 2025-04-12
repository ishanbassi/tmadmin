import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPublishedTmPhonetics } from '../published-tm-phonetics.model';
import { PublishedTmPhoneticsService } from '../service/published-tm-phonetics.service';

@Component({
  templateUrl: './published-tm-phonetics-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PublishedTmPhoneticsDeleteDialogComponent {
  publishedTmPhonetics?: IPublishedTmPhonetics;

  protected publishedTmPhoneticsService = inject(PublishedTmPhoneticsService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.publishedTmPhoneticsService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
