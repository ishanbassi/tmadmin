import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { UserEventsTrackingDetailComponent } from './user-events-tracking-detail.component';

describe('UserEventsTracking Management Detail Component', () => {
  let comp: UserEventsTrackingDetailComponent;
  let fixture: ComponentFixture<UserEventsTrackingDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserEventsTrackingDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./user-events-tracking-detail.component').then(m => m.UserEventsTrackingDetailComponent),
              resolve: { userEventsTracking: () => of({ id: 1044 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(UserEventsTrackingDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UserEventsTrackingDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load userEventsTracking on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', UserEventsTrackingDetailComponent);

      // THEN
      expect(instance.userEventsTracking()).toEqual(expect.objectContaining({ id: 1044 }));
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
