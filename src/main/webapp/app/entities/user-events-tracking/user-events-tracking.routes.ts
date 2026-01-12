import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import UserEventsTrackingResolve from './route/user-events-tracking-routing-resolve.service';

const userEventsTrackingRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/user-events-tracking.component').then(m => m.UserEventsTrackingComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/user-events-tracking-detail.component').then(m => m.UserEventsTrackingDetailComponent),
    resolve: {
      userEventsTracking: UserEventsTrackingResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/user-events-tracking-update.component').then(m => m.UserEventsTrackingUpdateComponent),
    resolve: {
      userEventsTracking: UserEventsTrackingResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/user-events-tracking-update.component').then(m => m.UserEventsTrackingUpdateComponent),
    resolve: {
      userEventsTracking: UserEventsTrackingResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default userEventsTrackingRoute;
