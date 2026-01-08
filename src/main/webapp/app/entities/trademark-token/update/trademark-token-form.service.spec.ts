import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../trademark-token.test-samples';

import { TrademarkTokenFormService } from './trademark-token-form.service';

describe('TrademarkToken Form Service', () => {
  let service: TrademarkTokenFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TrademarkTokenFormService);
  });

  describe('Service methods', () => {
    describe('createTrademarkTokenFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTrademarkTokenFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            tokenText: expect.any(Object),
            tokenType: expect.any(Object),
            position: expect.any(Object),
            trademark: expect.any(Object),
          }),
        );
      });

      it('passing ITrademarkToken should create a new form with FormGroup', () => {
        const formGroup = service.createTrademarkTokenFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            tokenText: expect.any(Object),
            tokenType: expect.any(Object),
            position: expect.any(Object),
            trademark: expect.any(Object),
          }),
        );
      });
    });

    describe('getTrademarkToken', () => {
      it('should return NewTrademarkToken for default TrademarkToken initial value', () => {
        const formGroup = service.createTrademarkTokenFormGroup(sampleWithNewData);

        const trademarkToken = service.getTrademarkToken(formGroup) as any;

        expect(trademarkToken).toMatchObject(sampleWithNewData);
      });

      it('should return NewTrademarkToken for empty TrademarkToken initial value', () => {
        const formGroup = service.createTrademarkTokenFormGroup();

        const trademarkToken = service.getTrademarkToken(formGroup) as any;

        expect(trademarkToken).toMatchObject({});
      });

      it('should return ITrademarkToken', () => {
        const formGroup = service.createTrademarkTokenFormGroup(sampleWithRequiredData);

        const trademarkToken = service.getTrademarkToken(formGroup) as any;

        expect(trademarkToken).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITrademarkToken should not enable id FormControl', () => {
        const formGroup = service.createTrademarkTokenFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTrademarkToken should disable id FormControl', () => {
        const formGroup = service.createTrademarkTokenFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
