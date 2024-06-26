import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { TmAgentService } from '../service/tm-agent.service';
import { ITmAgent } from '../tm-agent.model';
import { TmAgentFormService } from './tm-agent-form.service';

import { TmAgentUpdateComponent } from './tm-agent-update.component';

describe('TmAgent Management Update Component', () => {
  let comp: TmAgentUpdateComponent;
  let fixture: ComponentFixture<TmAgentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tmAgentFormService: TmAgentFormService;
  let tmAgentService: TmAgentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, TmAgentUpdateComponent],
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
      .overrideTemplate(TmAgentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TmAgentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tmAgentFormService = TestBed.inject(TmAgentFormService);
    tmAgentService = TestBed.inject(TmAgentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const tmAgent: ITmAgent = { id: 456 };

      activatedRoute.data = of({ tmAgent });
      comp.ngOnInit();

      expect(comp.tmAgent).toEqual(tmAgent);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITmAgent>>();
      const tmAgent = { id: 123 };
      jest.spyOn(tmAgentFormService, 'getTmAgent').mockReturnValue(tmAgent);
      jest.spyOn(tmAgentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tmAgent });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tmAgent }));
      saveSubject.complete();

      // THEN
      expect(tmAgentFormService.getTmAgent).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(tmAgentService.update).toHaveBeenCalledWith(expect.objectContaining(tmAgent));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITmAgent>>();
      const tmAgent = { id: 123 };
      jest.spyOn(tmAgentFormService, 'getTmAgent').mockReturnValue({ id: null });
      jest.spyOn(tmAgentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tmAgent: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tmAgent }));
      saveSubject.complete();

      // THEN
      expect(tmAgentFormService.getTmAgent).toHaveBeenCalled();
      expect(tmAgentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITmAgent>>();
      const tmAgent = { id: 123 };
      jest.spyOn(tmAgentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tmAgent });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tmAgentService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
