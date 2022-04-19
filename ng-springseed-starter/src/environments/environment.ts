// This file can be replaced during build by using the `fileReplacements` array.
// `ng build` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

import { KeycloakService } from "keycloak-angular";

export const environment = {
  production: false,
  useHash: true,
  api: {
    prefixUrl: '/api'
  },
  // keycloak 初始化方法
  initializeKeycloak: function(keycloak: KeycloakService) {
    return () =>
    keycloak.init({
      config: {
        url: 'http://localhost:9000/auth',
        realm: 'springseeds',
        clientId: 'ng-springseed-starter'
      },
      initOptions: {
        // onLoad: 'check-sso',
        silentCheckSsoRedirectUri:
          window.location.origin + '/assets/silent-check-sso.html'
      }
    });    
  }
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/plugins/zone-error';  // Included with Angular CLI.
