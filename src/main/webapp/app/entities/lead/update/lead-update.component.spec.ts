import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { LeadService } from '../service/lead.service';
import { ILead } from '../lead.model';
import { LeadFormService } from './lead-form.service';

import { LeadUpdateComponent } from './lead-update.component';

describe('Lead Management Update Component', () => {
  let comp: LeadUpdateComponent;
  let fixture: ComponentFixture<LeadUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let leadFormService: LeadFormService;
  let leadService: LeadService;
  let employeeService: EmployeeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [LeadUpdateComponent],
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
      .overrideTemplate(LeadUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LeadUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    leadFormService = TestBed.inject(LeadFormService);
    leadService = TestBed.inject(LeadService);
    employeeService = TestBed.inject(EmployeeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Employee query and add missing value', () => {
      const lead: ILead = { id: 456 };
      const assignedTo: IEmployee = { id: 7859 };
      lead.assignedTo = assignedTo;

      const employeeCollection: IEmployee[] = [{ id: 10871 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [assignedTo];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ lead });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining),
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const lead: ILead = { id: 456 };
      const assignedTo: IEmployee = { id: 8590 };
      lead.assignedTo = assignedTo;

      activatedRoute.data = of({ lead });
      comp.ngOnInit();

      expect(comp.employeesSharedCollection).toContain(assignedTo);
      expect(comp.lead).toEqual(lead);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILead>>();
      const lead = { id: 123 };
      jest.spyOn(leadFormService, 'getLead').mockReturnValue(lead);
      jest.spyOn(leadService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ lead });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: lead }));
      saveSubject.complete();

      // THEN
      expect(leadFormService.getLead).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(leadService.update).toHaveBeenCalledWith(expect.objectContaining(lead));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILead>>();
      const lead = { id: 123 };
      jest.spyOn(leadFormService, 'getLead').mockReturnValue({ id: null });
      jest.spyOn(leadService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ lead: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: lead }));
      saveSubject.complete();

      // THEN
      expect(leadFormService.getLead).toHaveBeenCalled();
      expect(leadService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILead>>();
      const lead = { id: 123 };
      jest.spyOn(leadService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ lead });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(leadService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareEmployee', () => {
      it('Should forward to employeeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(employeeService, 'compareEmployee');
        comp.compareEmployee(entity, entity2);
        expect(employeeService.compareEmployee).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
