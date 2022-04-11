import { Component } from "@angular/core";
import { KeycloakService } from "keycloak-angular";
import { KeycloakProfile } from "keycloak-js";

@Component({
    selector: 'app-keycloak-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.less']
})
export class KeycloakLoginComponent {
    public isLoggedIn = false;
    public userProfile: KeycloakProfile | null = null;

    constructor(private readonly keycloak: KeycloakService) { }

    public async ngOnInit() {
        this.isLoggedIn = await this.keycloak.isLoggedIn();

        if (this.isLoggedIn) {
            this.userProfile = await this.keycloak.loadUserProfile();
        }
    }

    public login() {
        this.keycloak.login();
    }

    public logout() {
        this.keycloak.logout();
    }
}