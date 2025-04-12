import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ITmAgent } from '../tm-agent.model';

@Component({
  standalone: true,
  selector: 'jhi-tm-agent-detail',
  templateUrl: './tm-agent-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class TmAgentDetailComponent {
  tmAgent = input<ITmAgent | null>(null);

  previousState(): void {
    window.history.back();
  }
}
