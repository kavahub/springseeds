import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { environment } from '@env/environment';

import { LayoutBlankComponent } from '@layout';

import { HomeComponent } from './home.component';

const routes: Routes = [
  {
    path: '',
    component: LayoutBlankComponent,
    children: [
      { path: '', redirectTo: 'home', pathMatch: 'full' },
      { path: 'home', component: HomeComponent, data: { title: '主页'} },

      { path: 'keycloak', loadChildren: () => import('./keycloak/keycloak.module').then(m => m.KeyclaokModule) },
    ]
  },
];

@NgModule({
  imports: [
    RouterModule.forRoot(
      routes, {
        useHash: environment.useHash,
        // NOTICE: If you use `reuse-tab` component and turn on keepingScroll you can set to `disabled`
        // Pls refer to https://ng-alain.com/components/reuse-tab
        scrollPositionRestoration: 'top',
      }
    )],
  exports: [RouterModule],
})
export class RouteRoutingModule { }
