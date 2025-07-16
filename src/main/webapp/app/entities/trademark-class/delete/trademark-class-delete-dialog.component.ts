import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITrademarkClass } from '../trademark-class.model';
import { TrademarkClassService } from '../service/trademark-class.service';

@Component({
  templateUrl: './trademark-class-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TrademarkClassDeleteDialogComponent {
  trademarkClass?: ITrademarkClass;

  protected trademarkClassService = inject(TrademarkClassService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.trademarkClassService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
