/* eslint-disable import/order */
import { CommonModule } from '@angular/common';
import { NgModule, Type } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { SharedModule } from '@shared';

import { LayoutBlankComponent, LayoutHomeComponent } from './';

const COMPONENTS: Array<Type<void>>  = [LayoutBlankComponent, LayoutHomeComponent];

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    SharedModule
  ],
  declarations: [...COMPONENTS],
  exports: [...COMPONENTS],
})
export class LayoutModule { }
