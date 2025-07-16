import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ITrademarkClass } from '../trademark-class.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../trademark-class.test-samples';

import { RestTrademarkClass, TrademarkClassService } from './trademark-class.service';

const requireRestSample: RestTrademarkClass = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  modifiedDate: sampleWithRequiredData.modifiedDate?.toJSON(),
};

describe('TrademarkClass Service', () => {
  let service: TrademarkClassService;
  let httpMock: HttpTestingController;
  let expectedResult: ITrademarkClass | ITrademarkClass[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(TrademarkClassService);
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

    it('should create a TrademarkClass', () => {
      const trademarkClass = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(trademarkClass).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TrademarkClass', () => {
      const trademarkClass = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(trademarkClass).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TrademarkClass', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TrademarkClass', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TrademarkClass', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTrademarkClassToCollectionIfMissing', () => {
      it('should add a TrademarkClass to an empty array', () => {
        const trademarkClass: ITrademarkClass = sampleWithRequiredData;
        expectedResult = service.addTrademarkClassToCollectionIfMissing([], trademarkClass);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(trademarkClass);
      });

      it('should not add a TrademarkClass to an array that contains it', () => {
        const trademarkClass: ITrademarkClass = sampleWithRequiredData;
        const trademarkClassCollection: ITrademarkClass[] = [
          {
            ...trademarkClass,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTrademarkClassToCollectionIfMissing(trademarkClassCollection, trademarkClass);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TrademarkClass to an array that doesn't contain it", () => {
        const trademarkClass: ITrademarkClass = sampleWithRequiredData;
        const trademarkClassCollection: ITrademarkClass[] = [sampleWithPartialData];
        expectedResult = service.addTrademarkClassToCollectionIfMissing(trademarkClassCollection, trademarkClass);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(trademarkClass);
      });

      it('should add only unique TrademarkClass to an array', () => {
        const trademarkClassArray: ITrademarkClass[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const trademarkClassCollection: ITrademarkClass[] = [sampleWithRequiredData];
        expectedResult = service.addTrademarkClassToCollectionIfMissing(trademarkClassCollection, ...trademarkClassArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const trademarkClass: ITrademarkClass = sampleWithRequiredData;
        const trademarkClass2: ITrademarkClass = sampleWithPartialData;
        expectedResult = service.addTrademarkClassToCollectionIfMissing([], trademarkClass, trademarkClass2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(trademarkClass);
        expect(expectedResult).toContain(trademarkClass2);
      });

      it('should accept null and undefined values', () => {
        const trademarkClass: ITrademarkClass = sampleWithRequiredData;
        expectedResult = service.addTrademarkClassToCollectionIfMissing([], null, trademarkClass, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(trademarkClass);
      });

      it('should return initial array if no TrademarkClass is added', () => {
        const trademarkClassCollection: ITrademarkClass[] = [sampleWithRequiredData];
        expectedResult = service.addTrademarkClassToCollectionIfMissing(trademarkClassCollection, undefined, null);
        expect(expectedResult).toEqual(trademarkClassCollection);
      });
    });

    describe('compareTrademarkClass', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTrademarkClass(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 17567 };
        const entity2 = null;

        const compareResult1 = service.compareTrademarkClass(entity1, entity2);
        const compareResult2 = service.compareTrademarkClass(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 17567 };
        const entity2 = { id: 29079 };

        const compareResult1 = service.compareTrademarkClass(entity1, entity2);
        const compareResult2 = service.compareTrademarkClass(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 17567 };
        const entity2 = { id: 17567 };

        const compareResult1 = service.compareTrademarkClass(entity1, entity2);
        const compareResult2 = service.compareTrademarkClass(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
