import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../trademark-class.test-samples';

import { TrademarkClassFormService } from './trademark-class-form.service';

describe('TrademarkClass Form Service', () => {
  let service: TrademarkClassFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TrademarkClassFormService);
  });

  describe('Service methods', () => {
    describe('createTrademarkClassFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTrademarkClassFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            tmClass: expect.any(Object),
            keyword: expect.any(Object),
            title: expect.any(Object),
            description: expect.any(Object),
            createdDate: expect.any(Object),
            modifiedDate: expect.any(Object),
            deleted: expect.any(Object),
          }),
        );
      });

      it('passing ITrademarkClass should create a new form with FormGroup', () => {
        const formGroup = service.createTrademarkClassFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            tmClass: expect.any(Object),
            keyword: expect.any(Object),
            title: expect.any(Object),
            description: expect.any(Object),
            createdDate: expect.any(Object),
            modifiedDate: expect.any(Object),
            deleted: expect.any(Object),
          }),
        );
      });
    });

    describe('getTrademarkClass', () => {
      it('should return NewTrademarkClass for default TrademarkClass initial value', () => {
        const formGroup = service.createTrademarkClassFormGroup(sampleWithNewData);

        const trademarkClass = service.getTrademarkClass(formGroup) as any;

        expect(trademarkClass).toMatchObject(sampleWithNewData);
      });

      it('should return NewTrademarkClass for empty TrademarkClass initial value', () => {
        const formGroup = service.createTrademarkClassFormGroup();

        const trademarkClass = service.getTrademarkClass(formGroup) as any;

        expect(trademarkClass).toMatchObject({});
      });

      it('should return ITrademarkClass', () => {
        const formGroup = service.createTrademarkClassFormGroup(sampleWithRequiredData);

        const trademarkClass = service.getTrademarkClass(formGroup) as any;

        expect(trademarkClass).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITrademarkClass should not enable id FormControl', () => {
        const formGroup = service.createTrademarkClassFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTrademarkClass should disable id FormControl', () => {
        const formGroup = service.createTrademarkClassFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
