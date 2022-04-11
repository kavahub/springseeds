import { NgModule, Type } from '@angular/core';
import { SharedModule } from '@shared';
import { RouteRoutingModule } from './routes-routing.module';

import { HomeComponent } from './home.component';

const COMPONENTS: Array<Type<void>> = [
  HomeComponent
];

@NgModule({
  imports: [SharedModule, RouteRoutingModule],
  declarations: COMPONENTS,
})
export class RoutesModule {}
