import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { PublishedTmDetailComponent } from './published-tm-detail.component';

describe('PublishedTm Management Detail Component', () => {
  let comp: PublishedTmDetailComponent;
  let fixture: ComponentFixture<PublishedTmDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PublishedTmDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: PublishedTmDetailComponent,
              resolve: { publishedTm: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(PublishedTmDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PublishedTmDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load publishedTm on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PublishedTmDetailComponent);

      // THEN
      expect(instance.publishedTm()).toEqual(expect.objectContaining({ id: 123 }));
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
