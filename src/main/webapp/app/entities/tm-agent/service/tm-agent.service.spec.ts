import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITmAgent } from '../tm-agent.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../tm-agent.test-samples';

import { TmAgentService } from './tm-agent.service';

const requireRestSample: ITmAgent = {
  ...sampleWithRequiredData,
};

describe('TmAgent Service', () => {
  let service: TmAgentService;
  let httpMock: HttpTestingController;
  let expectedResult: ITmAgent | ITmAgent[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TmAgentService);
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

    it('should create a TmAgent', () => {
      const tmAgent = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(tmAgent).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TmAgent', () => {
      const tmAgent = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(tmAgent).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TmAgent', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TmAgent', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TmAgent', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTmAgentToCollectionIfMissing', () => {
      it('should add a TmAgent to an empty array', () => {
        const tmAgent: ITmAgent = sampleWithRequiredData;
        expectedResult = service.addTmAgentToCollectionIfMissing([], tmAgent);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tmAgent);
      });

      it('should not add a TmAgent to an array that contains it', () => {
        const tmAgent: ITmAgent = sampleWithRequiredData;
        const tmAgentCollection: ITmAgent[] = [
          {
            ...tmAgent,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTmAgentToCollectionIfMissing(tmAgentCollection, tmAgent);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TmAgent to an array that doesn't contain it", () => {
        const tmAgent: ITmAgent = sampleWithRequiredData;
        const tmAgentCollection: ITmAgent[] = [sampleWithPartialData];
        expectedResult = service.addTmAgentToCollectionIfMissing(tmAgentCollection, tmAgent);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tmAgent);
      });

      it('should add only unique TmAgent to an array', () => {
        const tmAgentArray: ITmAgent[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const tmAgentCollection: ITmAgent[] = [sampleWithRequiredData];
        expectedResult = service.addTmAgentToCollectionIfMissing(tmAgentCollection, ...tmAgentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tmAgent: ITmAgent = sampleWithRequiredData;
        const tmAgent2: ITmAgent = sampleWithPartialData;
        expectedResult = service.addTmAgentToCollectionIfMissing([], tmAgent, tmAgent2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tmAgent);
        expect(expectedResult).toContain(tmAgent2);
      });

      it('should accept null and undefined values', () => {
        const tmAgent: ITmAgent = sampleWithRequiredData;
        expectedResult = service.addTmAgentToCollectionIfMissing([], null, tmAgent, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tmAgent);
      });

      it('should return initial array if no TmAgent is added', () => {
        const tmAgentCollection: ITmAgent[] = [sampleWithRequiredData];
        expectedResult = service.addTmAgentToCollectionIfMissing(tmAgentCollection, undefined, null);
        expect(expectedResult).toEqual(tmAgentCollection);
      });
    });

    describe('compareTmAgent', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTmAgent(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTmAgent(entity1, entity2);
        const compareResult2 = service.compareTmAgent(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTmAgent(entity1, entity2);
        const compareResult2 = service.compareTmAgent(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTmAgent(entity1, entity2);
        const compareResult2 = service.compareTmAgent(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
