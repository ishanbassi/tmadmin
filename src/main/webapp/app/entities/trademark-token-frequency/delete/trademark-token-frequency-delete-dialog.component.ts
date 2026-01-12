import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITrademarkTokenFrequency } from '../trademark-token-frequency.model';
import { TrademarkTokenFrequencyService } from '../service/trademark-token-frequency.service';

@Component({
  templateUrl: './trademark-token-frequency-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TrademarkTokenFrequencyDeleteDialogComponent {
  trademarkTokenFrequency?: ITrademarkTokenFrequency;

  protected trademarkTokenFrequencyService = inject(TrademarkTokenFrequencyService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.trademarkTokenFrequencyService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
