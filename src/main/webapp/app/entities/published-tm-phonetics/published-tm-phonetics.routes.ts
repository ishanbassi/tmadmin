import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { PublishedTmPhoneticsComponent } from './list/published-tm-phonetics.component';
import { PublishedTmPhoneticsDetailComponent } from './detail/published-tm-phonetics-detail.component';
import { PublishedTmPhoneticsUpdateComponent } from './update/published-tm-phonetics-update.component';
import PublishedTmPhoneticsResolve from './route/published-tm-phonetics-routing-resolve.service';

const publishedTmPhoneticsRoute: Routes = [
  {
    path: '',
    component: PublishedTmPhoneticsComponent,
    data: {
      defaultSort: `id,${  ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PublishedTmPhoneticsDetailComponent,
    resolve: {
      publishedTmPhonetics: PublishedTmPhoneticsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PublishedTmPhoneticsUpdateComponent,
    resolve: {
      publishedTmPhonetics: PublishedTmPhoneticsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PublishedTmPhoneticsUpdateComponent,
    resolve: {
      publishedTmPhonetics: PublishedTmPhoneticsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default publishedTmPhoneticsRoute;
