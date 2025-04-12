import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IUserProfile } from 'app/entities/user-profile/user-profile.model';
import { UserProfileService } from 'app/entities/user-profile/service/user-profile.service';
import { TrademarkService } from '../service/trademark.service';
import { ITrademark } from '../trademark.model';
import { TrademarkFormService } from './trademark-form.service';

import { TrademarkUpdateComponent } from './trademark-update.component';

describe('Trademark Management Update Component', () => {
  let comp: TrademarkUpdateComponent;
  let fixture: ComponentFixture<TrademarkUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let trademarkFormService: TrademarkFormService;
  let trademarkService: TrademarkService;
  let userProfileService: UserProfileService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TrademarkUpdateComponent],
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
      .overrideTemplate(TrademarkUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TrademarkUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    trademarkFormService = TestBed.inject(TrademarkFormService);
    trademarkService = TestBed.inject(TrademarkService);
    userProfileService = TestBed.inject(UserProfileService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call UserProfile query and add missing value', () => {
      const trademark: ITrademark = { id: 3769 };
      const userProfile: IUserProfile = { id: 22058 };
      trademark.userProfile = userProfile;

      const userProfileCollection: IUserProfile[] = [{ id: 22058 }];
      jest.spyOn(userProfileService, 'query').mockReturnValue(of(new HttpResponse({ body: userProfileCollection })));
      const additionalUserProfiles = [userProfile];
      const expectedCollection: IUserProfile[] = [...additionalUserProfiles, ...userProfileCollection];
      jest.spyOn(userProfileService, 'addUserProfileToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ trademark });
      comp.ngOnInit();

      expect(userProfileService.query).toHaveBeenCalled();
      expect(userProfileService.addUserProfileToCollectionIfMissing).toHaveBeenCalledWith(
        userProfileCollection,
        ...additionalUserProfiles.map(expect.objectContaining),
      );
      expect(comp.userProfilesSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const trademark: ITrademark = { id: 3769 };
      const userProfile: IUserProfile = { id: 22058 };
      trademark.userProfile = userProfile;

      activatedRoute.data = of({ trademark });
      comp.ngOnInit();

      expect(comp.userProfilesSharedCollection).toContainEqual(userProfile);
      expect(comp.trademark).toEqual(trademark);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITrademark>>();
      const trademark = { id: 4352 };
      jest.spyOn(trademarkFormService, 'getTrademark').mockReturnValue(trademark);
      jest.spyOn(trademarkService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trademark });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: trademark }));
      saveSubject.complete();

      // THEN
      expect(trademarkFormService.getTrademark).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(trademarkService.update).toHaveBeenCalledWith(expect.objectContaining(trademark));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITrademark>>();
      const trademark = { id: 4352 };
      jest.spyOn(trademarkFormService, 'getTrademark').mockReturnValue({ id: null });
      jest.spyOn(trademarkService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trademark: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: trademark }));
      saveSubject.complete();

      // THEN
      expect(trademarkFormService.getTrademark).toHaveBeenCalled();
      expect(trademarkService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITrademark>>();
      const trademark = { id: 4352 };
      jest.spyOn(trademarkService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trademark });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(trademarkService.update).toHaveBeenCalled();
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
