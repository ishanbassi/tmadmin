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
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
