import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { ICompany } from '../company.model';

@Component({
  selector: 'jhi-company-detail',
  templateUrl: './company-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class CompanyDetailComponent {
  company = input<ICompany | null>(null);

  previousState(): void {
    window.history.back();
  }
}
