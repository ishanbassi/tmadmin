import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { TrademarkService } from '../service/trademark.service';
import { ITrademark } from '../trademark.model';
import { TrademarkFormService } from './trademark-form.service';

import { TrademarkUpdateComponent } from './trademark-update.component';

describe('Trademark Management Update Component', () => {
  let comp: TrademarkUpdateComponent;
  let fixture: ComponentFixture<TrademarkUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let trademarkFormService: TrademarkFormService;
  let trademarkService: TrademarkService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, TrademarkUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(TrademarkUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TrademarkUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    trademarkFormService = TestBed.inject(TrademarkFormService);
    trademarkService = TestBed.inject(TrademarkService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const trademark: ITrademark = { id: 456 };

      activatedRoute.data = of({ trademark });
      comp.ngOnInit();

      expect(comp.trademark).toEqual(trademark);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITrademark>>();
      const trademark = { id: 123 };
      jest.spyOn(trademarkFormService, 'getTrademark').mockReturnValue(trademark);
      jest.spyOn(trademarkService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trademark });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: trademark }));
      saveSubject.complete();

      // THEN
      expect(trademarkFormService.getTrademark).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(trademarkService.update).toHaveBeenCalledWith(expect.objectContaining(trademark));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITrademark>>();
      const trademark = { id: 123 };
      jest.spyOn(trademarkFormService, 'getTrademark').mockReturnValue({ id: null });
      jest.spyOn(trademarkService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trademark: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: trademark }));
      saveSubject.complete();

      // THEN
      expect(trademarkFormService.getTrademark).toHaveBeenCalled();
      expect(trademarkService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITrademark>>();
      const trademark = { id: 123 };
      jest.spyOn(trademarkService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trademark });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(trademarkService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
