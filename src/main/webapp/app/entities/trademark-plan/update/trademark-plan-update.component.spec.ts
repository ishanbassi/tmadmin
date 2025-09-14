import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { TrademarkPlanService } from '../service/trademark-plan.service';
import { ITrademarkPlan } from '../trademark-plan.model';
import { TrademarkPlanFormService } from './trademark-plan-form.service';

import { TrademarkPlanUpdateComponent } from './trademark-plan-update.component';

describe('TrademarkPlan Management Update Component', () => {
  let comp: TrademarkPlanUpdateComponent;
  let fixture: ComponentFixture<TrademarkPlanUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let trademarkPlanFormService: TrademarkPlanFormService;
  let trademarkPlanService: TrademarkPlanService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TrademarkPlanUpdateComponent],
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
      .overrideTemplate(TrademarkPlanUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TrademarkPlanUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    trademarkPlanFormService = TestBed.inject(TrademarkPlanFormService);
    trademarkPlanService = TestBed.inject(TrademarkPlanService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const trademarkPlan: ITrademarkPlan = { id: 23847 };

      activatedRoute.data = of({ trademarkPlan });
      comp.ngOnInit();

      expect(comp.trademarkPlan).toEqual(trademarkPlan);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITrademarkPlan>>();
      const trademarkPlan = { id: 27639 };
      jest.spyOn(trademarkPlanFormService, 'getTrademarkPlan').mockReturnValue(trademarkPlan);
      jest.spyOn(trademarkPlanService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trademarkPlan });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: trademarkPlan }));
      saveSubject.complete();

      // THEN
      expect(trademarkPlanFormService.getTrademarkPlan).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(trademarkPlanService.update).toHaveBeenCalledWith(expect.objectContaining(trademarkPlan));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITrademarkPlan>>();
      const trademarkPlan = { id: 27639 };
      jest.spyOn(trademarkPlanFormService, 'getTrademarkPlan').mockReturnValue({ id: null });
      jest.spyOn(trademarkPlanService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trademarkPlan: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: trademarkPlan }));
      saveSubject.complete();

      // THEN
      expect(trademarkPlanFormService.getTrademarkPlan).toHaveBeenCalled();
      expect(trademarkPlanService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITrademarkPlan>>();
      const trademarkPlan = { id: 27639 };
      jest.spyOn(trademarkPlanService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trademarkPlan });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(trademarkPlanService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
