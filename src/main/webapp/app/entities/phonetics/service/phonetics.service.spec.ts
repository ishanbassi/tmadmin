import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPhonetics } from '../phonetics.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../phonetics.test-samples';

import { PhoneticsService } from './phonetics.service';

const requireRestSample: IPhonetics = {
  ...sampleWithRequiredData,
};

describe('Phonetics Service', () => {
  let service: PhoneticsService;
  let httpMock: HttpTestingController;
  let expectedResult: IPhonetics | IPhonetics[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PhoneticsService);
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

    it('should create a Phonetics', () => {
      const phonetics = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(phonetics).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Phonetics', () => {
      const phonetics = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(phonetics).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Phonetics', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Phonetics', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Phonetics', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPhoneticsToCollectionIfMissing', () => {
      it('should add a Phonetics to an empty array', () => {
        const phonetics: IPhonetics = sampleWithRequiredData;
        expectedResult = service.addPhoneticsToCollectionIfMissing([], phonetics);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(phonetics);
      });

      it('should not add a Phonetics to an array that contains it', () => {
        const phonetics: IPhonetics = sampleWithRequiredData;
        const phoneticsCollection: IPhonetics[] = [
          {
            ...phonetics,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPhoneticsToCollectionIfMissing(phoneticsCollection, phonetics);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Phonetics to an array that doesn't contain it", () => {
        const phonetics: IPhonetics = sampleWithRequiredData;
        const phoneticsCollection: IPhonetics[] = [sampleWithPartialData];
        expectedResult = service.addPhoneticsToCollectionIfMissing(phoneticsCollection, phonetics);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(phonetics);
      });

      it('should add only unique Phonetics to an array', () => {
        const phoneticsArray: IPhonetics[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const phoneticsCollection: IPhonetics[] = [sampleWithRequiredData];
        expectedResult = service.addPhoneticsToCollectionIfMissing(phoneticsCollection, ...phoneticsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const phonetics: IPhonetics = sampleWithRequiredData;
        const phonetics2: IPhonetics = sampleWithPartialData;
        expectedResult = service.addPhoneticsToCollectionIfMissing([], phonetics, phonetics2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(phonetics);
        expect(expectedResult).toContain(phonetics2);
      });

      it('should accept null and undefined values', () => {
        const phonetics: IPhonetics = sampleWithRequiredData;
        expectedResult = service.addPhoneticsToCollectionIfMissing([], null, phonetics, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(phonetics);
      });

      it('should return initial array if no Phonetics is added', () => {
        const phoneticsCollection: IPhonetics[] = [sampleWithRequiredData];
        expectedResult = service.addPhoneticsToCollectionIfMissing(phoneticsCollection, undefined, null);
        expect(expectedResult).toEqual(phoneticsCollection);
      });
    });

    describe('comparePhonetics', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePhonetics(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePhonetics(entity1, entity2);
        const compareResult2 = service.comparePhonetics(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePhonetics(entity1, entity2);
        const compareResult2 = service.comparePhonetics(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePhonetics(entity1, entity2);
        const compareResult2 = service.comparePhonetics(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
