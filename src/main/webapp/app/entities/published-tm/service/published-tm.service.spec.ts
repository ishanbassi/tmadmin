import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IPublishedTm } from '../published-tm.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../published-tm.test-samples';

import { PublishedTmService, RestPublishedTm } from './published-tm.service';

const requireRestSample: RestPublishedTm = {
  ...sampleWithRequiredData,
  applicationDate: sampleWithRequiredData.applicationDate?.format(DATE_FORMAT),
};

describe('PublishedTm Service', () => {
  let service: PublishedTmService;
  let httpMock: HttpTestingController;
  let expectedResult: IPublishedTm | IPublishedTm[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PublishedTmService);
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

    it('should create a PublishedTm', () => {
      const publishedTm = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(publishedTm).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PublishedTm', () => {
      const publishedTm = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(publishedTm).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PublishedTm', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PublishedTm', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PublishedTm', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPublishedTmToCollectionIfMissing', () => {
      it('should add a PublishedTm to an empty array', () => {
        const publishedTm: IPublishedTm = sampleWithRequiredData;
        expectedResult = service.addPublishedTmToCollectionIfMissing([], publishedTm);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(publishedTm);
      });

      it('should not add a PublishedTm to an array that contains it', () => {
        const publishedTm: IPublishedTm = sampleWithRequiredData;
        const publishedTmCollection: IPublishedTm[] = [
          {
            ...publishedTm,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPublishedTmToCollectionIfMissing(publishedTmCollection, publishedTm);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PublishedTm to an array that doesn't contain it", () => {
        const publishedTm: IPublishedTm = sampleWithRequiredData;
        const publishedTmCollection: IPublishedTm[] = [sampleWithPartialData];
        expectedResult = service.addPublishedTmToCollectionIfMissing(publishedTmCollection, publishedTm);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(publishedTm);
      });

      it('should add only unique PublishedTm to an array', () => {
        const publishedTmArray: IPublishedTm[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const publishedTmCollection: IPublishedTm[] = [sampleWithRequiredData];
        expectedResult = service.addPublishedTmToCollectionIfMissing(publishedTmCollection, ...publishedTmArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const publishedTm: IPublishedTm = sampleWithRequiredData;
        const publishedTm2: IPublishedTm = sampleWithPartialData;
        expectedResult = service.addPublishedTmToCollectionIfMissing([], publishedTm, publishedTm2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(publishedTm);
        expect(expectedResult).toContain(publishedTm2);
      });

      it('should accept null and undefined values', () => {
        const publishedTm: IPublishedTm = sampleWithRequiredData;
        expectedResult = service.addPublishedTmToCollectionIfMissing([], null, publishedTm, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(publishedTm);
      });

      it('should return initial array if no PublishedTm is added', () => {
        const publishedTmCollection: IPublishedTm[] = [sampleWithRequiredData];
        expectedResult = service.addPublishedTmToCollectionIfMissing(publishedTmCollection, undefined, null);
        expect(expectedResult).toEqual(publishedTmCollection);
      });
    });

    describe('comparePublishedTm', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePublishedTm(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePublishedTm(entity1, entity2);
        const compareResult2 = service.comparePublishedTm(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePublishedTm(entity1, entity2);
        const compareResult2 = service.comparePublishedTm(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePublishedTm(entity1, entity2);
        const compareResult2 = service.comparePublishedTm(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
