import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ITrademark } from 'app/entities/trademark/trademark.model';
import { TrademarkService } from 'app/entities/trademark/service/trademark.service';
import { TrademarkClassService } from '../service/trademark-class.service';
import { ITrademarkClass } from '../trademark-class.model';
import { TrademarkClassFormService } from './trademark-class-form.service';

import { TrademarkClassUpdateComponent } from './trademark-class-update.component';

describe('TrademarkClass Management Update Component', () => {
  let comp: TrademarkClassUpdateComponent;
  let fixture: ComponentFixture<TrademarkClassUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let trademarkClassFormService: TrademarkClassFormService;
  let trademarkClassService: TrademarkClassService;
  let trademarkService: TrademarkService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TrademarkClassUpdateComponent],
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
      .overrideTemplate(TrademarkClassUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TrademarkClassUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    trademarkClassFormService = TestBed.inject(TrademarkClassFormService);
    trademarkClassService = TestBed.inject(TrademarkClassService);
    trademarkService = TestBed.inject(TrademarkService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Trademark query and add missing value', () => {
      const trademarkClass: ITrademarkClass = { id: 29079 };
      const trademarks: ITrademark[] = [{ id: 4352 }];
      trademarkClass.trademarks = trademarks;

      const trademarkCollection: ITrademark[] = [{ id: 4352 }];
      jest.spyOn(trademarkService, 'query').mockReturnValue(of(new HttpResponse({ body: trademarkCollection })));
      const additionalTrademarks = [...trademarks];
      const expectedCollection: ITrademark[] = [...additionalTrademarks, ...trademarkCollection];
      jest.spyOn(trademarkService, 'addTrademarkToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ trademarkClass });
      comp.ngOnInit();

      expect(trademarkService.query).toHaveBeenCalled();
      expect(trademarkService.addTrademarkToCollectionIfMissing).toHaveBeenCalledWith(
        trademarkCollection,
        ...additionalTrademarks.map(expect.objectContaining),
      );
      expect(comp.trademarksSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const trademarkClass: ITrademarkClass = { id: 29079 };
      const trademarks: ITrademark = { id: 4352 };
      trademarkClass.trademarks = [trademarks];

      activatedRoute.data = of({ trademarkClass });
      comp.ngOnInit();

      expect(comp.trademarksSharedCollection).toContainEqual(trademarks);
      expect(comp.trademarkClass).toEqual(trademarkClass);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITrademarkClass>>();
      const trademarkClass = { id: 17567 };
      jest.spyOn(trademarkClassFormService, 'getTrademarkClass').mockReturnValue(trademarkClass);
      jest.spyOn(trademarkClassService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trademarkClass });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: trademarkClass }));
      saveSubject.complete();

      // THEN
      expect(trademarkClassFormService.getTrademarkClass).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(trademarkClassService.update).toHaveBeenCalledWith(expect.objectContaining(trademarkClass));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITrademarkClass>>();
      const trademarkClass = { id: 17567 };
      jest.spyOn(trademarkClassFormService, 'getTrademarkClass').mockReturnValue({ id: null });
      jest.spyOn(trademarkClassService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trademarkClass: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: trademarkClass }));
      saveSubject.complete();

      // THEN
      expect(trademarkClassFormService.getTrademarkClass).toHaveBeenCalled();
      expect(trademarkClassService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITrademarkClass>>();
      const trademarkClass = { id: 17567 };
      jest.spyOn(trademarkClassService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trademarkClass });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(trademarkClassService.update).toHaveBeenCalled();
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
