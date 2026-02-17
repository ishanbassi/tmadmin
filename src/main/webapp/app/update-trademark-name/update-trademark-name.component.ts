import { Component, inject, OnInit } from '@angular/core';
import { ITrademark } from '../entities/trademark/trademark.model';
import { Router } from '@angular/router';
import { TrademarkService } from '../entities/trademark/service/trademark.service';
import { ITEMS_PER_PAGE } from '../config/pagination.constants';
import { debounceTime, distinctUntilChanged, EMPTY, switchMap, tap } from 'rxjs';
import SharedModule from 'app/shared/shared.module';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { environment } from 'environments/environment';

@Component({
  selector: 'jhi-update-trademark-name',
  imports: [SharedModule, ReactiveFormsModule],
  templateUrl: './update-trademark-name.component.html',
  styleUrl: './update-trademark-name.component.scss',
})
export class UpdateTrademarkNameComponent implements OnInit {
  trademarks: ITrademark[] | null = [];
  journals: number[] = [];
  itemsPerPage = 5000;
  isLoading = false;
  selectedJournal?: number;
  baseApiUrl = environment.BASE_SERVER_URL;
  fb = inject(FormBuilder);

  form = this.fb.group({
    trademarks: this.fb.array([]),
  });

  get trademarkArray(): FormArray {
    return this.form.get('trademarks') as FormArray;
  }
  createTrademarkGroup(trademark: ITrademark): FormGroup {
    return this.fb.group({
      id: [trademark.id],
      name: [trademark.name],
    });
  }

  ngOnInit(): void {
    this.trademarkService
      .getJournalNumbers()
      .pipe(
        tap(journals => {
          this.journals = journals;
          this.selectedJournal = this.journals[0];
        }),
        switchMap(journal => {
          const queryObject: any = {
            size: this.itemsPerPage,
            eagerload: true,
            ['journalNo.equals']: this.selectedJournal,
            ['name.specified']: false,
          };
          return this.trademarkService.query(queryObject);
        }),
      )
      .pipe(
        tap(tms => tms.body?.forEach(tm => this.trademarkArray.push(this.createTrademarkGroup(tm)))),
        tap(() => {
          this.trademarkArray.controls.forEach(group => {
            group
              .get('name')
              ?.valueChanges.pipe(
                debounceTime(3000),
                distinctUntilChanged(),
                switchMap(value => {
                  if (group.dirty) {
                    return this.trademarkService.partialUpdate(group.value);
                  }
                  return EMPTY;
                }),
              )
              .subscribe(() => group.markAsPristine());
          });
        }),
      )
      .subscribe(tms => {
        this.trademarks = tms.body;
      });
  }

  public readonly router = inject(Router);
  protected readonly trademarkService = inject(TrademarkService);

  trackId = (item: ITrademark): number => this.trademarkService.getTrademarkIdentifier(item);
}
