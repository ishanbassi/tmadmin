import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import TmAgentResolve from './route/tm-agent-routing-resolve.service';

const tmAgentRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/tm-agent.component').then(m => m.TmAgentComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/tm-agent-detail.component').then(m => m.TmAgentDetailComponent),
    resolve: {
      tmAgent: TmAgentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/tm-agent-update.component').then(m => m.TmAgentUpdateComponent),
    resolve: {
      tmAgent: TmAgentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/tm-agent-update.component').then(m => m.TmAgentUpdateComponent),
    resolve: {
      tmAgent: TmAgentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default tmAgentRoute;
