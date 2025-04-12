import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IPublishedTm } from 'app/entities/published-tm/published-tm.model';
import { PublishedTmService } from 'app/entities/published-tm/service/published-tm.service';
import { PublishedTmPhoneticsService } from '../service/published-tm-phonetics.service';
import { IPublishedTmPhonetics } from '../published-tm-phonetics.model';
import { PublishedTmPhoneticsFormService } from './published-tm-phonetics-form.service';

import { PublishedTmPhoneticsUpdateComponent } from './published-tm-phonetics-update.component';

describe('PublishedTmPhonetics Management Update Component', () => {
  let comp: PublishedTmPhoneticsUpdateComponent;
  let fixture: ComponentFixture<PublishedTmPhoneticsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let publishedTmPhoneticsFormService: PublishedTmPhoneticsFormService;
  let publishedTmPhoneticsService: PublishedTmPhoneticsService;
  let publishedTmService: PublishedTmService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [PublishedTmPhoneticsUpdateComponent],
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
      .overrideTemplate(PublishedTmPhoneticsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PublishedTmPhoneticsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    publishedTmPhoneticsFormService = TestBed.inject(PublishedTmPhoneticsFormService);
    publishedTmPhoneticsService = TestBed.inject(PublishedTmPhoneticsService);
    publishedTmService = TestBed.inject(PublishedTmService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call PublishedTm query and add missing value', () => {
      const publishedTmPhonetics: IPublishedTmPhonetics = { id: 3884 };
      const publishedTm: IPublishedTm = { id: 23395 };
      publishedTmPhonetics.publishedTm = publishedTm;

      const publishedTmCollection: IPublishedTm[] = [{ id: 23395 }];
      jest.spyOn(publishedTmService, 'query').mockReturnValue(of(new HttpResponse({ body: publishedTmCollection })));
      const additionalPublishedTms = [publishedTm];
      const expectedCollection: IPublishedTm[] = [...additionalPublishedTms, ...publishedTmCollection];
      jest.spyOn(publishedTmService, 'addPublishedTmToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ publishedTmPhonetics });
      comp.ngOnInit();

      expect(publishedTmService.query).toHaveBeenCalled();
      expect(publishedTmService.addPublishedTmToCollectionIfMissing).toHaveBeenCalledWith(
        publishedTmCollection,
        ...additionalPublishedTms.map(expect.objectContaining),
      );
      expect(comp.publishedTmsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const publishedTmPhonetics: IPublishedTmPhonetics = { id: 3884 };
      const publishedTm: IPublishedTm = { id: 23395 };
      publishedTmPhonetics.publishedTm = publishedTm;

      activatedRoute.data = of({ publishedTmPhonetics });
      comp.ngOnInit();

      expect(comp.publishedTmsSharedCollection).toContainEqual(publishedTm);
      expect(comp.publishedTmPhonetics).toEqual(publishedTmPhonetics);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPublishedTmPhonetics>>();
      const publishedTmPhonetics = { id: 28612 };
      jest.spyOn(publishedTmPhoneticsFormService, 'getPublishedTmPhonetics').mockReturnValue(publishedTmPhonetics);
      jest.spyOn(publishedTmPhoneticsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ publishedTmPhonetics });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: publishedTmPhonetics }));
      saveSubject.complete();

      // THEN
      expect(publishedTmPhoneticsFormService.getPublishedTmPhonetics).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(publishedTmPhoneticsService.update).toHaveBeenCalledWith(expect.objectContaining(publishedTmPhonetics));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPublishedTmPhonetics>>();
      const publishedTmPhonetics = { id: 28612 };
      jest.spyOn(publishedTmPhoneticsFormService, 'getPublishedTmPhonetics').mockReturnValue({ id: null });
      jest.spyOn(publishedTmPhoneticsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ publishedTmPhonetics: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: publishedTmPhonetics }));
      saveSubject.complete();

      // THEN
      expect(publishedTmPhoneticsFormService.getPublishedTmPhonetics).toHaveBeenCalled();
      expect(publishedTmPhoneticsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPublishedTmPhonetics>>();
      const publishedTmPhonetics = { id: 28612 };
      jest.spyOn(publishedTmPhoneticsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ publishedTmPhonetics });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(publishedTmPhoneticsService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePublishedTm', () => {
      it('should forward to publishedTmService', () => {
        const entity = { id: 23395 };
        const entity2 = { id: 3430 };
        jest.spyOn(publishedTmService, 'comparePublishedTm');
        comp.comparePublishedTm(entity, entity2);
        expect(publishedTmService.comparePublishedTm).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
