import { KeycloakService } from "keycloak-angular";

export const environment = {
  production: true,
  useHash: true,
  api: {
    prefixUrl: '/api'
  },
  // keycloak 初始化方法
  initializeKeycloak: function(keycloak: KeycloakService) {
    return () =>
    keycloak.init({
      config: {
        url: 'http://auth-server:9000/auth',
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
