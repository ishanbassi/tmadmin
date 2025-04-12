import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { ITmAgent } from '../tm-agent.model';

@Component({
  selector: 'jhi-tm-agent-detail',
  templateUrl: './tm-agent-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class TmAgentDetailComponent {
  tmAgent = input<ITmAgent | null>(null);

  previousState(): void {
    window.history.back();
  }
}
