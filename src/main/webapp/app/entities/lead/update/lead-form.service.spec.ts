import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../lead.test-samples';

import { LeadFormService } from './lead-form.service';

describe('Lead Form Service', () => {
  let service: LeadFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LeadFormService);
  });

  describe('Service methods', () => {
    describe('createLeadFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createLeadFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fullName: expect.any(Object),
            phoneNumber: expect.any(Object),
            email: expect.any(Object),
            city: expect.any(Object),
            brandName: expect.any(Object),
            selectedPackage: expect.any(Object),
            tmClass: expect.any(Object),
            comments: expect.any(Object),
            contactMethod: expect.any(Object),
            createdDate: expect.any(Object),
            modifiedDate: expect.any(Object),
            deleted: expect.any(Object),
            status: expect.any(Object),
            leadSource: expect.any(Object),
            assignedTo: expect.any(Object),
          }),
        );
      });

      it('passing ILead should create a new form with FormGroup', () => {
        const formGroup = service.createLeadFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fullName: expect.any(Object),
            phoneNumber: expect.any(Object),
            email: expect.any(Object),
            city: expect.any(Object),
            brandName: expect.any(Object),
            selectedPackage: expect.any(Object),
            tmClass: expect.any(Object),
            comments: expect.any(Object),
            contactMethod: expect.any(Object),
            createdDate: expect.any(Object),
            modifiedDate: expect.any(Object),
            deleted: expect.any(Object),
            status: expect.any(Object),
            leadSource: expect.any(Object),
            assignedTo: expect.any(Object),
          }),
        );
      });
    });

    describe('getLead', () => {
      it('should return NewLead for default Lead initial value', () => {
        const formGroup = service.createLeadFormGroup(sampleWithNewData);

        const lead = service.getLead(formGroup) as any;

        expect(lead).toMatchObject(sampleWithNewData);
      });

      it('should return NewLead for empty Lead initial value', () => {
        const formGroup = service.createLeadFormGroup();

        const lead = service.getLead(formGroup) as any;

        expect(lead).toMatchObject({});
      });

      it('should return ILead', () => {
        const formGroup = service.createLeadFormGroup(sampleWithRequiredData);

        const lead = service.getLead(formGroup) as any;

        expect(lead).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ILead should not enable id FormControl', () => {
        const formGroup = service.createLeadFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewLead should disable id FormControl', () => {
        const formGroup = service.createLeadFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
