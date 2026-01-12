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
  {
    path: 'trademark-class',
    data: { pageTitle: 'TrademarkClasses' },
    loadChildren: () => import('./trademark-class/trademark-class.routes'),
  },
  {
    path: 'payment',
    data: { pageTitle: 'Payments' },
    loadChildren: () => import('./payment/payment.routes'),
  },
  {
    path: 'trademark-plan',
    data: { pageTitle: 'TrademarkPlans' },
    loadChildren: () => import('./trademark-plan/trademark-plan.routes'),
  },
  {
    path: 'trademark-token',
    data: { pageTitle: 'TrademarkTokens' },
    loadChildren: () => import('./trademark-token/trademark-token.routes'),
  },
  {
    path: 'token-phonetic',
    data: { pageTitle: 'TokenPhonetics' },
    loadChildren: () => import('./token-phonetic/token-phonetic.routes'),
  },
  {
    path: 'trademark-token-frequency',
    data: { pageTitle: 'TrademarkTokenFrequencies' },
    loadChildren: () => import('./trademark-token-frequency/trademark-token-frequency.routes'),
  },
  {
    path: 'user-events-tracking',
    data: { pageTitle: 'UserEventsTrackings' },
    loadChildren: () => import('./user-events-tracking/user-events-tracking.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
