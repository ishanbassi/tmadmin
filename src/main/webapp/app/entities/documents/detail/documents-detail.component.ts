import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IDocuments } from '../documents.model';

@Component({
  selector: 'jhi-documents-detail',
  templateUrl: './documents-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class DocumentsDetailComponent {
  documents = input<IDocuments | null>(null);

  previousState(): void {
    window.history.back();
  }
}
