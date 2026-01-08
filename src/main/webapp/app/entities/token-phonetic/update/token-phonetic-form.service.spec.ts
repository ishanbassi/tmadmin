import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../token-phonetic.test-samples';

import { TokenPhoneticFormService } from './token-phonetic-form.service';

describe('TokenPhonetic Form Service', () => {
  let service: TokenPhoneticFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TokenPhoneticFormService);
  });

  describe('Service methods', () => {
    describe('createTokenPhoneticFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTokenPhoneticFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            algorithm: expect.any(Object),
            phoneticCode: expect.any(Object),
            secondaryPhoneticCode: expect.any(Object),
            trademarkToken: expect.any(Object),
          }),
        );
      });

      it('passing ITokenPhonetic should create a new form with FormGroup', () => {
        const formGroup = service.createTokenPhoneticFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            algorithm: expect.any(Object),
            phoneticCode: expect.any(Object),
            secondaryPhoneticCode: expect.any(Object),
            trademarkToken: expect.any(Object),
          }),
        );
      });
    });

    describe('getTokenPhonetic', () => {
      it('should return NewTokenPhonetic for default TokenPhonetic initial value', () => {
        const formGroup = service.createTokenPhoneticFormGroup(sampleWithNewData);

        const tokenPhonetic = service.getTokenPhonetic(formGroup) as any;

        expect(tokenPhonetic).toMatchObject(sampleWithNewData);
      });

      it('should return NewTokenPhonetic for empty TokenPhonetic initial value', () => {
        const formGroup = service.createTokenPhoneticFormGroup();

        const tokenPhonetic = service.getTokenPhonetic(formGroup) as any;

        expect(tokenPhonetic).toMatchObject({});
      });

      it('should return ITokenPhonetic', () => {
        const formGroup = service.createTokenPhoneticFormGroup(sampleWithRequiredData);

        const tokenPhonetic = service.getTokenPhonetic(formGroup) as any;

        expect(tokenPhonetic).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITokenPhonetic should not enable id FormControl', () => {
        const formGroup = service.createTokenPhoneticFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTokenPhonetic should disable id FormControl', () => {
        const formGroup = service.createTokenPhoneticFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
