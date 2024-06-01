import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPublishedTmPhonetics } from '../published-tm-phonetics.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../published-tm-phonetics.test-samples';

import { PublishedTmPhoneticsService } from './published-tm-phonetics.service';

const requireRestSample: IPublishedTmPhonetics = {
  ...sampleWithRequiredData,
};

describe('PublishedTmPhonetics Service', () => {
  let service: PublishedTmPhoneticsService;
  let httpMock: HttpTestingController;
  let expectedResult: IPublishedTmPhonetics | IPublishedTmPhonetics[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PublishedTmPhoneticsService);
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

    it('should create a PublishedTmPhonetics', () => {
      const publishedTmPhonetics = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(publishedTmPhonetics).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PublishedTmPhonetics', () => {
      const publishedTmPhonetics = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(publishedTmPhonetics).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PublishedTmPhonetics', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PublishedTmPhonetics', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PublishedTmPhonetics', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPublishedTmPhoneticsToCollectionIfMissing', () => {
      it('should add a PublishedTmPhonetics to an empty array', () => {
        const publishedTmPhonetics: IPublishedTmPhonetics = sampleWithRequiredData;
        expectedResult = service.addPublishedTmPhoneticsToCollectionIfMissing([], publishedTmPhonetics);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(publishedTmPhonetics);
      });

      it('should not add a PublishedTmPhonetics to an array that contains it', () => {
        const publishedTmPhonetics: IPublishedTmPhonetics = sampleWithRequiredData;
        const publishedTmPhoneticsCollection: IPublishedTmPhonetics[] = [
          {
            ...publishedTmPhonetics,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPublishedTmPhoneticsToCollectionIfMissing(publishedTmPhoneticsCollection, publishedTmPhonetics);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PublishedTmPhonetics to an array that doesn't contain it", () => {
        const publishedTmPhonetics: IPublishedTmPhonetics = sampleWithRequiredData;
        const publishedTmPhoneticsCollection: IPublishedTmPhonetics[] = [sampleWithPartialData];
        expectedResult = service.addPublishedTmPhoneticsToCollectionIfMissing(publishedTmPhoneticsCollection, publishedTmPhonetics);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(publishedTmPhonetics);
      });

      it('should add only unique PublishedTmPhonetics to an array', () => {
        const publishedTmPhoneticsArray: IPublishedTmPhonetics[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const publishedTmPhoneticsCollection: IPublishedTmPhonetics[] = [sampleWithRequiredData];
        expectedResult = service.addPublishedTmPhoneticsToCollectionIfMissing(publishedTmPhoneticsCollection, ...publishedTmPhoneticsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const publishedTmPhonetics: IPublishedTmPhonetics = sampleWithRequiredData;
        const publishedTmPhonetics2: IPublishedTmPhonetics = sampleWithPartialData;
        expectedResult = service.addPublishedTmPhoneticsToCollectionIfMissing([], publishedTmPhonetics, publishedTmPhonetics2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(publishedTmPhonetics);
        expect(expectedResult).toContain(publishedTmPhonetics2);
      });

      it('should accept null and undefined values', () => {
        const publishedTmPhonetics: IPublishedTmPhonetics = sampleWithRequiredData;
        expectedResult = service.addPublishedTmPhoneticsToCollectionIfMissing([], null, publishedTmPhonetics, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(publishedTmPhonetics);
      });

      it('should return initial array if no PublishedTmPhonetics is added', () => {
        const publishedTmPhoneticsCollection: IPublishedTmPhonetics[] = [sampleWithRequiredData];
        expectedResult = service.addPublishedTmPhoneticsToCollectionIfMissing(publishedTmPhoneticsCollection, undefined, null);
        expect(expectedResult).toEqual(publishedTmPhoneticsCollection);
      });
    });

    describe('comparePublishedTmPhonetics', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePublishedTmPhonetics(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePublishedTmPhonetics(entity1, entity2);
        const compareResult2 = service.comparePublishedTmPhonetics(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePublishedTmPhonetics(entity1, entity2);
        const compareResult2 = service.comparePublishedTmPhonetics(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePublishedTmPhonetics(entity1, entity2);
        const compareResult2 = service.comparePublishedTmPhonetics(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
