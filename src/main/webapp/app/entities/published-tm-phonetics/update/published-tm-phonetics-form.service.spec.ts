import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../published-tm-phonetics.test-samples';

import { PublishedTmPhoneticsFormService } from './published-tm-phonetics-form.service';

describe('PublishedTmPhonetics Form Service', () => {
  let service: PublishedTmPhoneticsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PublishedTmPhoneticsFormService);
  });

  describe('Service methods', () => {
    describe('createPublishedTmPhoneticsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPublishedTmPhoneticsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            sanitizedTm: expect.any(Object),
            phoneticPk: expect.any(Object),
            phoneticSk: expect.any(Object),
            complete: expect.any(Object),
            publishedTm: expect.any(Object),
          }),
        );
      });

      it('passing IPublishedTmPhonetics should create a new form with FormGroup', () => {
        const formGroup = service.createPublishedTmPhoneticsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            sanitizedTm: expect.any(Object),
            phoneticPk: expect.any(Object),
            phoneticSk: expect.any(Object),
            complete: expect.any(Object),
            publishedTm: expect.any(Object),
          }),
        );
      });
    });

    describe('getPublishedTmPhonetics', () => {
      it('should return NewPublishedTmPhonetics for default PublishedTmPhonetics initial value', () => {
        const formGroup = service.createPublishedTmPhoneticsFormGroup(sampleWithNewData);

        const publishedTmPhonetics = service.getPublishedTmPhonetics(formGroup) as any;

        expect(publishedTmPhonetics).toMatchObject(sampleWithNewData);
      });

      it('should return NewPublishedTmPhonetics for empty PublishedTmPhonetics initial value', () => {
        const formGroup = service.createPublishedTmPhoneticsFormGroup();

        const publishedTmPhonetics = service.getPublishedTmPhonetics(formGroup) as any;

        expect(publishedTmPhonetics).toMatchObject({});
      });

      it('should return IPublishedTmPhonetics', () => {
        const formGroup = service.createPublishedTmPhoneticsFormGroup(sampleWithRequiredData);

        const publishedTmPhonetics = service.getPublishedTmPhonetics(formGroup) as any;

        expect(publishedTmPhonetics).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPublishedTmPhonetics should not enable id FormControl', () => {
        const formGroup = service.createPublishedTmPhoneticsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPublishedTmPhonetics should disable id FormControl', () => {
        const formGroup = service.createPublishedTmPhoneticsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
