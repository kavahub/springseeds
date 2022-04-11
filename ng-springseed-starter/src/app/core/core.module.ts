import { NgModule, Optional, SkipSelf, Type } from '@angular/core';
import { throwIfAlreadyLoaded } from './module-import-guard';


const SERVICES: Array<Type<void>> = [

]

@NgModule({
  providers: [...SERVICES]
})
export class CoreModule {
  constructor( @Optional() @SkipSelf() parentModule: CoreModule) {
    throwIfAlreadyLoaded(parentModule, 'CoreModule');
  }
}