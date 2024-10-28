import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IPublishedTmPhonetics } from '../published-tm-phonetics.model';

@Component({
  standalone: true,
  selector: 'jhi-published-tm-phonetics-detail',
  templateUrl: './published-tm-phonetics-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class PublishedTmPhoneticsDetailComponent {
  publishedTmPhonetics = input<IPublishedTmPhonetics | null>(null);

  previousState(): void {
    window.history.back();
  }
}
