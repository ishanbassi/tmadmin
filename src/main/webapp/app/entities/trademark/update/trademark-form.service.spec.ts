import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../trademark.test-samples';

import { TrademarkFormService } from './trademark-form.service';

describe('Trademark Form Service', () => {
  let service: TrademarkFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TrademarkFormService);
  });

  describe('Service methods', () => {
    describe('createTrademarkFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTrademarkFormGroup();

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
            source: expect.any(Object),
            planType: expect.any(Object),
            lead: expect.any(Object),
            user: expect.any(Object),
            trademarkClasses: expect.any(Object),
          }),
        );
      });

      it('passing ITrademark should create a new form with FormGroup', () => {
        const formGroup = service.createTrademarkFormGroup(sampleWithRequiredData);

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
            source: expect.any(Object),
            planType: expect.any(Object),
            lead: expect.any(Object),
            user: expect.any(Object),
            trademarkClasses: expect.any(Object),
          }),
        );
      });
    });

    describe('getTrademark', () => {
      it('should return NewTrademark for default Trademark initial value', () => {
        const formGroup = service.createTrademarkFormGroup(sampleWithNewData);

        const trademark = service.getTrademark(formGroup) as any;

        expect(trademark).toMatchObject(sampleWithNewData);
      });

      it('should return NewTrademark for empty Trademark initial value', () => {
        const formGroup = service.createTrademarkFormGroup();

        const trademark = service.getTrademark(formGroup) as any;

        expect(trademark).toMatchObject({});
      });

      it('should return ITrademark', () => {
        const formGroup = service.createTrademarkFormGroup(sampleWithRequiredData);

        const trademark = service.getTrademark(formGroup) as any;

        expect(trademark).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITrademark should not enable id FormControl', () => {
        const formGroup = service.createTrademarkFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTrademark should disable id FormControl', () => {
        const formGroup = service.createTrademarkFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
