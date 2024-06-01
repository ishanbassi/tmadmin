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
              component: PhoneticsDetailComponent,
              resolve: { phonetics: () => of({ id: 123 }) },
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
    it('Should load phonetics on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PhoneticsDetailComponent);

      // THEN
      expect(instance.phonetics()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
