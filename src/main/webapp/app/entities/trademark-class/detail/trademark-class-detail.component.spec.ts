import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { TrademarkClassDetailComponent } from './trademark-class-detail.component';

describe('TrademarkClass Management Detail Component', () => {
  let comp: TrademarkClassDetailComponent;
  let fixture: ComponentFixture<TrademarkClassDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TrademarkClassDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./trademark-class-detail.component').then(m => m.TrademarkClassDetailComponent),
              resolve: { trademarkClass: () => of({ id: 17567 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TrademarkClassDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TrademarkClassDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load trademarkClass on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TrademarkClassDetailComponent);

      // THEN
      expect(instance.trademarkClass()).toEqual(expect.objectContaining({ id: 17567 }));
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
