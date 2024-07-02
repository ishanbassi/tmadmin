import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

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

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const publishedTm: IPublishedTm = { id: 456 };

      activatedRoute.data = of({ publishedTm });
      comp.ngOnInit();

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
});
