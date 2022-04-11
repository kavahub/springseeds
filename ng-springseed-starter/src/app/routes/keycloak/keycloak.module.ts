import { NgModule, Type } from '@angular/core';
import { SharedModule } from '@shared';

import { KeycloakRoutingModule } from './keycloak-routing.module';
import { KeycloakLoginComponent } from './login.component';

const COMPONENTS: Array<Type<void>> = [KeycloakLoginComponent];

@NgModule({
  imports: [SharedModule, KeycloakRoutingModule],
  declarations: [...COMPONENTS]
})
export class KeyclaokModule {}
