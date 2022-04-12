import { APP_INITIALIZER, LOCALE_ID, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { NzMessageModule } from 'ng-zorro-antd/message';
import { NzNotificationModule } from 'ng-zorro-antd/notification';
import { KeycloakService, KeycloakAngularModule } from 'keycloak-angular';

import { environment } from '@env/environment';
import { CoreModule } from './core/core.module';
import { GlobalConfigModule } from './global-config.module';
import { LayoutModule } from './layout/layout.module';
import { RoutesModule } from './routes/routes.module';
import { SharedModule } from './shared/shared.module';

// 语言
import { default as ngLang } from '@angular/common/locales/zh';
import { zhCN as dateLang } from 'date-fns/locale';
import { NZ_DATE_LOCALE, NZ_I18N, zh_CN as zorroLang } from 'ng-zorro-antd/i18n';
const LANG = {
  abbr: 'zh',
  ng: ngLang,
  zorro: zorroLang,
  date: dateLang
};
import { registerLocaleData } from '@angular/common';
registerLocaleData(LANG.ng, LANG.abbr);
const LANG_PROVIDES = [
  { provide: LOCALE_ID, useValue: LANG.abbr },
  { provide: NZ_I18N, useValue: LANG.zorro },
  { provide: NZ_DATE_LOCALE, useValue: LANG.date },
];

// 应用初始化
const APPINIT_PROVIDES = [
  {
    provide: APP_INITIALIZER,
    useFactory: environment.initializeKeycloak,
    multi: true,
    deps: [KeycloakService]
  }
];

// http 中断器
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { DefaultInterceptor } from '@core';
const INTERCEPTOR_PROVIDES = [
  { provide: HTTP_INTERCEPTORS, useClass: DefaultInterceptor, multi: true}
];

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    BrowserAnimationsModule,
    KeycloakAngularModule,
    GlobalConfigModule.forRoot(),
    CoreModule,
    SharedModule,
    LayoutModule,
    RoutesModule,
    NzMessageModule,
    NzNotificationModule,
  ],
  providers: [
    ...LANG_PROVIDES,
    ...INTERCEPTOR_PROVIDES,
    ...APPINIT_PROVIDES
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
