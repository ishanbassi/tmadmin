import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import DocumentsResolve from './route/documents-routing-resolve.service';

const documentsRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/documents.component').then(m => m.DocumentsComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/documents-detail.component').then(m => m.DocumentsDetailComponent),
    resolve: {
      documents: DocumentsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/documents-update.component').then(m => m.DocumentsUpdateComponent),
    resolve: {
      documents: DocumentsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/documents-update.component').then(m => m.DocumentsUpdateComponent),
    resolve: {
      documents: DocumentsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default documentsRoute;
