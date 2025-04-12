import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IPublishedTmPhonetics } from '../published-tm-phonetics.model';

@Component({
  selector: 'jhi-published-tm-phonetics-detail',
  templateUrl: './published-tm-phonetics-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class PublishedTmPhoneticsDetailComponent {
  publishedTmPhonetics = input<IPublishedTmPhonetics | null>(null);

  previousState(): void {
    window.history.back();
  }
}
