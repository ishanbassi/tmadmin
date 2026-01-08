import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { ITrademarkToken } from '../trademark-token.model';

@Component({
  selector: 'jhi-trademark-token-detail',
  templateUrl: './trademark-token-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class TrademarkTokenDetailComponent {
  trademarkToken = input<ITrademarkToken | null>(null);

  previousState(): void {
    window.history.back();
  }
}
