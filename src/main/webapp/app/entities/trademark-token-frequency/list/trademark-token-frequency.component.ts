import { Component, NgZone, OnInit, inject, signal } from '@angular/core';
import { ActivatedRoute, Data, ParamMap, Router, RouterModule } from '@angular/router';
import { Observable, Subscription, combineLatest, filter, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { SortByDirective, SortDirective, SortService, type SortState, sortStateSignal } from 'app/shared/sort';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { FormsModule } from '@angular/forms';
import { DEFAULT_SORT_DATA, ITEM_DELETED_EVENT, SORT } from 'app/config/navigation.constants';
import { ITrademarkTokenFrequency } from '../trademark-token-frequency.model';
import { EntityArrayResponseType, TrademarkTokenFrequencyService } from '../service/trademark-token-frequency.service';
import { TrademarkTokenFrequencyDeleteDialogComponent } from '../delete/trademark-token-frequency-delete-dialog.component';

@Component({
  selector: 'jhi-trademark-token-frequency',
  templateUrl: './trademark-token-frequency.component.html',
  imports: [RouterModule, FormsModule, SharedModule, SortDirective, SortByDirective, FormatMediumDatetimePipe],
})
export class TrademarkTokenFrequencyComponent implements OnInit {
  subscription: Subscription | null = null;
  trademarkTokenFrequencies = signal<ITrademarkTokenFrequency[]>([]);
  isLoading = false;

  sortState = sortStateSignal({});

  public readonly router = inject(Router);
  protected readonly trademarkTokenFrequencyService = inject(TrademarkTokenFrequencyService);
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly sortService = inject(SortService);
  protected modalService = inject(NgbModal);
  protected ngZone = inject(NgZone);

  trackId = (item: ITrademarkTokenFrequency): number => this.trademarkTokenFrequencyService.getTrademarkTokenFrequencyIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => {
          if (this.trademarkTokenFrequencies().length === 0) {
            this.load();
          } else {
            this.trademarkTokenFrequencies.set(this.refineData(this.trademarkTokenFrequencies()));
          }
        }),
      )
      .subscribe();
  }

  delete(trademarkTokenFrequency: ITrademarkTokenFrequency): void {
    const modalRef = this.modalService.open(TrademarkTokenFrequencyDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.trademarkTokenFrequency = trademarkTokenFrequency;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        tap(() => this.load()),
      )
      .subscribe();
  }

  load(): void {
    this.queryBackend().subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
      },
    });
  }

  navigateToWithComponentValues(event: SortState): void {
    this.handleNavigation(event);
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    this.sortState.set(this.sortService.parseSortParam(params.get(SORT) ?? data[DEFAULT_SORT_DATA]));
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.trademarkTokenFrequencies.set(this.refineData(dataFromBody));
  }

  protected refineData(data: ITrademarkTokenFrequency[]): ITrademarkTokenFrequency[] {
    const { predicate, order } = this.sortState();
    return predicate && order ? data.sort(this.sortService.startSort({ predicate, order })) : data;
  }

  protected fillComponentAttributesFromResponseBody(data: ITrademarkTokenFrequency[] | null): ITrademarkTokenFrequency[] {
    return data ?? [];
  }

  protected queryBackend(): Observable<EntityArrayResponseType> {
    this.isLoading = true;
    const queryObject: any = {
      sort: this.sortService.buildSortParam(this.sortState()),
    };
    return this.trademarkTokenFrequencyService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
  }

  protected handleNavigation(sortState: SortState): void {
    const queryParamsObj = {
      sort: this.sortService.buildSortParam(sortState),
    };

    this.ngZone.run(() => {
      this.router.navigate(['./'], {
        relativeTo: this.activatedRoute,
        queryParams: queryParamsObj,
      });
    });
  }
}
