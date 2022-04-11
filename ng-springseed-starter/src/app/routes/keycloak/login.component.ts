import { Component } from "@angular/core";
import { KeycloakService } from "keycloak-angular";
import { KeycloakProfile } from "keycloak-js";

@Component({
    selector: 'app-keycloak-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.less']
})
export class KeycloakLoginComponent {
    isLoggedIn = false;
    userProfile: KeycloakProfile | null = null;

    constructor(private readonly keycloak: KeycloakService) { }

    async ngOnInit() {
        this.isLoggedIn = await this.keycloak.isLoggedIn();

        if (this.isLoggedIn) {
            this.userProfile = await this.keycloak.loadUserProfile();
        }
    }

    login(): void{
        this.keycloak.login();
    }

    logout(): void{
        this.keycloak.logout();
    }
}