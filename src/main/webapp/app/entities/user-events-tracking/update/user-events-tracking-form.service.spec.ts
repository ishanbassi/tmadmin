import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../user-events-tracking.test-samples';

import { UserEventsTrackingFormService } from './user-events-tracking-form.service';

describe('UserEventsTracking Form Service', () => {
  let service: UserEventsTrackingFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserEventsTrackingFormService);
  });

  describe('Service methods', () => {
    describe('createUserEventsTrackingFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createUserEventsTrackingFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            eventType: expect.any(Object),
            pageName: expect.any(Object),
            deviceType: expect.any(Object),
            createdDate: expect.any(Object),
            deleted: expect.any(Object),
            modifiedDate: expect.any(Object),
            userProfile: expect.any(Object),
          }),
        );
      });

      it('passing IUserEventsTracking should create a new form with FormGroup', () => {
        const formGroup = service.createUserEventsTrackingFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            eventType: expect.any(Object),
            pageName: expect.any(Object),
            deviceType: expect.any(Object),
            createdDate: expect.any(Object),
            deleted: expect.any(Object),
            modifiedDate: expect.any(Object),
            userProfile: expect.any(Object),
          }),
        );
      });
    });

    describe('getUserEventsTracking', () => {
      it('should return NewUserEventsTracking for default UserEventsTracking initial value', () => {
        const formGroup = service.createUserEventsTrackingFormGroup(sampleWithNewData);

        const userEventsTracking = service.getUserEventsTracking(formGroup) as any;

        expect(userEventsTracking).toMatchObject(sampleWithNewData);
      });

      it('should return NewUserEventsTracking for empty UserEventsTracking initial value', () => {
        const formGroup = service.createUserEventsTrackingFormGroup();

        const userEventsTracking = service.getUserEventsTracking(formGroup) as any;

        expect(userEventsTracking).toMatchObject({});
      });

      it('should return IUserEventsTracking', () => {
        const formGroup = service.createUserEventsTrackingFormGroup(sampleWithRequiredData);

        const userEventsTracking = service.getUserEventsTracking(formGroup) as any;

        expect(userEventsTracking).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IUserEventsTracking should not enable id FormControl', () => {
        const formGroup = service.createUserEventsTrackingFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewUserEventsTracking should disable id FormControl', () => {
        const formGroup = service.createUserEventsTrackingFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
