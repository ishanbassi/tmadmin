jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, fakeAsync, inject, tick } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { TrademarkTokenFrequencyService } from '../service/trademark-token-frequency.service';

import { TrademarkTokenFrequencyDeleteDialogComponent } from './trademark-token-frequency-delete-dialog.component';

describe('TrademarkTokenFrequency Management Delete Component', () => {
  let comp: TrademarkTokenFrequencyDeleteDialogComponent;
  let fixture: ComponentFixture<TrademarkTokenFrequencyDeleteDialogComponent>;
  let service: TrademarkTokenFrequencyService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TrademarkTokenFrequencyDeleteDialogComponent],
      providers: [provideHttpClient(), NgbActiveModal],
    })
      .overrideTemplate(TrademarkTokenFrequencyDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TrademarkTokenFrequencyDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(TrademarkTokenFrequencyService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      }),
    ));

    it('should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
