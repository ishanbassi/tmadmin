import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { PublishedTmPhoneticsDetailComponent } from './published-tm-phonetics-detail.component';

describe('PublishedTmPhonetics Management Detail Component', () => {
  let comp: PublishedTmPhoneticsDetailComponent;
  let fixture: ComponentFixture<PublishedTmPhoneticsDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PublishedTmPhoneticsDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: PublishedTmPhoneticsDetailComponent,
              resolve: { publishedTmPhonetics: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(PublishedTmPhoneticsDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PublishedTmPhoneticsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load publishedTmPhonetics on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PublishedTmPhoneticsDetailComponent);

      // THEN
      expect(instance.publishedTmPhonetics()).toEqual(expect.objectContaining({ id: 123 }));
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
