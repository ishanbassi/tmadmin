import { Component, computed, NgZone, inject, OnInit, signal, WritableSignal } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router, RouterModule } from '@angular/router';
import { combineLatest, filter, Observable, Subscription, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { sortStateSignal, SortDirective, SortByDirective, type SortState, SortService } from 'app/shared/sort';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { FormsModule } from '@angular/forms';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { SORT, ITEM_DELETED_EVENT, DEFAULT_SORT_DATA } from 'app/config/navigation.constants';
import { ParseLinks } from 'app/core/util/parse-links.service';
import { InfiniteScrollDirective } from 'ngx-infinite-scroll';
import { EntityArrayResponseType, PublishedTmService } from '../service/published-tm.service';
import { PublishedTmDeleteDialogComponent } from '../delete/published-tm-delete-dialog.component';
import { IPublishedTm } from '../published-tm.model';
import FilterComponent from '../../../shared/filter/filter.component';
import { FilterOption, FilterOptions } from '../../../shared/filter/filter.model';
import { saveAs } from 'file-saver';


@Component({
  standalone: true,
  selector: 'jhi-published-tm',
  templateUrl: './published-tm.component.html',
  imports: [
    RouterModule,
    FormsModule,
    SharedModule,
    SortDirective,
    SortByDirective,
    DurationPipe,
    FormatMediumDatetimePipe,
    FormatMediumDatePipe,
    InfiniteScrollDirective,
    FilterComponent
  ],
})
export class PublishedTmComponent implements OnInit {

  subscription: Subscription | null = null;
  publishedTms?: IPublishedTm[];
  isLoading = false;
  filters = new FilterOptions()

  sortState = sortStateSignal({});

  itemsPerPage = ITEMS_PER_PAGE;
  links: WritableSignal<{ [key: string]: undefined | { [key: string]: string | undefined } }> = signal({});
  hasMorePage = computed(() => !!this.links().next);
  isFirstFetch = computed(() => Object.keys(this.links()).length === 0);

  public router = inject(Router);
  protected publishedTmService = inject(PublishedTmService);
  protected activatedRoute = inject(ActivatedRoute);
  protected sortService = inject(SortService);
  protected parseLinks = inject(ParseLinks);
  protected modalService = inject(NgbModal);
  protected ngZone = inject(NgZone);

  trackId = (_index: number, item: IPublishedTm): number => this.publishedTmService.getPublishedTmIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => this.reset()),
        tap(() => this.load()),
      )
      .subscribe();
  }

  reset(): void {
    this.publishedTms = [];
  }

  loadNextPage(): void {
    this.load();
  }

  delete(publishedTm: IPublishedTm): void {
    const modalRef = this.modalService.open(PublishedTmDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.publishedTm = publishedTm;
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
    this.fillComponentAttributesFromResponseHeader(response.headers);
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.publishedTms = dataFromBody;
  }

  protected fillComponentAttributesFromResponseBody(data: IPublishedTm[] | null): IPublishedTm[] {
    // If there is previous link, data is a infinite scroll pagination content.
    if (this.links().prev) {
      const publishedTmsNew = this.publishedTms ?? [];
      if (data) {
        for (const d of data) {
          if (publishedTmsNew.map(op => op.id).indexOf(d.id) === -1) {
            publishedTmsNew.push(d);
          }
        }
      }
      return publishedTmsNew;
    }
    return data ?? [];
  }

  protected fillComponentAttributesFromResponseHeader(headers: HttpHeaders): void {
    const linkHeader = headers.get('link');
    if (linkHeader) {
      this.links.set(this.parseLinks.parseAll(linkHeader));
    } else {
      this.links.set({});
    }
  }

  protected queryBackend(): Observable<EntityArrayResponseType> {
    this.isLoading = true;
    const queryObject: any = {
      size: this.itemsPerPage,
    };
    if (this.hasMorePage()) {
      Object.assign(queryObject, this.links().next);
    } else if (this.isFirstFetch()) {
      Object.assign(queryObject, { sort: this.sortService.buildSortParam(this.sortState()) });
    }

    return this.publishedTmService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
  }

  protected handleNavigation(sortState: SortState): void {
    this.links.set({});

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

  download() {
    this.downloadBackend().subscribe({
      next: (res: any) => {
        let fileName: any = res.headers.get('content-disposition');
        fileName = fileName.replace('form-data; name="attachment"; filename="', '');
        fileName = fileName.split('"').join("");
        saveAs(res.body, fileName);
      },
      error: (error) => {
        
      }
    })
  }

  protected downloadBackend(): Observable<HttpResponse<Blob>> {
    this.isLoading = true;

    return this.publishedTmService.download().pipe(tap(() => (this.isLoading = false)));
  }
}
