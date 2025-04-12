import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { TmAgentComponent } from './list/tm-agent.component';
import { TmAgentDetailComponent } from './detail/tm-agent-detail.component';
import { TmAgentUpdateComponent } from './update/tm-agent-update.component';
import TmAgentResolve from './route/tm-agent-routing-resolve.service';

const tmAgentRoute: Routes = [
  {
    path: '',
    component: TmAgentComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TmAgentDetailComponent,
    resolve: {
      tmAgent: TmAgentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TmAgentUpdateComponent,
    resolve: {
      tmAgent: TmAgentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TmAgentUpdateComponent,
    resolve: {
      tmAgent: TmAgentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default tmAgentRoute;
