import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import PhoneticsResolve from './route/phonetics-routing-resolve.service';

const phoneticsRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/phonetics.component').then(m => m.PhoneticsComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/phonetics-detail.component').then(m => m.PhoneticsDetailComponent),
    resolve: {
      phonetics: PhoneticsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/phonetics-update.component').then(m => m.PhoneticsUpdateComponent),
    resolve: {
      phonetics: PhoneticsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/phonetics-update.component').then(m => m.PhoneticsUpdateComponent),
    resolve: {
      phonetics: PhoneticsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default phoneticsRoute;
