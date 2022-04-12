import { NgModule, Type } from '@angular/core';
import { SharedModule } from '@shared';

import { KeycloakRoutingModule } from './keycloak-routing.module';
import { KeycloakLoginComponent } from './login.component';
import { KeycloakUserInfoComponent } from './user-info.component';

const COMPONENTS: Array<Type<void>> = [KeycloakUserInfoComponent, KeycloakLoginComponent];

@NgModule({
  imports: [SharedModule, KeycloakRoutingModule],
  declarations: [...COMPONENTS]
})
export class KeyclaokModule {}
