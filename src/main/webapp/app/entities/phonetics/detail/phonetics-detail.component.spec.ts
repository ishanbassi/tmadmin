import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { PhoneticsDetailComponent } from './phonetics-detail.component';

describe('Phonetics Management Detail Component', () => {
  let comp: PhoneticsDetailComponent;
  let fixture: ComponentFixture<PhoneticsDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PhoneticsDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./phonetics-detail.component').then(m => m.PhoneticsDetailComponent),
              resolve: { phonetics: () => of({ id: 29047 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(PhoneticsDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PhoneticsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load phonetics on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PhoneticsDetailComponent);

      // THEN
      expect(instance.phonetics()).toEqual(expect.objectContaining({ id: 29047 }));
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
