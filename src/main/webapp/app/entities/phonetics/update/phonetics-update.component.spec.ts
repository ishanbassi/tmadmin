import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ITrademark } from 'app/entities/trademark/trademark.model';
import { TrademarkService } from 'app/entities/trademark/service/trademark.service';
import { PhoneticsService } from '../service/phonetics.service';
import { IPhonetics } from '../phonetics.model';
import { PhoneticsFormService } from './phonetics-form.service';

import { PhoneticsUpdateComponent } from './phonetics-update.component';

describe('Phonetics Management Update Component', () => {
  let comp: PhoneticsUpdateComponent;
  let fixture: ComponentFixture<PhoneticsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let phoneticsFormService: PhoneticsFormService;
  let phoneticsService: PhoneticsService;
  let trademarkService: TrademarkService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [PhoneticsUpdateComponent],
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
      .overrideTemplate(PhoneticsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PhoneticsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    phoneticsFormService = TestBed.inject(PhoneticsFormService);
    phoneticsService = TestBed.inject(PhoneticsService);
    trademarkService = TestBed.inject(TrademarkService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Trademark query and add missing value', () => {
      const phonetics: IPhonetics = { id: 456 };
      const trademark: ITrademark = { id: 5453 };
      phonetics.trademark = trademark;

      const trademarkCollection: ITrademark[] = [{ id: 16898 }];
      jest.spyOn(trademarkService, 'query').mockReturnValue(of(new HttpResponse({ body: trademarkCollection })));
      const additionalTrademarks = [trademark];
      const expectedCollection: ITrademark[] = [...additionalTrademarks, ...trademarkCollection];
      jest.spyOn(trademarkService, 'addTrademarkToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ phonetics });
      comp.ngOnInit();

      expect(trademarkService.query).toHaveBeenCalled();
      expect(trademarkService.addTrademarkToCollectionIfMissing).toHaveBeenCalledWith(
        trademarkCollection,
        ...additionalTrademarks.map(expect.objectContaining),
      );
      expect(comp.trademarksSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const phonetics: IPhonetics = { id: 456 };
      const trademark: ITrademark = { id: 11332 };
      phonetics.trademark = trademark;

      activatedRoute.data = of({ phonetics });
      comp.ngOnInit();

      expect(comp.trademarksSharedCollection).toContain(trademark);
      expect(comp.phonetics).toEqual(phonetics);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPhonetics>>();
      const phonetics = { id: 123 };
      jest.spyOn(phoneticsFormService, 'getPhonetics').mockReturnValue(phonetics);
      jest.spyOn(phoneticsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ phonetics });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: phonetics }));
      saveSubject.complete();

      // THEN
      expect(phoneticsFormService.getPhonetics).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(phoneticsService.update).toHaveBeenCalledWith(expect.objectContaining(phonetics));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPhonetics>>();
      const phonetics = { id: 123 };
      jest.spyOn(phoneticsFormService, 'getPhonetics').mockReturnValue({ id: null });
      jest.spyOn(phoneticsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ phonetics: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: phonetics }));
      saveSubject.complete();

      // THEN
      expect(phoneticsFormService.getPhonetics).toHaveBeenCalled();
      expect(phoneticsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPhonetics>>();
      const phonetics = { id: 123 };
      jest.spyOn(phoneticsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ phonetics });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(phoneticsService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTrademark', () => {
      it('Should forward to trademarkService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(trademarkService, 'compareTrademark');
        comp.compareTrademark(entity, entity2);
        expect(trademarkService.compareTrademark).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
