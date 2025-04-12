import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../published-tm.test-samples';

import { PublishedTmFormService } from './published-tm-form.service';

describe('PublishedTm Form Service', () => {
  let service: PublishedTmFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PublishedTmFormService);
  });

  describe('Service methods', () => {
    describe('createPublishedTmFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPublishedTmFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            details: expect.any(Object),
            applicationNo: expect.any(Object),
            applicationDate: expect.any(Object),
            agentName: expect.any(Object),
            agentAddress: expect.any(Object),
            proprietorName: expect.any(Object),
            proprietorAddress: expect.any(Object),
            headOffice: expect.any(Object),
            imgUrl: expect.any(Object),
            tmClass: expect.any(Object),
            journalNo: expect.any(Object),
            deleted: expect.any(Object),
            usage: expect.any(Object),
            associatedTms: expect.any(Object),
            trademarkStatus: expect.any(Object),
            createdDate: expect.any(Object),
            modifiedDate: expect.any(Object),
            renewalDate: expect.any(Object),
            type: expect.any(Object),
            pageNo: expect.any(Object),
            tmAgent: expect.any(Object),
          }),
        );
      });

      it('passing IPublishedTm should create a new form with FormGroup', () => {
        const formGroup = service.createPublishedTmFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            details: expect.any(Object),
            applicationNo: expect.any(Object),
            applicationDate: expect.any(Object),
            agentName: expect.any(Object),
            agentAddress: expect.any(Object),
            proprietorName: expect.any(Object),
            proprietorAddress: expect.any(Object),
            headOffice: expect.any(Object),
            imgUrl: expect.any(Object),
            tmClass: expect.any(Object),
            journalNo: expect.any(Object),
            deleted: expect.any(Object),
            usage: expect.any(Object),
            associatedTms: expect.any(Object),
            trademarkStatus: expect.any(Object),
            createdDate: expect.any(Object),
            modifiedDate: expect.any(Object),
            renewalDate: expect.any(Object),
            type: expect.any(Object),
            pageNo: expect.any(Object),
            tmAgent: expect.any(Object),
          }),
        );
      });
    });

    describe('getPublishedTm', () => {
      it('should return NewPublishedTm for default PublishedTm initial value', () => {
        const formGroup = service.createPublishedTmFormGroup(sampleWithNewData);

        const publishedTm = service.getPublishedTm(formGroup) as any;

        expect(publishedTm).toMatchObject(sampleWithNewData);
      });

      it('should return NewPublishedTm for empty PublishedTm initial value', () => {
        const formGroup = service.createPublishedTmFormGroup();

        const publishedTm = service.getPublishedTm(formGroup) as any;

        expect(publishedTm).toMatchObject({});
      });

      it('should return IPublishedTm', () => {
        const formGroup = service.createPublishedTmFormGroup(sampleWithRequiredData);

        const publishedTm = service.getPublishedTm(formGroup) as any;

        expect(publishedTm).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPublishedTm should not enable id FormControl', () => {
        const formGroup = service.createPublishedTmFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPublishedTm should disable id FormControl', () => {
        const formGroup = service.createPublishedTmFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
