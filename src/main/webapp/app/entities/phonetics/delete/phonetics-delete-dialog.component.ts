import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPhonetics } from '../phonetics.model';
import { PhoneticsService } from '../service/phonetics.service';

@Component({
  standalone: true,
  templateUrl: './phonetics-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PhoneticsDeleteDialogComponent {
  phonetics?: IPhonetics;

  protected phoneticsService = inject(PhoneticsService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.phoneticsService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
