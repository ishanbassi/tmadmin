import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { ITmAgent } from 'app/entities/tm-agent/tm-agent.model';
import { TmAgentService } from 'app/entities/tm-agent/service/tm-agent.service';
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
  let tmAgentService: TmAgentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TrademarkUpdateComponent],
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
      .overrideTemplate(TrademarkUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TrademarkUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    trademarkFormService = TestBed.inject(TrademarkFormService);
    trademarkService = TestBed.inject(TrademarkService);
    tmAgentService = TestBed.inject(TmAgentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call TmAgent query and add missing value', () => {
      const trademark: ITrademark = { id: 456 };
      const tmAgent: ITmAgent = { id: 27147 };
      trademark.tmAgent = tmAgent;

      const tmAgentCollection: ITmAgent[] = [{ id: 22640 }];
      jest.spyOn(tmAgentService, 'query').mockReturnValue(of(new HttpResponse({ body: tmAgentCollection })));
      const additionalTmAgents = [tmAgent];
      const expectedCollection: ITmAgent[] = [...additionalTmAgents, ...tmAgentCollection];
      jest.spyOn(tmAgentService, 'addTmAgentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ trademark });
      comp.ngOnInit();

      expect(tmAgentService.query).toHaveBeenCalled();
      expect(tmAgentService.addTmAgentToCollectionIfMissing).toHaveBeenCalledWith(
        tmAgentCollection,
        ...additionalTmAgents.map(expect.objectContaining),
      );
      expect(comp.tmAgentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const trademark: ITrademark = { id: 456 };
      const tmAgent: ITmAgent = { id: 27355 };
      trademark.tmAgent = tmAgent;

      activatedRoute.data = of({ trademark });
      comp.ngOnInit();

      expect(comp.tmAgentsSharedCollection).toContain(tmAgent);
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

  describe('Compare relationships', () => {
    describe('compareTmAgent', () => {
      it('Should forward to tmAgentService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(tmAgentService, 'compareTmAgent');
        comp.compareTmAgent(entity, entity2);
        expect(tmAgentService.compareTmAgent).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
