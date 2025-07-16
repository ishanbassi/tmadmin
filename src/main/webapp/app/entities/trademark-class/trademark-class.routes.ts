import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import TrademarkClassResolve from './route/trademark-class-routing-resolve.service';

const trademarkClassRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/trademark-class.component').then(m => m.TrademarkClassComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/trademark-class-detail.component').then(m => m.TrademarkClassDetailComponent),
    resolve: {
      trademarkClass: TrademarkClassResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/trademark-class-update.component').then(m => m.TrademarkClassUpdateComponent),
    resolve: {
      trademarkClass: TrademarkClassResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/trademark-class-update.component').then(m => m.TrademarkClassUpdateComponent),
    resolve: {
      trademarkClass: TrademarkClassResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default trademarkClassRoute;
