import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IPublishedTm } from '../published-tm.model';

@Component({
  standalone: true,
  selector: 'jhi-published-tm-detail',
  templateUrl: './published-tm-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class PublishedTmDetailComponent {
  publishedTm = input<IPublishedTm | null>(null);

  previousState(): void {
    window.history.back();
  }
}
