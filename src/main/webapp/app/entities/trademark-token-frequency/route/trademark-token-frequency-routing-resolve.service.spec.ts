import { TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { ActivatedRoute, ActivatedRouteSnapshot, Router, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';

import { ITrademarkTokenFrequency } from '../trademark-token-frequency.model';
import { TrademarkTokenFrequencyService } from '../service/trademark-token-frequency.service';

import trademarkTokenFrequencyResolve from './trademark-token-frequency-routing-resolve.service';

describe('TrademarkTokenFrequency routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: TrademarkTokenFrequencyService;
  let resultTrademarkTokenFrequency: ITrademarkTokenFrequency | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    service = TestBed.inject(TrademarkTokenFrequencyService);
    resultTrademarkTokenFrequency = undefined;
  });

  describe('resolve', () => {
    it('should return ITrademarkTokenFrequency returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        trademarkTokenFrequencyResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultTrademarkTokenFrequency = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith(123);
      expect(resultTrademarkTokenFrequency).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        trademarkTokenFrequencyResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultTrademarkTokenFrequency = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toHaveBeenCalled();
      expect(resultTrademarkTokenFrequency).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<ITrademarkTokenFrequency>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        trademarkTokenFrequencyResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultTrademarkTokenFrequency = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith(123);
      expect(resultTrademarkTokenFrequency).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
