import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { TrademarkDetailComponent } from './trademark-detail.component';

describe('Trademark Management Detail Component', () => {
  let comp: TrademarkDetailComponent;
  let fixture: ComponentFixture<TrademarkDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TrademarkDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./trademark-detail.component').then(m => m.TrademarkDetailComponent),
              resolve: { trademark: () => of({ id: 4352 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TrademarkDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TrademarkDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load trademark on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TrademarkDetailComponent);

      // THEN
      expect(instance.trademark()).toEqual(expect.objectContaining({ id: 4352 }));
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
