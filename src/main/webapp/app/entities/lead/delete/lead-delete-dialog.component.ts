import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ILead } from '../lead.model';
import { LeadService } from '../service/lead.service';

@Component({
  templateUrl: './lead-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class LeadDeleteDialogComponent {
  lead?: ILead;

  protected leadService = inject(LeadService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.leadService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
