import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IPhonetics } from '../phonetics.model';

@Component({
  selector: 'jhi-phonetics-detail',
  templateUrl: './phonetics-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class PhoneticsDetailComponent {
  phonetics = input<IPhonetics | null>(null);

  previousState(): void {
    window.history.back();
  }
}
