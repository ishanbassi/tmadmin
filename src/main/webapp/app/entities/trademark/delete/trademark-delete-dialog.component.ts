import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITrademark } from '../trademark.model';
import { TrademarkService } from '../service/trademark.service';

@Component({
  standalone: true,
  templateUrl: './trademark-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TrademarkDeleteDialogComponent {
  trademark?: ITrademark;

  protected trademarkService = inject(TrademarkService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.trademarkService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
