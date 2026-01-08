import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { ITokenPhonetic } from '../token-phonetic.model';

@Component({
  selector: 'jhi-token-phonetic-detail',
  templateUrl: './token-phonetic-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class TokenPhoneticDetailComponent {
  tokenPhonetic = input<ITokenPhonetic | null>(null);

  previousState(): void {
    window.history.back();
  }
}
