import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IUserEventsTracking } from '../user-events-tracking.model';

@Component({
  selector: 'jhi-user-events-tracking-detail',
  templateUrl: './user-events-tracking-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class UserEventsTrackingDetailComponent {
  userEventsTracking = input<IUserEventsTracking | null>(null);

  previousState(): void {
    window.history.back();
  }
}
