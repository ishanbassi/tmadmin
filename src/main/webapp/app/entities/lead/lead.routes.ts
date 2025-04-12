import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import LeadResolve from './route/lead-routing-resolve.service';

const leadRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/lead.component').then(m => m.LeadComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/lead-detail.component').then(m => m.LeadDetailComponent),
    resolve: {
      lead: LeadResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/lead-update.component').then(m => m.LeadUpdateComponent),
    resolve: {
      lead: LeadResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/lead-update.component').then(m => m.LeadUpdateComponent),
    resolve: {
      lead: LeadResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default leadRoute;
