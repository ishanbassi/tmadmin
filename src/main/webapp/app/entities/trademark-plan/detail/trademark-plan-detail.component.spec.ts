import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { TrademarkPlanDetailComponent } from './trademark-plan-detail.component';

describe('TrademarkPlan Management Detail Component', () => {
  let comp: TrademarkPlanDetailComponent;
  let fixture: ComponentFixture<TrademarkPlanDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TrademarkPlanDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./trademark-plan-detail.component').then(m => m.TrademarkPlanDetailComponent),
              resolve: { trademarkPlan: () => of({ id: 27639 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TrademarkPlanDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TrademarkPlanDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load trademarkPlan on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TrademarkPlanDetailComponent);

      // THEN
      expect(instance.trademarkPlan()).toEqual(expect.objectContaining({ id: 27639 }));
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
