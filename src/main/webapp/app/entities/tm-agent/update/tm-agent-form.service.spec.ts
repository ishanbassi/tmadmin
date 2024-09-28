import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../tm-agent.test-samples';

import { TmAgentFormService } from './tm-agent-form.service';

describe('TmAgent Form Service', () => {
  let service: TmAgentFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TmAgentFormService);
  });

  describe('Service methods', () => {
    describe('createTmAgentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTmAgentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            agentCode: expect.any(Object),
            firstName: expect.any(Object),
            lastName: expect.any(Object),
            address: expect.any(Object),
            createdDate: expect.any(Object),
            modifiedDate: expect.any(Object),
            deleted: expect.any(Object),
            companyName: expect.any(Object),
          }),
        );
      });

      it('passing ITmAgent should create a new form with FormGroup', () => {
        const formGroup = service.createTmAgentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            agentCode: expect.any(Object),
            firstName: expect.any(Object),
            lastName: expect.any(Object),
            address: expect.any(Object),
            createdDate: expect.any(Object),
            modifiedDate: expect.any(Object),
            deleted: expect.any(Object),
            companyName: expect.any(Object),
          }),
        );
      });
    });

    describe('getTmAgent', () => {
      it('should return NewTmAgent for default TmAgent initial value', () => {
        const formGroup = service.createTmAgentFormGroup(sampleWithNewData);

        const tmAgent = service.getTmAgent(formGroup) as any;

        expect(tmAgent).toMatchObject(sampleWithNewData);
      });

      it('should return NewTmAgent for empty TmAgent initial value', () => {
        const formGroup = service.createTmAgentFormGroup();

        const tmAgent = service.getTmAgent(formGroup) as any;

        expect(tmAgent).toMatchObject({});
      });

      it('should return ITmAgent', () => {
        const formGroup = service.createTmAgentFormGroup(sampleWithRequiredData);

        const tmAgent = service.getTmAgent(formGroup) as any;

        expect(tmAgent).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITmAgent should not enable id FormControl', () => {
        const formGroup = service.createTmAgentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTmAgent should disable id FormControl', () => {
        const formGroup = service.createTmAgentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
