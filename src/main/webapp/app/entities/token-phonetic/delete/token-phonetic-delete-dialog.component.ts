import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITokenPhonetic } from '../token-phonetic.model';
import { TokenPhoneticService } from '../service/token-phonetic.service';

@Component({
  templateUrl: './token-phonetic-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TokenPhoneticDeleteDialogComponent {
  tokenPhonetic?: ITokenPhonetic;

  protected tokenPhoneticService = inject(TokenPhoneticService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tokenPhoneticService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
