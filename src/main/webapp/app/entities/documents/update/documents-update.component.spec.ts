import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ITrademark } from 'app/entities/trademark/trademark.model';
import { TrademarkService } from 'app/entities/trademark/service/trademark.service';
import { DocumentsService } from '../service/documents.service';
import { IDocuments } from '../documents.model';
import { DocumentsFormService } from './documents-form.service';

import { DocumentsUpdateComponent } from './documents-update.component';

describe('Documents Management Update Component', () => {
  let comp: DocumentsUpdateComponent;
  let fixture: ComponentFixture<DocumentsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let documentsFormService: DocumentsFormService;
  let documentsService: DocumentsService;
  let trademarkService: TrademarkService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [DocumentsUpdateComponent],
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
      .overrideTemplate(DocumentsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DocumentsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    documentsFormService = TestBed.inject(DocumentsFormService);
    documentsService = TestBed.inject(DocumentsService);
    trademarkService = TestBed.inject(TrademarkService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Trademark query and add missing value', () => {
      const documents: IDocuments = { id: 1265 };
      const trademark: ITrademark = { id: 4352 };
      documents.trademark = trademark;

      const trademarkCollection: ITrademark[] = [{ id: 4352 }];
      jest.spyOn(trademarkService, 'query').mockReturnValue(of(new HttpResponse({ body: trademarkCollection })));
      const additionalTrademarks = [trademark];
      const expectedCollection: ITrademark[] = [...additionalTrademarks, ...trademarkCollection];
      jest.spyOn(trademarkService, 'addTrademarkToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ documents });
      comp.ngOnInit();

      expect(trademarkService.query).toHaveBeenCalled();
      expect(trademarkService.addTrademarkToCollectionIfMissing).toHaveBeenCalledWith(
        trademarkCollection,
        ...additionalTrademarks.map(expect.objectContaining),
      );
      expect(comp.trademarksSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const documents: IDocuments = { id: 1265 };
      const trademark: ITrademark = { id: 4352 };
      documents.trademark = trademark;

      activatedRoute.data = of({ documents });
      comp.ngOnInit();

      expect(comp.trademarksSharedCollection).toContainEqual(trademark);
      expect(comp.documents).toEqual(documents);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocuments>>();
      const documents = { id: 24452 };
      jest.spyOn(documentsFormService, 'getDocuments').mockReturnValue(documents);
      jest.spyOn(documentsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documents });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documents }));
      saveSubject.complete();

      // THEN
      expect(documentsFormService.getDocuments).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(documentsService.update).toHaveBeenCalledWith(expect.objectContaining(documents));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocuments>>();
      const documents = { id: 24452 };
      jest.spyOn(documentsFormService, 'getDocuments').mockReturnValue({ id: null });
      jest.spyOn(documentsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documents: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documents }));
      saveSubject.complete();

      // THEN
      expect(documentsFormService.getDocuments).toHaveBeenCalled();
      expect(documentsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocuments>>();
      const documents = { id: 24452 };
      jest.spyOn(documentsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documents });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(documentsService.update).toHaveBeenCalled();
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
