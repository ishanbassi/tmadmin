import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITrademarkPlan } from '../trademark-plan.model';
import { TrademarkPlanService } from '../service/trademark-plan.service';

@Component({
  templateUrl: './trademark-plan-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TrademarkPlanDeleteDialogComponent {
  trademarkPlan?: ITrademarkPlan;

  protected trademarkPlanService = inject(TrademarkPlanService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.trademarkPlanService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
