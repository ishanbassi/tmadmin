import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import PublishedTmPhoneticsResolve from './route/published-tm-phonetics-routing-resolve.service';

const publishedTmPhoneticsRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/published-tm-phonetics.component').then(m => m.PublishedTmPhoneticsComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/published-tm-phonetics-detail.component').then(m => m.PublishedTmPhoneticsDetailComponent),
    resolve: {
      publishedTmPhonetics: PublishedTmPhoneticsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/published-tm-phonetics-update.component').then(m => m.PublishedTmPhoneticsUpdateComponent),
    resolve: {
      publishedTmPhonetics: PublishedTmPhoneticsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/published-tm-phonetics-update.component').then(m => m.PublishedTmPhoneticsUpdateComponent),
    resolve: {
      publishedTmPhonetics: PublishedTmPhoneticsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default publishedTmPhoneticsRoute;
