import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ICompany } from 'app/entities/company/company.model';
import { CompanyService } from 'app/entities/company/service/company.service';
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
  let companyService: CompanyService;

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
    companyService = TestBed.inject(CompanyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Company query and add missing value', () => {
      const trademark: ITrademark = { id: 3769 };
      const company: ICompany = { id: 29751 };
      trademark.company = company;

      const companyCollection: ICompany[] = [{ id: 29751 }];
      jest.spyOn(companyService, 'query').mockReturnValue(of(new HttpResponse({ body: companyCollection })));
      const additionalCompanies = [company];
      const expectedCollection: ICompany[] = [...additionalCompanies, ...companyCollection];
      jest.spyOn(companyService, 'addCompanyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ trademark });
      comp.ngOnInit();

      expect(companyService.query).toHaveBeenCalled();
      expect(companyService.addCompanyToCollectionIfMissing).toHaveBeenCalledWith(
        companyCollection,
        ...additionalCompanies.map(expect.objectContaining),
      );
      expect(comp.companiesSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const trademark: ITrademark = { id: 3769 };
      const company: ICompany = { id: 29751 };
      trademark.company = company;

      activatedRoute.data = of({ trademark });
      comp.ngOnInit();

      expect(comp.companiesSharedCollection).toContainEqual(company);
      expect(comp.trademark).toEqual(trademark);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITrademark>>();
      const trademark = { id: 4352 };
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

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITrademark>>();
      const trademark = { id: 4352 };
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

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITrademark>>();
      const trademark = { id: 4352 };
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
    describe('compareCompany', () => {
      it('should forward to companyService', () => {
        const entity = { id: 29751 };
        const entity2 = { id: 7586 };
        jest.spyOn(companyService, 'compareCompany');
        comp.compareCompany(entity, entity2);
        expect(companyService.compareCompany).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
