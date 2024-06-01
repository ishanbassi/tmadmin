import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';

import { IPublishedTmPhonetics } from '../published-tm-phonetics.model';
import { PublishedTmPhoneticsService } from '../service/published-tm-phonetics.service';

import publishedTmPhoneticsResolve from './published-tm-phonetics-routing-resolve.service';

describe('PublishedTmPhonetics routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: PublishedTmPhoneticsService;
  let resultPublishedTmPhonetics: IPublishedTmPhonetics | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
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
    service = TestBed.inject(PublishedTmPhoneticsService);
    resultPublishedTmPhonetics = undefined;
  });

  describe('resolve', () => {
    it('should return IPublishedTmPhonetics returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        publishedTmPhoneticsResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultPublishedTmPhonetics = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPublishedTmPhonetics).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        publishedTmPhoneticsResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultPublishedTmPhonetics = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultPublishedTmPhonetics).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IPublishedTmPhonetics>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        publishedTmPhoneticsResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultPublishedTmPhonetics = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPublishedTmPhonetics).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
