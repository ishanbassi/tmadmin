import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IUserEventsTracking } from '../user-events-tracking.model';
import { UserEventsTrackingService } from '../service/user-events-tracking.service';

@Component({
  templateUrl: './user-events-tracking-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class UserEventsTrackingDeleteDialogComponent {
  userEventsTracking?: IUserEventsTracking;

  protected userEventsTrackingService = inject(UserEventsTrackingService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.userEventsTrackingService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
