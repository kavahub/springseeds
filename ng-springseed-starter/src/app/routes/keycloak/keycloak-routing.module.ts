import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LayoutHomeComponent } from '@layout';

import { KeycloakLoginComponent } from './login.component';
import { KeycloakUserInfoComponent } from './user-info.component';

const routes: Routes = [
  {
    path: '',
    component: LayoutHomeComponent,
    children: [
      { path: '', redirectTo: 'login', pathMatch: 'full' },
      { path: 'login', component: KeycloakLoginComponent },
      { path: 'user-info', component: KeycloakUserInfoComponent }
      
    ]
  },
];


@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class KeycloakRoutingModule {}
