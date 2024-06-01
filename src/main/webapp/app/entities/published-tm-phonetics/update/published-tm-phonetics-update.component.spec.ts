import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

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
      imports: [HttpClientTestingModule, PublishedTmPhoneticsUpdateComponent],
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
    it('Should call PublishedTm query and add missing value', () => {
      const publishedTmPhonetics: IPublishedTmPhonetics = { id: 456 };
      const publishedTm: IPublishedTm = { id: 19326 };
      publishedTmPhonetics.publishedTm = publishedTm;

      const publishedTmCollection: IPublishedTm[] = [{ id: 31446 }];
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

    it('Should update editForm', () => {
      const publishedTmPhonetics: IPublishedTmPhonetics = { id: 456 };
      const publishedTm: IPublishedTm = { id: 16810 };
      publishedTmPhonetics.publishedTm = publishedTm;

      activatedRoute.data = of({ publishedTmPhonetics });
      comp.ngOnInit();

      expect(comp.publishedTmsSharedCollection).toContain(publishedTm);
      expect(comp.publishedTmPhonetics).toEqual(publishedTmPhonetics);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPublishedTmPhonetics>>();
      const publishedTmPhonetics = { id: 123 };
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

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPublishedTmPhonetics>>();
      const publishedTmPhonetics = { id: 123 };
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

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPublishedTmPhonetics>>();
      const publishedTmPhonetics = { id: 123 };
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
      it('Should forward to publishedTmService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(publishedTmService, 'comparePublishedTm');
        comp.comparePublishedTm(entity, entity2);
        expect(publishedTmService.comparePublishedTm).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
