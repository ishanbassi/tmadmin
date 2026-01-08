import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ITrademarkToken } from '../trademark-token.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../trademark-token.test-samples';

import { TrademarkTokenService } from './trademark-token.service';

const requireRestSample: ITrademarkToken = {
  ...sampleWithRequiredData,
};

describe('TrademarkToken Service', () => {
  let service: TrademarkTokenService;
  let httpMock: HttpTestingController;
  let expectedResult: ITrademarkToken | ITrademarkToken[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(TrademarkTokenService);
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

    it('should create a TrademarkToken', () => {
      const trademarkToken = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(trademarkToken).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TrademarkToken', () => {
      const trademarkToken = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(trademarkToken).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TrademarkToken', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TrademarkToken', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TrademarkToken', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTrademarkTokenToCollectionIfMissing', () => {
      it('should add a TrademarkToken to an empty array', () => {
        const trademarkToken: ITrademarkToken = sampleWithRequiredData;
        expectedResult = service.addTrademarkTokenToCollectionIfMissing([], trademarkToken);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(trademarkToken);
      });

      it('should not add a TrademarkToken to an array that contains it', () => {
        const trademarkToken: ITrademarkToken = sampleWithRequiredData;
        const trademarkTokenCollection: ITrademarkToken[] = [
          {
            ...trademarkToken,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTrademarkTokenToCollectionIfMissing(trademarkTokenCollection, trademarkToken);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TrademarkToken to an array that doesn't contain it", () => {
        const trademarkToken: ITrademarkToken = sampleWithRequiredData;
        const trademarkTokenCollection: ITrademarkToken[] = [sampleWithPartialData];
        expectedResult = service.addTrademarkTokenToCollectionIfMissing(trademarkTokenCollection, trademarkToken);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(trademarkToken);
      });

      it('should add only unique TrademarkToken to an array', () => {
        const trademarkTokenArray: ITrademarkToken[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const trademarkTokenCollection: ITrademarkToken[] = [sampleWithRequiredData];
        expectedResult = service.addTrademarkTokenToCollectionIfMissing(trademarkTokenCollection, ...trademarkTokenArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const trademarkToken: ITrademarkToken = sampleWithRequiredData;
        const trademarkToken2: ITrademarkToken = sampleWithPartialData;
        expectedResult = service.addTrademarkTokenToCollectionIfMissing([], trademarkToken, trademarkToken2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(trademarkToken);
        expect(expectedResult).toContain(trademarkToken2);
      });

      it('should accept null and undefined values', () => {
        const trademarkToken: ITrademarkToken = sampleWithRequiredData;
        expectedResult = service.addTrademarkTokenToCollectionIfMissing([], null, trademarkToken, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(trademarkToken);
      });

      it('should return initial array if no TrademarkToken is added', () => {
        const trademarkTokenCollection: ITrademarkToken[] = [sampleWithRequiredData];
        expectedResult = service.addTrademarkTokenToCollectionIfMissing(trademarkTokenCollection, undefined, null);
        expect(expectedResult).toEqual(trademarkTokenCollection);
      });
    });

    describe('compareTrademarkToken', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTrademarkToken(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 1043 };
        const entity2 = null;

        const compareResult1 = service.compareTrademarkToken(entity1, entity2);
        const compareResult2 = service.compareTrademarkToken(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 1043 };
        const entity2 = { id: 17385 };

        const compareResult1 = service.compareTrademarkToken(entity1, entity2);
        const compareResult2 = service.compareTrademarkToken(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 1043 };
        const entity2 = { id: 1043 };

        const compareResult1 = service.compareTrademarkToken(entity1, entity2);
        const compareResult2 = service.compareTrademarkToken(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
