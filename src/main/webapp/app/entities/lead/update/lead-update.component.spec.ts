import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

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
    it('should call Employee query and add missing value', () => {
      const lead: ILead = { id: 6619 };
      const assignedTo: IEmployee = { id: 1749 };
      lead.assignedTo = assignedTo;

      const employeeCollection: IEmployee[] = [{ id: 1749 }];
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

    it('should update editForm', () => {
      const lead: ILead = { id: 6619 };
      const assignedTo: IEmployee = { id: 1749 };
      lead.assignedTo = assignedTo;

      activatedRoute.data = of({ lead });
      comp.ngOnInit();

      expect(comp.employeesSharedCollection).toContainEqual(assignedTo);
      expect(comp.lead).toEqual(lead);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILead>>();
      const lead = { id: 32296 };
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

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILead>>();
      const lead = { id: 32296 };
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

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILead>>();
      const lead = { id: 32296 };
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
      it('should forward to employeeService', () => {
        const entity = { id: 1749 };
        const entity2 = { id: 1545 };
        jest.spyOn(employeeService, 'compareEmployee');
        comp.compareEmployee(entity, entity2);
        expect(employeeService.compareEmployee).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
