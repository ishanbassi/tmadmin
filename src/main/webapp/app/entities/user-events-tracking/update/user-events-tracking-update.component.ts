import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUserProfile } from 'app/entities/user-profile/user-profile.model';
import { UserProfileService } from 'app/entities/user-profile/service/user-profile.service';
import { IUserEventsTracking } from '../user-events-tracking.model';
import { UserEventsTrackingService } from '../service/user-events-tracking.service';
import { UserEventsTrackingFormGroup, UserEventsTrackingFormService } from './user-events-tracking-form.service';

@Component({
  selector: 'jhi-user-events-tracking-update',
  templateUrl: './user-events-tracking-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class UserEventsTrackingUpdateComponent implements OnInit {
  isSaving = false;
  userEventsTracking: IUserEventsTracking | null = null;

  userProfilesSharedCollection: IUserProfile[] = [];

  protected userEventsTrackingService = inject(UserEventsTrackingService);
  protected userEventsTrackingFormService = inject(UserEventsTrackingFormService);
  protected userProfileService = inject(UserProfileService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: UserEventsTrackingFormGroup = this.userEventsTrackingFormService.createUserEventsTrackingFormGroup();

  compareUserProfile = (o1: IUserProfile | null, o2: IUserProfile | null): boolean => this.userProfileService.compareUserProfile(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userEventsTracking }) => {
      this.userEventsTracking = userEventsTracking;
      if (userEventsTracking) {
        this.updateForm(userEventsTracking);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const userEventsTracking = this.userEventsTrackingFormService.getUserEventsTracking(this.editForm);
    if (userEventsTracking.id !== null) {
      this.subscribeToSaveResponse(this.userEventsTrackingService.update(userEventsTracking));
    } else {
      this.subscribeToSaveResponse(this.userEventsTrackingService.create(userEventsTracking));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserEventsTracking>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(userEventsTracking: IUserEventsTracking): void {
    this.userEventsTracking = userEventsTracking;
    this.userEventsTrackingFormService.resetForm(this.editForm, userEventsTracking);

    this.userProfilesSharedCollection = this.userProfileService.addUserProfileToCollectionIfMissing<IUserProfile>(
      this.userProfilesSharedCollection,
      userEventsTracking.userProfile,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userProfileService
      .query()
      .pipe(map((res: HttpResponse<IUserProfile[]>) => res.body ?? []))
      .pipe(
        map((userProfiles: IUserProfile[]) =>
          this.userProfileService.addUserProfileToCollectionIfMissing<IUserProfile>(userProfiles, this.userEventsTracking?.userProfile),
        ),
      )
      .subscribe((userProfiles: IUserProfile[]) => (this.userProfilesSharedCollection = userProfiles));
  }
}
