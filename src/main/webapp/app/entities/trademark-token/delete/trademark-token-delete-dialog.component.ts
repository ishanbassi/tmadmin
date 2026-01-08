import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITrademarkToken } from '../trademark-token.model';
import { TrademarkTokenService } from '../service/trademark-token.service';

@Component({
  templateUrl: './trademark-token-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TrademarkTokenDeleteDialogComponent {
  trademarkToken?: ITrademarkToken;

  protected trademarkTokenService = inject(TrademarkTokenService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.trademarkTokenService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
