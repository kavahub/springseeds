import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { KeycloakLoginComponent } from './login.component';

const routes: Routes = [
  { path: 'login', component: KeycloakLoginComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class KeycloakRoutingModule {}
