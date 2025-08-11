import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../documents.test-samples';

import { DocumentsFormService } from './documents-form.service';

describe('Documents Form Service', () => {
  let service: DocumentsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DocumentsFormService);
  });

  describe('Service methods', () => {
    describe('createDocumentsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDocumentsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentType: expect.any(Object),
            fileContentType: expect.any(Object),
            fileName: expect.any(Object),
            fileUrl: expect.any(Object),
            createdDate: expect.any(Object),
            modifiedDate: expect.any(Object),
            deleted: expect.any(Object),
            trademark: expect.any(Object),
          }),
        );
      });

      it('passing IDocuments should create a new form with FormGroup', () => {
        const formGroup = service.createDocumentsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            documentType: expect.any(Object),
            fileContentType: expect.any(Object),
            fileName: expect.any(Object),
            fileUrl: expect.any(Object),
            createdDate: expect.any(Object),
            modifiedDate: expect.any(Object),
            deleted: expect.any(Object),
            trademark: expect.any(Object),
          }),
        );
      });
    });

    describe('getDocuments', () => {
      it('should return NewDocuments for default Documents initial value', () => {
        const formGroup = service.createDocumentsFormGroup(sampleWithNewData);

        const documents = service.getDocuments(formGroup) as any;

        expect(documents).toMatchObject(sampleWithNewData);
      });

      it('should return NewDocuments for empty Documents initial value', () => {
        const formGroup = service.createDocumentsFormGroup();

        const documents = service.getDocuments(formGroup) as any;

        expect(documents).toMatchObject({});
      });

      it('should return IDocuments', () => {
        const formGroup = service.createDocumentsFormGroup(sampleWithRequiredData);

        const documents = service.getDocuments(formGroup) as any;

        expect(documents).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDocuments should not enable id FormControl', () => {
        const formGroup = service.createDocumentsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDocuments should disable id FormControl', () => {
        const formGroup = service.createDocumentsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
