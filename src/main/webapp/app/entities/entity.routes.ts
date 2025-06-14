import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'Authorities' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'trademark',
    data: { pageTitle: 'Trademarks' },
    loadChildren: () => import('./trademark/trademark.routes'),
  },
  {
    path: 'phonetics',
    data: { pageTitle: 'Phonetics' },
    loadChildren: () => import('./phonetics/phonetics.routes'),
  },
  {
    path: 'published-tm-phonetics',
    data: { pageTitle: 'PublishedTmPhonetics' },
    loadChildren: () => import('./published-tm-phonetics/published-tm-phonetics.routes'),
  },
  {
    path: 'published-tm',
    data: { pageTitle: 'PublishedTms' },
    loadChildren: () => import('./published-tm/published-tm.routes'),
  },
  {
    path: 'tm-agent',
    data: { pageTitle: 'TmAgents' },
    loadChildren: () => import('./tm-agent/tm-agent.routes'),
  },
  {
    path: 'user-profile',
    data: { pageTitle: 'UserProfiles' },
    loadChildren: () => import('./user-profile/user-profile.routes'),
  },
  {
    path: 'lead',
    data: { pageTitle: 'Leads' },
    loadChildren: () => import('./lead/lead.routes'),
  },
  {
    path: 'employee',
    data: { pageTitle: 'Employees' },
    loadChildren: () => import('./employee/employee.routes'),
  },
  {
    path: 'company',
    data: { pageTitle: 'Companies' },
    loadChildren: () => import('./company/company.routes'),
  },
  {
    path: 'documents',
    data: { pageTitle: 'Documents' },
    loadChildren: () => import('./documents/documents.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
