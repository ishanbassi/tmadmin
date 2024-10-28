import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { PublishedTmComponent } from './list/published-tm.component';
import { PublishedTmDetailComponent } from './detail/published-tm-detail.component';
import { PublishedTmUpdateComponent } from './update/published-tm-update.component';
import PublishedTmResolve from './route/published-tm-routing-resolve.service';

const publishedTmRoute: Routes = [
  {
    path: '',
    component: PublishedTmComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PublishedTmDetailComponent,
    resolve: {
      publishedTm: PublishedTmResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PublishedTmUpdateComponent,
    resolve: {
      publishedTm: PublishedTmResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PublishedTmUpdateComponent,
    resolve: {
      publishedTm: PublishedTmResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default publishedTmRoute;
