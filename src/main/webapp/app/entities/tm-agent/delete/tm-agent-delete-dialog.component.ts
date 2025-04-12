import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITmAgent } from '../tm-agent.model';
import { TmAgentService } from '../service/tm-agent.service';

@Component({
  templateUrl: './tm-agent-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TmAgentDeleteDialogComponent {
  tmAgent?: ITmAgent;

  protected tmAgentService = inject(TmAgentService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tmAgentService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
