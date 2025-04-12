import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { TmAgentDetailComponent } from './tm-agent-detail.component';

describe('TmAgent Management Detail Component', () => {
  let comp: TmAgentDetailComponent;
  let fixture: ComponentFixture<TmAgentDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TmAgentDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./tm-agent-detail.component').then(m => m.TmAgentDetailComponent),
              resolve: { tmAgent: () => of({ id: 9499 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TmAgentDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TmAgentDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load tmAgent on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TmAgentDetailComponent);

      // THEN
      expect(instance.tmAgent()).toEqual(expect.objectContaining({ id: 9499 }));
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
