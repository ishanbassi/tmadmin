import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IUserEventsTracking } from '../user-events-tracking.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../user-events-tracking.test-samples';

import { RestUserEventsTracking, UserEventsTrackingService } from './user-events-tracking.service';

const requireRestSample: RestUserEventsTracking = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  modifiedDate: sampleWithRequiredData.modifiedDate?.toJSON(),
};

describe('UserEventsTracking Service', () => {
  let service: UserEventsTrackingService;
  let httpMock: HttpTestingController;
  let expectedResult: IUserEventsTracking | IUserEventsTracking[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(UserEventsTrackingService);
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

    it('should create a UserEventsTracking', () => {
      const userEventsTracking = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(userEventsTracking).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a UserEventsTracking', () => {
      const userEventsTracking = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(userEventsTracking).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a UserEventsTracking', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of UserEventsTracking', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a UserEventsTracking', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addUserEventsTrackingToCollectionIfMissing', () => {
      it('should add a UserEventsTracking to an empty array', () => {
        const userEventsTracking: IUserEventsTracking = sampleWithRequiredData;
        expectedResult = service.addUserEventsTrackingToCollectionIfMissing([], userEventsTracking);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(userEventsTracking);
      });

      it('should not add a UserEventsTracking to an array that contains it', () => {
        const userEventsTracking: IUserEventsTracking = sampleWithRequiredData;
        const userEventsTrackingCollection: IUserEventsTracking[] = [
          {
            ...userEventsTracking,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addUserEventsTrackingToCollectionIfMissing(userEventsTrackingCollection, userEventsTracking);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a UserEventsTracking to an array that doesn't contain it", () => {
        const userEventsTracking: IUserEventsTracking = sampleWithRequiredData;
        const userEventsTrackingCollection: IUserEventsTracking[] = [sampleWithPartialData];
        expectedResult = service.addUserEventsTrackingToCollectionIfMissing(userEventsTrackingCollection, userEventsTracking);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(userEventsTracking);
      });

      it('should add only unique UserEventsTracking to an array', () => {
        const userEventsTrackingArray: IUserEventsTracking[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const userEventsTrackingCollection: IUserEventsTracking[] = [sampleWithRequiredData];
        expectedResult = service.addUserEventsTrackingToCollectionIfMissing(userEventsTrackingCollection, ...userEventsTrackingArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const userEventsTracking: IUserEventsTracking = sampleWithRequiredData;
        const userEventsTracking2: IUserEventsTracking = sampleWithPartialData;
        expectedResult = service.addUserEventsTrackingToCollectionIfMissing([], userEventsTracking, userEventsTracking2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(userEventsTracking);
        expect(expectedResult).toContain(userEventsTracking2);
      });

      it('should accept null and undefined values', () => {
        const userEventsTracking: IUserEventsTracking = sampleWithRequiredData;
        expectedResult = service.addUserEventsTrackingToCollectionIfMissing([], null, userEventsTracking, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(userEventsTracking);
      });

      it('should return initial array if no UserEventsTracking is added', () => {
        const userEventsTrackingCollection: IUserEventsTracking[] = [sampleWithRequiredData];
        expectedResult = service.addUserEventsTrackingToCollectionIfMissing(userEventsTrackingCollection, undefined, null);
        expect(expectedResult).toEqual(userEventsTrackingCollection);
      });
    });

    describe('compareUserEventsTracking', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareUserEventsTracking(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 1044 };
        const entity2 = null;

        const compareResult1 = service.compareUserEventsTracking(entity1, entity2);
        const compareResult2 = service.compareUserEventsTracking(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 1044 };
        const entity2 = { id: 17786 };

        const compareResult1 = service.compareUserEventsTracking(entity1, entity2);
        const compareResult2 = service.compareUserEventsTracking(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 1044 };
        const entity2 = { id: 1044 };

        const compareResult1 = service.compareUserEventsTracking(entity1, entity2);
        const compareResult2 = service.compareUserEventsTracking(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
