import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import PublishedTmResolve from './route/published-tm-routing-resolve.service';

const publishedTmRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/published-tm.component').then(m => m.PublishedTmComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/published-tm-detail.component').then(m => m.PublishedTmDetailComponent),
    resolve: {
      publishedTm: PublishedTmResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/published-tm-update.component').then(m => m.PublishedTmUpdateComponent),
    resolve: {
      publishedTm: PublishedTmResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/published-tm-update.component').then(m => m.PublishedTmUpdateComponent),
    resolve: {
      publishedTm: PublishedTmResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default publishedTmRoute;
