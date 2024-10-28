import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../phonetics.test-samples';

import { PhoneticsFormService } from './phonetics-form.service';

describe('Phonetics Form Service', () => {
  let service: PhoneticsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PhoneticsFormService);
  });

  describe('Service methods', () => {
    describe('createPhoneticsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPhoneticsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            sanitizedTm: expect.any(Object),
            phoneticPk: expect.any(Object),
            phoneticSk: expect.any(Object),
            complete: expect.any(Object),
            trademark: expect.any(Object),
          }),
        );
      });

      it('passing IPhonetics should create a new form with FormGroup', () => {
        const formGroup = service.createPhoneticsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            sanitizedTm: expect.any(Object),
            phoneticPk: expect.any(Object),
            phoneticSk: expect.any(Object),
            complete: expect.any(Object),
            trademark: expect.any(Object),
          }),
        );
      });
    });

    describe('getPhonetics', () => {
      it('should return NewPhonetics for default Phonetics initial value', () => {
        const formGroup = service.createPhoneticsFormGroup(sampleWithNewData);

        const phonetics = service.getPhonetics(formGroup) as any;

        expect(phonetics).toMatchObject(sampleWithNewData);
      });

      it('should return NewPhonetics for empty Phonetics initial value', () => {
        const formGroup = service.createPhoneticsFormGroup();

        const phonetics = service.getPhonetics(formGroup) as any;

        expect(phonetics).toMatchObject({});
      });

      it('should return IPhonetics', () => {
        const formGroup = service.createPhoneticsFormGroup(sampleWithRequiredData);

        const phonetics = service.getPhonetics(formGroup) as any;

        expect(phonetics).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPhonetics should not enable id FormControl', () => {
        const formGroup = service.createPhoneticsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPhonetics should disable id FormControl', () => {
        const formGroup = service.createPhoneticsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
