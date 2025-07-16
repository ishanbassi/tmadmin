import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { ITrademarkClass } from '../trademark-class.model';

@Component({
  selector: 'jhi-trademark-class-detail',
  templateUrl: './trademark-class-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class TrademarkClassDetailComponent {
  trademarkClass = input<ITrademarkClass | null>(null);

  previousState(): void {
    window.history.back();
  }
}
