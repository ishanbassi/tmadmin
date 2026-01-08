import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { TrademarkTokenDetailComponent } from './trademark-token-detail.component';

describe('TrademarkToken Management Detail Component', () => {
  let comp: TrademarkTokenDetailComponent;
  let fixture: ComponentFixture<TrademarkTokenDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TrademarkTokenDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./trademark-token-detail.component').then(m => m.TrademarkTokenDetailComponent),
              resolve: { trademarkToken: () => of({ id: 1043 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TrademarkTokenDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TrademarkTokenDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load trademarkToken on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TrademarkTokenDetailComponent);

      // THEN
      expect(instance.trademarkToken()).toEqual(expect.objectContaining({ id: 1043 }));
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
