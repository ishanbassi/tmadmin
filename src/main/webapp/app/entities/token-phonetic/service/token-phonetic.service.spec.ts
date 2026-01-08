import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ITokenPhonetic } from '../token-phonetic.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../token-phonetic.test-samples';

import { TokenPhoneticService } from './token-phonetic.service';

const requireRestSample: ITokenPhonetic = {
  ...sampleWithRequiredData,
};

describe('TokenPhonetic Service', () => {
  let service: TokenPhoneticService;
  let httpMock: HttpTestingController;
  let expectedResult: ITokenPhonetic | ITokenPhonetic[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(TokenPhoneticService);
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

    it('should create a TokenPhonetic', () => {
      const tokenPhonetic = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(tokenPhonetic).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TokenPhonetic', () => {
      const tokenPhonetic = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(tokenPhonetic).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TokenPhonetic', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TokenPhonetic', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TokenPhonetic', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTokenPhoneticToCollectionIfMissing', () => {
      it('should add a TokenPhonetic to an empty array', () => {
        const tokenPhonetic: ITokenPhonetic = sampleWithRequiredData;
        expectedResult = service.addTokenPhoneticToCollectionIfMissing([], tokenPhonetic);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tokenPhonetic);
      });

      it('should not add a TokenPhonetic to an array that contains it', () => {
        const tokenPhonetic: ITokenPhonetic = sampleWithRequiredData;
        const tokenPhoneticCollection: ITokenPhonetic[] = [
          {
            ...tokenPhonetic,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTokenPhoneticToCollectionIfMissing(tokenPhoneticCollection, tokenPhonetic);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TokenPhonetic to an array that doesn't contain it", () => {
        const tokenPhonetic: ITokenPhonetic = sampleWithRequiredData;
        const tokenPhoneticCollection: ITokenPhonetic[] = [sampleWithPartialData];
        expectedResult = service.addTokenPhoneticToCollectionIfMissing(tokenPhoneticCollection, tokenPhonetic);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tokenPhonetic);
      });

      it('should add only unique TokenPhonetic to an array', () => {
        const tokenPhoneticArray: ITokenPhonetic[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const tokenPhoneticCollection: ITokenPhonetic[] = [sampleWithRequiredData];
        expectedResult = service.addTokenPhoneticToCollectionIfMissing(tokenPhoneticCollection, ...tokenPhoneticArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tokenPhonetic: ITokenPhonetic = sampleWithRequiredData;
        const tokenPhonetic2: ITokenPhonetic = sampleWithPartialData;
        expectedResult = service.addTokenPhoneticToCollectionIfMissing([], tokenPhonetic, tokenPhonetic2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tokenPhonetic);
        expect(expectedResult).toContain(tokenPhonetic2);
      });

      it('should accept null and undefined values', () => {
        const tokenPhonetic: ITokenPhonetic = sampleWithRequiredData;
        expectedResult = service.addTokenPhoneticToCollectionIfMissing([], null, tokenPhonetic, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tokenPhonetic);
      });

      it('should return initial array if no TokenPhonetic is added', () => {
        const tokenPhoneticCollection: ITokenPhonetic[] = [sampleWithRequiredData];
        expectedResult = service.addTokenPhoneticToCollectionIfMissing(tokenPhoneticCollection, undefined, null);
        expect(expectedResult).toEqual(tokenPhoneticCollection);
      });
    });

    describe('compareTokenPhonetic', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTokenPhonetic(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 24987 };
        const entity2 = null;

        const compareResult1 = service.compareTokenPhonetic(entity1, entity2);
        const compareResult2 = service.compareTokenPhonetic(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 24987 };
        const entity2 = { id: 920 };

        const compareResult1 = service.compareTokenPhonetic(entity1, entity2);
        const compareResult2 = service.compareTokenPhonetic(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 24987 };
        const entity2 = { id: 24987 };

        const compareResult1 = service.compareTokenPhonetic(entity1, entity2);
        const compareResult2 = service.compareTokenPhonetic(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
