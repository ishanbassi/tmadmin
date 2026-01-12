import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../trademark-token-frequency.test-samples';

import { TrademarkTokenFrequencyFormService } from './trademark-token-frequency-form.service';

describe('TrademarkTokenFrequency Form Service', () => {
  let service: TrademarkTokenFrequencyFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TrademarkTokenFrequencyFormService);
  });

  describe('Service methods', () => {
    describe('createTrademarkTokenFrequencyFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTrademarkTokenFrequencyFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            frequency: expect.any(Object),
            word: expect.any(Object),
            createdDate: expect.any(Object),
            deleted: expect.any(Object),
            modifiedDate: expect.any(Object),
          }),
        );
      });

      it('passing ITrademarkTokenFrequency should create a new form with FormGroup', () => {
        const formGroup = service.createTrademarkTokenFrequencyFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            frequency: expect.any(Object),
            word: expect.any(Object),
            createdDate: expect.any(Object),
            deleted: expect.any(Object),
            modifiedDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getTrademarkTokenFrequency', () => {
      it('should return NewTrademarkTokenFrequency for default TrademarkTokenFrequency initial value', () => {
        const formGroup = service.createTrademarkTokenFrequencyFormGroup(sampleWithNewData);

        const trademarkTokenFrequency = service.getTrademarkTokenFrequency(formGroup) as any;

        expect(trademarkTokenFrequency).toMatchObject(sampleWithNewData);
      });

      it('should return NewTrademarkTokenFrequency for empty TrademarkTokenFrequency initial value', () => {
        const formGroup = service.createTrademarkTokenFrequencyFormGroup();

        const trademarkTokenFrequency = service.getTrademarkTokenFrequency(formGroup) as any;

        expect(trademarkTokenFrequency).toMatchObject({});
      });

      it('should return ITrademarkTokenFrequency', () => {
        const formGroup = service.createTrademarkTokenFrequencyFormGroup(sampleWithRequiredData);

        const trademarkTokenFrequency = service.getTrademarkTokenFrequency(formGroup) as any;

        expect(trademarkTokenFrequency).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITrademarkTokenFrequency should not enable id FormControl', () => {
        const formGroup = service.createTrademarkTokenFrequencyFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTrademarkTokenFrequency should disable id FormControl', () => {
        const formGroup = service.createTrademarkTokenFrequencyFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
