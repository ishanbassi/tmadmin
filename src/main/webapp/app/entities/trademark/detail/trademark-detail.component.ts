import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ITrademark } from '../trademark.model';

@Component({
  standalone: true,
  selector: 'jhi-trademark-detail',
  templateUrl: './trademark-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class TrademarkDetailComponent {
  trademark = input<ITrademark | null>(null);

  previousState(): void {
    window.history.back();
  }
}
