import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import TrademarkTokenResolve from './route/trademark-token-routing-resolve.service';

const trademarkTokenRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/trademark-token.component').then(m => m.TrademarkTokenComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/trademark-token-detail.component').then(m => m.TrademarkTokenDetailComponent),
    resolve: {
      trademarkToken: TrademarkTokenResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/trademark-token-update.component').then(m => m.TrademarkTokenUpdateComponent),
    resolve: {
      trademarkToken: TrademarkTokenResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/trademark-token-update.component').then(m => m.TrademarkTokenUpdateComponent),
    resolve: {
      trademarkToken: TrademarkTokenResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default trademarkTokenRoute;
