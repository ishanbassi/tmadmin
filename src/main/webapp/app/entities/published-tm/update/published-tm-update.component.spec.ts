import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ITmAgent } from 'app/entities/tm-agent/tm-agent.model';
import { TmAgentService } from 'app/entities/tm-agent/service/tm-agent.service';
import { PublishedTmService } from '../service/published-tm.service';
import { IPublishedTm } from '../published-tm.model';
import { PublishedTmFormService } from './published-tm-form.service';

import { PublishedTmUpdateComponent } from './published-tm-update.component';

describe('PublishedTm Management Update Component', () => {
  let comp: PublishedTmUpdateComponent;
  let fixture: ComponentFixture<PublishedTmUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let publishedTmFormService: PublishedTmFormService;
  let publishedTmService: PublishedTmService;
  let tmAgentService: TmAgentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [PublishedTmUpdateComponent],
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
      .overrideTemplate(PublishedTmUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PublishedTmUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    publishedTmFormService = TestBed.inject(PublishedTmFormService);
    publishedTmService = TestBed.inject(PublishedTmService);
    tmAgentService = TestBed.inject(TmAgentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call TmAgent query and add missing value', () => {
      const publishedTm: IPublishedTm = { id: 456 };
      const tmAgent: ITmAgent = { id: 23711 };
      publishedTm.tmAgent = tmAgent;

      const tmAgentCollection: ITmAgent[] = [{ id: 747 }];
      jest.spyOn(tmAgentService, 'query').mockReturnValue(of(new HttpResponse({ body: tmAgentCollection })));
      const additionalTmAgents = [tmAgent];
      const expectedCollection: ITmAgent[] = [...additionalTmAgents, ...tmAgentCollection];
      jest.spyOn(tmAgentService, 'addTmAgentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ publishedTm });
      comp.ngOnInit();

      expect(tmAgentService.query).toHaveBeenCalled();
      expect(tmAgentService.addTmAgentToCollectionIfMissing).toHaveBeenCalledWith(
        tmAgentCollection,
        ...additionalTmAgents.map(expect.objectContaining),
      );
      expect(comp.tmAgentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const publishedTm: IPublishedTm = { id: 456 };
      const tmAgent: ITmAgent = { id: 13664 };
      publishedTm.tmAgent = tmAgent;

      activatedRoute.data = of({ publishedTm });
      comp.ngOnInit();

      expect(comp.tmAgentsSharedCollection).toContain(tmAgent);
      expect(comp.publishedTm).toEqual(publishedTm);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPublishedTm>>();
      const publishedTm = { id: 123 };
      jest.spyOn(publishedTmFormService, 'getPublishedTm').mockReturnValue(publishedTm);
      jest.spyOn(publishedTmService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ publishedTm });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: publishedTm }));
      saveSubject.complete();

      // THEN
      expect(publishedTmFormService.getPublishedTm).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(publishedTmService.update).toHaveBeenCalledWith(expect.objectContaining(publishedTm));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPublishedTm>>();
      const publishedTm = { id: 123 };
      jest.spyOn(publishedTmFormService, 'getPublishedTm').mockReturnValue({ id: null });
      jest.spyOn(publishedTmService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ publishedTm: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: publishedTm }));
      saveSubject.complete();

      // THEN
      expect(publishedTmFormService.getPublishedTm).toHaveBeenCalled();
      expect(publishedTmService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPublishedTm>>();
      const publishedTm = { id: 123 };
      jest.spyOn(publishedTmService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ publishedTm });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(publishedTmService.update).toHaveBeenCalled();
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
