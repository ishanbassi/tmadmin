import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ITrademarkTokenFrequency } from '../trademark-token-frequency.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../trademark-token-frequency.test-samples';

import { RestTrademarkTokenFrequency, TrademarkTokenFrequencyService } from './trademark-token-frequency.service';

const requireRestSample: RestTrademarkTokenFrequency = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  modifiedDate: sampleWithRequiredData.modifiedDate?.toJSON(),
};

describe('TrademarkTokenFrequency Service', () => {
  let service: TrademarkTokenFrequencyService;
  let httpMock: HttpTestingController;
  let expectedResult: ITrademarkTokenFrequency | ITrademarkTokenFrequency[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(TrademarkTokenFrequencyService);
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

    it('should create a TrademarkTokenFrequency', () => {
      const trademarkTokenFrequency = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(trademarkTokenFrequency).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TrademarkTokenFrequency', () => {
      const trademarkTokenFrequency = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(trademarkTokenFrequency).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TrademarkTokenFrequency', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TrademarkTokenFrequency', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TrademarkTokenFrequency', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTrademarkTokenFrequencyToCollectionIfMissing', () => {
      it('should add a TrademarkTokenFrequency to an empty array', () => {
        const trademarkTokenFrequency: ITrademarkTokenFrequency = sampleWithRequiredData;
        expectedResult = service.addTrademarkTokenFrequencyToCollectionIfMissing([], trademarkTokenFrequency);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(trademarkTokenFrequency);
      });

      it('should not add a TrademarkTokenFrequency to an array that contains it', () => {
        const trademarkTokenFrequency: ITrademarkTokenFrequency = sampleWithRequiredData;
        const trademarkTokenFrequencyCollection: ITrademarkTokenFrequency[] = [
          {
            ...trademarkTokenFrequency,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTrademarkTokenFrequencyToCollectionIfMissing(
          trademarkTokenFrequencyCollection,
          trademarkTokenFrequency,
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TrademarkTokenFrequency to an array that doesn't contain it", () => {
        const trademarkTokenFrequency: ITrademarkTokenFrequency = sampleWithRequiredData;
        const trademarkTokenFrequencyCollection: ITrademarkTokenFrequency[] = [sampleWithPartialData];
        expectedResult = service.addTrademarkTokenFrequencyToCollectionIfMissing(
          trademarkTokenFrequencyCollection,
          trademarkTokenFrequency,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(trademarkTokenFrequency);
      });

      it('should add only unique TrademarkTokenFrequency to an array', () => {
        const trademarkTokenFrequencyArray: ITrademarkTokenFrequency[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const trademarkTokenFrequencyCollection: ITrademarkTokenFrequency[] = [sampleWithRequiredData];
        expectedResult = service.addTrademarkTokenFrequencyToCollectionIfMissing(
          trademarkTokenFrequencyCollection,
          ...trademarkTokenFrequencyArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const trademarkTokenFrequency: ITrademarkTokenFrequency = sampleWithRequiredData;
        const trademarkTokenFrequency2: ITrademarkTokenFrequency = sampleWithPartialData;
        expectedResult = service.addTrademarkTokenFrequencyToCollectionIfMissing([], trademarkTokenFrequency, trademarkTokenFrequency2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(trademarkTokenFrequency);
        expect(expectedResult).toContain(trademarkTokenFrequency2);
      });

      it('should accept null and undefined values', () => {
        const trademarkTokenFrequency: ITrademarkTokenFrequency = sampleWithRequiredData;
        expectedResult = service.addTrademarkTokenFrequencyToCollectionIfMissing([], null, trademarkTokenFrequency, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(trademarkTokenFrequency);
      });

      it('should return initial array if no TrademarkTokenFrequency is added', () => {
        const trademarkTokenFrequencyCollection: ITrademarkTokenFrequency[] = [sampleWithRequiredData];
        expectedResult = service.addTrademarkTokenFrequencyToCollectionIfMissing(trademarkTokenFrequencyCollection, undefined, null);
        expect(expectedResult).toEqual(trademarkTokenFrequencyCollection);
      });
    });

    describe('compareTrademarkTokenFrequency', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTrademarkTokenFrequency(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 15292 };
        const entity2 = null;

        const compareResult1 = service.compareTrademarkTokenFrequency(entity1, entity2);
        const compareResult2 = service.compareTrademarkTokenFrequency(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 15292 };
        const entity2 = { id: 28547 };

        const compareResult1 = service.compareTrademarkTokenFrequency(entity1, entity2);
        const compareResult2 = service.compareTrademarkTokenFrequency(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 15292 };
        const entity2 = { id: 15292 };

        const compareResult1 = service.compareTrademarkTokenFrequency(entity1, entity2);
        const compareResult2 = service.compareTrademarkTokenFrequency(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
