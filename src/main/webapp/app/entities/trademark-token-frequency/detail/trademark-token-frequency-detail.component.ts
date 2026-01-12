import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { ITrademarkTokenFrequency } from '../trademark-token-frequency.model';

@Component({
  selector: 'jhi-trademark-token-frequency-detail',
  templateUrl: './trademark-token-frequency-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class TrademarkTokenFrequencyDetailComponent {
  trademarkTokenFrequency = input<ITrademarkTokenFrequency | null>(null);

  previousState(): void {
    window.history.back();
  }
}
