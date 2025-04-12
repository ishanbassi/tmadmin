import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { TrademarkComponent } from './list/trademark.component';
import { TrademarkDetailComponent } from './detail/trademark-detail.component';
import { TrademarkUpdateComponent } from './update/trademark-update.component';
import TrademarkResolve from './route/trademark-routing-resolve.service';

const trademarkRoute: Routes = [
  {
    path: '',
    component: TrademarkComponent,
    data: {
      defaultSort: `id,${  ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TrademarkDetailComponent,
    resolve: {
      trademark: TrademarkResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TrademarkUpdateComponent,
    resolve: {
      trademark: TrademarkResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TrademarkUpdateComponent,
    resolve: {
      trademark: TrademarkResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default trademarkRoute;
