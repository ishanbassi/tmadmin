import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IDocuments } from '../documents.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../documents.test-samples';

import { DocumentsService, RestDocuments } from './documents.service';

const requireRestSample: RestDocuments = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  modifiedDate: sampleWithRequiredData.modifiedDate?.toJSON(),
};

describe('Documents Service', () => {
  let service: DocumentsService;
  let httpMock: HttpTestingController;
  let expectedResult: IDocuments | IDocuments[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(DocumentsService);
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

    it('should create a Documents', () => {
      const documents = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(documents).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Documents', () => {
      const documents = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(documents).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Documents', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Documents', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Documents', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDocumentsToCollectionIfMissing', () => {
      it('should add a Documents to an empty array', () => {
        const documents: IDocuments = sampleWithRequiredData;
        expectedResult = service.addDocumentsToCollectionIfMissing([], documents);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(documents);
      });

      it('should not add a Documents to an array that contains it', () => {
        const documents: IDocuments = sampleWithRequiredData;
        const documentsCollection: IDocuments[] = [
          {
            ...documents,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDocumentsToCollectionIfMissing(documentsCollection, documents);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Documents to an array that doesn't contain it", () => {
        const documents: IDocuments = sampleWithRequiredData;
        const documentsCollection: IDocuments[] = [sampleWithPartialData];
        expectedResult = service.addDocumentsToCollectionIfMissing(documentsCollection, documents);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(documents);
      });

      it('should add only unique Documents to an array', () => {
        const documentsArray: IDocuments[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const documentsCollection: IDocuments[] = [sampleWithRequiredData];
        expectedResult = service.addDocumentsToCollectionIfMissing(documentsCollection, ...documentsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const documents: IDocuments = sampleWithRequiredData;
        const documents2: IDocuments = sampleWithPartialData;
        expectedResult = service.addDocumentsToCollectionIfMissing([], documents, documents2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(documents);
        expect(expectedResult).toContain(documents2);
      });

      it('should accept null and undefined values', () => {
        const documents: IDocuments = sampleWithRequiredData;
        expectedResult = service.addDocumentsToCollectionIfMissing([], null, documents, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(documents);
      });

      it('should return initial array if no Documents is added', () => {
        const documentsCollection: IDocuments[] = [sampleWithRequiredData];
        expectedResult = service.addDocumentsToCollectionIfMissing(documentsCollection, undefined, null);
        expect(expectedResult).toEqual(documentsCollection);
      });
    });

    describe('compareDocuments', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDocuments(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 24452 };
        const entity2 = null;

        const compareResult1 = service.compareDocuments(entity1, entity2);
        const compareResult2 = service.compareDocuments(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 24452 };
        const entity2 = { id: 1265 };

        const compareResult1 = service.compareDocuments(entity1, entity2);
        const compareResult2 = service.compareDocuments(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 24452 };
        const entity2 = { id: 24452 };

        const compareResult1 = service.compareDocuments(entity1, entity2);
        const compareResult2 = service.compareDocuments(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
