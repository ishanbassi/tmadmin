import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { TrademarkTokenFrequencyService } from '../service/trademark-token-frequency.service';
import { ITrademarkTokenFrequency } from '../trademark-token-frequency.model';
import { TrademarkTokenFrequencyFormService } from './trademark-token-frequency-form.service';

import { TrademarkTokenFrequencyUpdateComponent } from './trademark-token-frequency-update.component';

describe('TrademarkTokenFrequency Management Update Component', () => {
  let comp: TrademarkTokenFrequencyUpdateComponent;
  let fixture: ComponentFixture<TrademarkTokenFrequencyUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let trademarkTokenFrequencyFormService: TrademarkTokenFrequencyFormService;
  let trademarkTokenFrequencyService: TrademarkTokenFrequencyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TrademarkTokenFrequencyUpdateComponent],
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
      .overrideTemplate(TrademarkTokenFrequencyUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TrademarkTokenFrequencyUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    trademarkTokenFrequencyFormService = TestBed.inject(TrademarkTokenFrequencyFormService);
    trademarkTokenFrequencyService = TestBed.inject(TrademarkTokenFrequencyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const trademarkTokenFrequency: ITrademarkTokenFrequency = { id: 28547 };

      activatedRoute.data = of({ trademarkTokenFrequency });
      comp.ngOnInit();

      expect(comp.trademarkTokenFrequency).toEqual(trademarkTokenFrequency);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITrademarkTokenFrequency>>();
      const trademarkTokenFrequency = { id: 15292 };
      jest.spyOn(trademarkTokenFrequencyFormService, 'getTrademarkTokenFrequency').mockReturnValue(trademarkTokenFrequency);
      jest.spyOn(trademarkTokenFrequencyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trademarkTokenFrequency });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: trademarkTokenFrequency }));
      saveSubject.complete();

      // THEN
      expect(trademarkTokenFrequencyFormService.getTrademarkTokenFrequency).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(trademarkTokenFrequencyService.update).toHaveBeenCalledWith(expect.objectContaining(trademarkTokenFrequency));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITrademarkTokenFrequency>>();
      const trademarkTokenFrequency = { id: 15292 };
      jest.spyOn(trademarkTokenFrequencyFormService, 'getTrademarkTokenFrequency').mockReturnValue({ id: null });
      jest.spyOn(trademarkTokenFrequencyService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trademarkTokenFrequency: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: trademarkTokenFrequency }));
      saveSubject.complete();

      // THEN
      expect(trademarkTokenFrequencyFormService.getTrademarkTokenFrequency).toHaveBeenCalled();
      expect(trademarkTokenFrequencyService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITrademarkTokenFrequency>>();
      const trademarkTokenFrequency = { id: 15292 };
      jest.spyOn(trademarkTokenFrequencyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trademarkTokenFrequency });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(trademarkTokenFrequencyService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
