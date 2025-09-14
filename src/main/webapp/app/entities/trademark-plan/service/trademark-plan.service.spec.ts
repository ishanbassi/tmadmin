import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ITrademarkPlan } from '../trademark-plan.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../trademark-plan.test-samples';

import { RestTrademarkPlan, TrademarkPlanService } from './trademark-plan.service';

const requireRestSample: RestTrademarkPlan = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  modifiedDate: sampleWithRequiredData.modifiedDate?.toJSON(),
};

describe('TrademarkPlan Service', () => {
  let service: TrademarkPlanService;
  let httpMock: HttpTestingController;
  let expectedResult: ITrademarkPlan | ITrademarkPlan[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(TrademarkPlanService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a TrademarkPlan', () => {
      const trademarkPlan = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(trademarkPlan).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TrademarkPlan', () => {
      const trademarkPlan = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(trademarkPlan).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TrademarkPlan', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TrademarkPlan', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TrademarkPlan', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTrademarkPlanToCollectionIfMissing', () => {
      it('should add a TrademarkPlan to an empty array', () => {
        const trademarkPlan: ITrademarkPlan = sampleWithRequiredData;
        expectedResult = service.addTrademarkPlanToCollectionIfMissing([], trademarkPlan);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(trademarkPlan);
      });

      it('should not add a TrademarkPlan to an array that contains it', () => {
        const trademarkPlan: ITrademarkPlan = sampleWithRequiredData;
        const trademarkPlanCollection: ITrademarkPlan[] = [
          {
            ...trademarkPlan,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTrademarkPlanToCollectionIfMissing(trademarkPlanCollection, trademarkPlan);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TrademarkPlan to an array that doesn't contain it", () => {
        const trademarkPlan: ITrademarkPlan = sampleWithRequiredData;
        const trademarkPlanCollection: ITrademarkPlan[] = [sampleWithPartialData];
        expectedResult = service.addTrademarkPlanToCollectionIfMissing(trademarkPlanCollection, trademarkPlan);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(trademarkPlan);
      });

      it('should add only unique TrademarkPlan to an array', () => {
        const trademarkPlanArray: ITrademarkPlan[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const trademarkPlanCollection: ITrademarkPlan[] = [sampleWithRequiredData];
        expectedResult = service.addTrademarkPlanToCollectionIfMissing(trademarkPlanCollection, ...trademarkPlanArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const trademarkPlan: ITrademarkPlan = sampleWithRequiredData;
        const trademarkPlan2: ITrademarkPlan = sampleWithPartialData;
        expectedResult = service.addTrademarkPlanToCollectionIfMissing([], trademarkPlan, trademarkPlan2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(trademarkPlan);
        expect(expectedResult).toContain(trademarkPlan2);
      });

      it('should accept null and undefined values', () => {
        const trademarkPlan: ITrademarkPlan = sampleWithRequiredData;
        expectedResult = service.addTrademarkPlanToCollectionIfMissing([], null, trademarkPlan, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(trademarkPlan);
      });

      it('should return initial array if no TrademarkPlan is added', () => {
        const trademarkPlanCollection: ITrademarkPlan[] = [sampleWithRequiredData];
        expectedResult = service.addTrademarkPlanToCollectionIfMissing(trademarkPlanCollection, undefined, null);
        expect(expectedResult).toEqual(trademarkPlanCollection);
      });
    });

    describe('compareTrademarkPlan', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTrademarkPlan(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 27639 };
        const entity2 = null;

        const compareResult1 = service.compareTrademarkPlan(entity1, entity2);
        const compareResult2 = service.compareTrademarkPlan(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 27639 };
        const entity2 = { id: 23847 };

        const compareResult1 = service.compareTrademarkPlan(entity1, entity2);
        const compareResult2 = service.compareTrademarkPlan(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 27639 };
        const entity2 = { id: 27639 };

        const compareResult1 = service.compareTrademarkPlan(entity1, entity2);
        const compareResult2 = service.compareTrademarkPlan(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
