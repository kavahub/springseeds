import { Component } from "@angular/core";
import { KeycloakService } from "keycloak-angular";

@Component({
    selector: 'app-keycloak-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.less']
})
export class KeycloakLoginComponent {
    isLoggedIn = false;

    constructor(private readonly keycloak: KeycloakService) { }

    async ngOnInit() {
        this.isLoggedIn = await this.keycloak.isLoggedIn();
    }

    login(): void {
        this.keycloak.login();
      }
    
      logout(): void {
        this.keycloak.logout();
      }
}