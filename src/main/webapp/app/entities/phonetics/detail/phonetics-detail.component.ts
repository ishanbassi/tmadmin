import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IPhonetics } from '../phonetics.model';

@Component({
  standalone: true,
  selector: 'jhi-phonetics-detail',
  templateUrl: './phonetics-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class PhoneticsDetailComponent {
  phonetics = input<IPhonetics | null>(null);

  previousState(): void {
    window.history.back();
  }
}
