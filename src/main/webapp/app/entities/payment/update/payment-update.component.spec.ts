import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ITrademark } from 'app/entities/trademark/trademark.model';
import { TrademarkService } from 'app/entities/trademark/service/trademark.service';
import { PaymentService } from '../service/payment.service';
import { IPayment } from '../payment.model';
import { PaymentFormService } from './payment-form.service';

import { PaymentUpdateComponent } from './payment-update.component';

describe('Payment Management Update Component', () => {
  let comp: PaymentUpdateComponent;
  let fixture: ComponentFixture<PaymentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let paymentFormService: PaymentFormService;
  let paymentService: PaymentService;
  let trademarkService: TrademarkService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [PaymentUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(PaymentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PaymentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    paymentFormService = TestBed.inject(PaymentFormService);
    paymentService = TestBed.inject(PaymentService);
    trademarkService = TestBed.inject(TrademarkService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Trademark query and add missing value', () => {
      const payment: IPayment = { id: 31232 };
      const trademark: ITrademark = { id: 4352 };
      payment.trademark = trademark;

      const trademarkCollection: ITrademark[] = [{ id: 4352 }];
      jest.spyOn(trademarkService, 'query').mockReturnValue(of(new HttpResponse({ body: trademarkCollection })));
      const additionalTrademarks = [trademark];
      const expectedCollection: ITrademark[] = [...additionalTrademarks, ...trademarkCollection];
      jest.spyOn(trademarkService, 'addTrademarkToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ payment });
      comp.ngOnInit();

      expect(trademarkService.query).toHaveBeenCalled();
      expect(trademarkService.addTrademarkToCollectionIfMissing).toHaveBeenCalledWith(
        trademarkCollection,
        ...additionalTrademarks.map(expect.objectContaining),
      );
      expect(comp.trademarksSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const payment: IPayment = { id: 31232 };
      const trademark: ITrademark = { id: 4352 };
      payment.trademark = trademark;

      activatedRoute.data = of({ payment });
      comp.ngOnInit();

      expect(comp.trademarksSharedCollection).toContainEqual(trademark);
      expect(comp.payment).toEqual(payment);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPayment>>();
      const payment = { id: 20208 };
      jest.spyOn(paymentFormService, 'getPayment').mockReturnValue(payment);
      jest.spyOn(paymentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ payment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: payment }));
      saveSubject.complete();

      // THEN
      expect(paymentFormService.getPayment).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(paymentService.update).toHaveBeenCalledWith(expect.objectContaining(payment));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPayment>>();
      const payment = { id: 20208 };
      jest.spyOn(paymentFormService, 'getPayment').mockReturnValue({ id: null });
      jest.spyOn(paymentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ payment: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: payment }));
      saveSubject.complete();

      // THEN
      expect(paymentFormService.getPayment).toHaveBeenCalled();
      expect(paymentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPayment>>();
      const payment = { id: 20208 };
      jest.spyOn(paymentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ payment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(paymentService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTrademark', () => {
      it('should forward to trademarkService', () => {
        const entity = { id: 4352 };
        const entity2 = { id: 3769 };
        jest.spyOn(trademarkService, 'compareTrademark');
        comp.compareTrademark(entity, entity2);
        expect(trademarkService.compareTrademark).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
