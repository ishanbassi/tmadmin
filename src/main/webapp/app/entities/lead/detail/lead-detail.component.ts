import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ILead } from '../lead.model';

@Component({
  standalone: true,
  selector: 'jhi-lead-detail',
  templateUrl: './lead-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class LeadDetailComponent {
  lead = input<ILead | null>(null);

  previousState(): void {
    window.history.back();
  }
}
