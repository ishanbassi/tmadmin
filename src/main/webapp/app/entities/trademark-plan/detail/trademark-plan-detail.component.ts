import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { ITrademarkPlan } from '../trademark-plan.model';

@Component({
  selector: 'jhi-trademark-plan-detail',
  templateUrl: './trademark-plan-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class TrademarkPlanDetailComponent {
  trademarkPlan = input<ITrademarkPlan | null>(null);

  previousState(): void {
    window.history.back();
  }
}
