import { Component } from "@angular/core";
import { KeycloakService } from "keycloak-angular";
import { KeycloakProfile } from "keycloak-js";

@Component({
    selector: 'app-keycloak-user-info',
    templateUrl: './user-info.component.html',
    styleUrls: ['./user-info.component.less']
})
export class KeycloakUserInfoComponent {
    isLoggedIn = false;
    userProfile: KeycloakProfile | null = null;

    constructor(private readonly keycloak: KeycloakService) { }

    async ngOnInit() {
        this.isLoggedIn = await this.keycloak.isLoggedIn();

        if (this.isLoggedIn) {
            this.userProfile = await this.keycloak.loadUserProfile();
        }
    }
}