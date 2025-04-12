import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import TrademarkResolve from './route/trademark-routing-resolve.service';

const trademarkRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/trademark.component').then(m => m.TrademarkComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/trademark-detail.component').then(m => m.TrademarkDetailComponent),
    resolve: {
      trademark: TrademarkResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/trademark-update.component').then(m => m.TrademarkUpdateComponent),
    resolve: {
      trademark: TrademarkResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/trademark-update.component').then(m => m.TrademarkUpdateComponent),
    resolve: {
      trademark: TrademarkResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default trademarkRoute;
