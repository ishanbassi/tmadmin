import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../trademark-plan.test-samples';

import { TrademarkPlanFormService } from './trademark-plan-form.service';

describe('TrademarkPlan Form Service', () => {
  let service: TrademarkPlanFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TrademarkPlanFormService);
  });

  describe('Service methods', () => {
    describe('createTrademarkPlanFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTrademarkPlanFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            fees: expect.any(Object),
            notes: expect.any(Object),
            createdDate: expect.any(Object),
            deleted: expect.any(Object),
            modifiedDate: expect.any(Object),
          }),
        );
      });

      it('passing ITrademarkPlan should create a new form with FormGroup', () => {
        const formGroup = service.createTrademarkPlanFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            fees: expect.any(Object),
            notes: expect.any(Object),
            createdDate: expect.any(Object),
            deleted: expect.any(Object),
            modifiedDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getTrademarkPlan', () => {
      it('should return NewTrademarkPlan for default TrademarkPlan initial value', () => {
        const formGroup = service.createTrademarkPlanFormGroup(sampleWithNewData);

        const trademarkPlan = service.getTrademarkPlan(formGroup) as any;

        expect(trademarkPlan).toMatchObject(sampleWithNewData);
      });

      it('should return NewTrademarkPlan for empty TrademarkPlan initial value', () => {
        const formGroup = service.createTrademarkPlanFormGroup();

        const trademarkPlan = service.getTrademarkPlan(formGroup) as any;

        expect(trademarkPlan).toMatchObject({});
      });

      it('should return ITrademarkPlan', () => {
        const formGroup = service.createTrademarkPlanFormGroup(sampleWithRequiredData);

        const trademarkPlan = service.getTrademarkPlan(formGroup) as any;

        expect(trademarkPlan).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITrademarkPlan should not enable id FormControl', () => {
        const formGroup = service.createTrademarkPlanFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTrademarkPlan should disable id FormControl', () => {
        const formGroup = service.createTrademarkPlanFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
