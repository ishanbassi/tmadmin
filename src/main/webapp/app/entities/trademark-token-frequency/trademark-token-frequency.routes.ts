import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import TrademarkTokenFrequencyResolve from './route/trademark-token-frequency-routing-resolve.service';

const trademarkTokenFrequencyRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/trademark-token-frequency.component').then(m => m.TrademarkTokenFrequencyComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/trademark-token-frequency-detail.component').then(m => m.TrademarkTokenFrequencyDetailComponent),
    resolve: {
      trademarkTokenFrequency: TrademarkTokenFrequencyResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/trademark-token-frequency-update.component').then(m => m.TrademarkTokenFrequencyUpdateComponent),
    resolve: {
      trademarkTokenFrequency: TrademarkTokenFrequencyResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/trademark-token-frequency-update.component').then(m => m.TrademarkTokenFrequencyUpdateComponent),
    resolve: {
      trademarkTokenFrequency: TrademarkTokenFrequencyResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default trademarkTokenFrequencyRoute;
