import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { TokenPhoneticDetailComponent } from './token-phonetic-detail.component';

describe('TokenPhonetic Management Detail Component', () => {
  let comp: TokenPhoneticDetailComponent;
  let fixture: ComponentFixture<TokenPhoneticDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TokenPhoneticDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./token-phonetic-detail.component').then(m => m.TokenPhoneticDetailComponent),
              resolve: { tokenPhonetic: () => of({ id: 24987 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TokenPhoneticDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TokenPhoneticDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load tokenPhonetic on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TokenPhoneticDetailComponent);

      // THEN
      expect(instance.tokenPhonetic()).toEqual(expect.objectContaining({ id: 24987 }));
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
