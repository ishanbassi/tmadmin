import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import TokenPhoneticResolve from './route/token-phonetic-routing-resolve.service';

const tokenPhoneticRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/token-phonetic.component').then(m => m.TokenPhoneticComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/token-phonetic-detail.component').then(m => m.TokenPhoneticDetailComponent),
    resolve: {
      tokenPhonetic: TokenPhoneticResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/token-phonetic-update.component').then(m => m.TokenPhoneticUpdateComponent),
    resolve: {
      tokenPhonetic: TokenPhoneticResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/token-phonetic-update.component').then(m => m.TokenPhoneticUpdateComponent),
    resolve: {
      tokenPhonetic: TokenPhoneticResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default tokenPhoneticRoute;
