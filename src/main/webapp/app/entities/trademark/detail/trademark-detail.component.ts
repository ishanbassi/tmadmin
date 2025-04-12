import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ITrademark } from '../trademark.model';

@Component({
  selector: 'jhi-trademark-detail',
  templateUrl: './trademark-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class TrademarkDetailComponent {
  trademark = input<ITrademark | null>(null);

  previousState(): void {
    window.history.back();
  }
}
