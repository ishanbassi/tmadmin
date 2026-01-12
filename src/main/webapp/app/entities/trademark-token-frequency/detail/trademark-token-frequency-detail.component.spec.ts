import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { TrademarkTokenFrequencyDetailComponent } from './trademark-token-frequency-detail.component';

describe('TrademarkTokenFrequency Management Detail Component', () => {
  let comp: TrademarkTokenFrequencyDetailComponent;
  let fixture: ComponentFixture<TrademarkTokenFrequencyDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TrademarkTokenFrequencyDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () =>
                import('./trademark-token-frequency-detail.component').then(m => m.TrademarkTokenFrequencyDetailComponent),
              resolve: { trademarkTokenFrequency: () => of({ id: 15292 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TrademarkTokenFrequencyDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TrademarkTokenFrequencyDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load trademarkTokenFrequency on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TrademarkTokenFrequencyDetailComponent);

      // THEN
      expect(instance.trademarkTokenFrequency()).toEqual(expect.objectContaining({ id: 15292 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
