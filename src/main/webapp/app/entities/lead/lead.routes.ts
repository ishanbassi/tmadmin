import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { LeadComponent } from './list/lead.component';
import { LeadDetailComponent } from './detail/lead-detail.component';
import { LeadUpdateComponent } from './update/lead-update.component';
import LeadResolve from './route/lead-routing-resolve.service';

const leadRoute: Routes = [
  {
    path: '',
    component: LeadComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LeadDetailComponent,
    resolve: {
      lead: LeadResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LeadUpdateComponent,
    resolve: {
      lead: LeadResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LeadUpdateComponent,
    resolve: {
      lead: LeadResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default leadRoute;
