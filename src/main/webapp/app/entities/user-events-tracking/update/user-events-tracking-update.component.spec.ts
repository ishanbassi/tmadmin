import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IUserProfile } from 'app/entities/user-profile/user-profile.model';
import { UserProfileService } from 'app/entities/user-profile/service/user-profile.service';
import { UserEventsTrackingService } from '../service/user-events-tracking.service';
import { IUserEventsTracking } from '../user-events-tracking.model';
import { UserEventsTrackingFormService } from './user-events-tracking-form.service';

import { UserEventsTrackingUpdateComponent } from './user-events-tracking-update.component';

describe('UserEventsTracking Management Update Component', () => {
  let comp: UserEventsTrackingUpdateComponent;
  let fixture: ComponentFixture<UserEventsTrackingUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let userEventsTrackingFormService: UserEventsTrackingFormService;
  let userEventsTrackingService: UserEventsTrackingService;
  let userProfileService: UserProfileService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [UserEventsTrackingUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(UserEventsTrackingUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UserEventsTrackingUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    userEventsTrackingFormService = TestBed.inject(UserEventsTrackingFormService);
    userEventsTrackingService = TestBed.inject(UserEventsTrackingService);
    userProfileService = TestBed.inject(UserProfileService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call UserProfile query and add missing value', () => {
      const userEventsTracking: IUserEventsTracking = { id: 17786 };
      const userProfile: IUserProfile = { id: 22058 };
      userEventsTracking.userProfile = userProfile;

      const userProfileCollection: IUserProfile[] = [{ id: 22058 }];
      jest.spyOn(userProfileService, 'query').mockReturnValue(of(new HttpResponse({ body: userProfileCollection })));
      const additionalUserProfiles = [userProfile];
      const expectedCollection: IUserProfile[] = [...additionalUserProfiles, ...userProfileCollection];
      jest.spyOn(userProfileService, 'addUserProfileToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userEventsTracking });
      comp.ngOnInit();

      expect(userProfileService.query).toHaveBeenCalled();
      expect(userProfileService.addUserProfileToCollectionIfMissing).toHaveBeenCalledWith(
        userProfileCollection,
        ...additionalUserProfiles.map(expect.objectContaining),
      );
      expect(comp.userProfilesSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const userEventsTracking: IUserEventsTracking = { id: 17786 };
      const userProfile: IUserProfile = { id: 22058 };
      userEventsTracking.userProfile = userProfile;

      activatedRoute.data = of({ userEventsTracking });
      comp.ngOnInit();

      expect(comp.userProfilesSharedCollection).toContainEqual(userProfile);
      expect(comp.userEventsTracking).toEqual(userEventsTracking);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserEventsTracking>>();
      const userEventsTracking = { id: 1044 };
      jest.spyOn(userEventsTrackingFormService, 'getUserEventsTracking').mockReturnValue(userEventsTracking);
      jest.spyOn(userEventsTrackingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userEventsTracking });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userEventsTracking }));
      saveSubject.complete();

      // THEN
      expect(userEventsTrackingFormService.getUserEventsTracking).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(userEventsTrackingService.update).toHaveBeenCalledWith(expect.objectContaining(userEventsTracking));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserEventsTracking>>();
      const userEventsTracking = { id: 1044 };
      jest.spyOn(userEventsTrackingFormService, 'getUserEventsTracking').mockReturnValue({ id: null });
      jest.spyOn(userEventsTrackingService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userEventsTracking: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userEventsTracking }));
      saveSubject.complete();

      // THEN
      expect(userEventsTrackingFormService.getUserEventsTracking).toHaveBeenCalled();
      expect(userEventsTrackingService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserEventsTracking>>();
      const userEventsTracking = { id: 1044 };
      jest.spyOn(userEventsTrackingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userEventsTracking });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(userEventsTrackingService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUserProfile', () => {
      it('should forward to userProfileService', () => {
        const entity = { id: 22058 };
        const entity2 = { id: 9009 };
        jest.spyOn(userProfileService, 'compareUserProfile');
        comp.compareUserProfile(entity, entity2);
        expect(userProfileService.compareUserProfile).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
