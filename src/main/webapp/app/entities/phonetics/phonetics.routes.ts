import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { PhoneticsComponent } from './list/phonetics.component';
import { PhoneticsDetailComponent } from './detail/phonetics-detail.component';
import { PhoneticsUpdateComponent } from './update/phonetics-update.component';
import PhoneticsResolve from './route/phonetics-routing-resolve.service';

const phoneticsRoute: Routes = [
  {
    path: '',
    component: PhoneticsComponent,
    data: {
      defaultSort: `id,${  ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PhoneticsDetailComponent,
    resolve: {
      phonetics: PhoneticsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PhoneticsUpdateComponent,
    resolve: {
      phonetics: PhoneticsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PhoneticsUpdateComponent,
    resolve: {
      phonetics: PhoneticsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default phoneticsRoute;
