import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ITrademark } from 'app/entities/trademark/trademark.model';
import { TrademarkService } from 'app/entities/trademark/service/trademark.service';
import { TrademarkTokenService } from '../service/trademark-token.service';
import { ITrademarkToken } from '../trademark-token.model';
import { TrademarkTokenFormService } from './trademark-token-form.service';

import { TrademarkTokenUpdateComponent } from './trademark-token-update.component';

describe('TrademarkToken Management Update Component', () => {
  let comp: TrademarkTokenUpdateComponent;
  let fixture: ComponentFixture<TrademarkTokenUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let trademarkTokenFormService: TrademarkTokenFormService;
  let trademarkTokenService: TrademarkTokenService;
  let trademarkService: TrademarkService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TrademarkTokenUpdateComponent],
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
      .overrideTemplate(TrademarkTokenUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TrademarkTokenUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    trademarkTokenFormService = TestBed.inject(TrademarkTokenFormService);
    trademarkTokenService = TestBed.inject(TrademarkTokenService);
    trademarkService = TestBed.inject(TrademarkService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Trademark query and add missing value', () => {
      const trademarkToken: ITrademarkToken = { id: 17385 };
      const trademark: ITrademark = { id: 4352 };
      trademarkToken.trademark = trademark;

      const trademarkCollection: ITrademark[] = [{ id: 4352 }];
      jest.spyOn(trademarkService, 'query').mockReturnValue(of(new HttpResponse({ body: trademarkCollection })));
      const additionalTrademarks = [trademark];
      const expectedCollection: ITrademark[] = [...additionalTrademarks, ...trademarkCollection];
      jest.spyOn(trademarkService, 'addTrademarkToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ trademarkToken });
      comp.ngOnInit();

      expect(trademarkService.query).toHaveBeenCalled();
      expect(trademarkService.addTrademarkToCollectionIfMissing).toHaveBeenCalledWith(
        trademarkCollection,
        ...additionalTrademarks.map(expect.objectContaining),
      );
      expect(comp.trademarksSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const trademarkToken: ITrademarkToken = { id: 17385 };
      const trademark: ITrademark = { id: 4352 };
      trademarkToken.trademark = trademark;

      activatedRoute.data = of({ trademarkToken });
      comp.ngOnInit();

      expect(comp.trademarksSharedCollection).toContainEqual(trademark);
      expect(comp.trademarkToken).toEqual(trademarkToken);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITrademarkToken>>();
      const trademarkToken = { id: 1043 };
      jest.spyOn(trademarkTokenFormService, 'getTrademarkToken').mockReturnValue(trademarkToken);
      jest.spyOn(trademarkTokenService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trademarkToken });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: trademarkToken }));
      saveSubject.complete();

      // THEN
      expect(trademarkTokenFormService.getTrademarkToken).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(trademarkTokenService.update).toHaveBeenCalledWith(expect.objectContaining(trademarkToken));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITrademarkToken>>();
      const trademarkToken = { id: 1043 };
      jest.spyOn(trademarkTokenFormService, 'getTrademarkToken').mockReturnValue({ id: null });
      jest.spyOn(trademarkTokenService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trademarkToken: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: trademarkToken }));
      saveSubject.complete();

      // THEN
      expect(trademarkTokenFormService.getTrademarkToken).toHaveBeenCalled();
      expect(trademarkTokenService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITrademarkToken>>();
      const trademarkToken = { id: 1043 };
      jest.spyOn(trademarkTokenService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trademarkToken });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(trademarkTokenService.update).toHaveBeenCalled();
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
