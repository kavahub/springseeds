import { Component } from "@angular/core";
import { Router } from "@angular/router";

@Component({
    selector: 'app-home',
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.less']
})
export class HomeComponent {
    keycloakList = [
        { title: 'Keycloak登录与登出', router: '/keycloak/login' },
        { title: 'Keycloak登录用户信息', router: '/keycloak/user-info' },
    ];

    constructor(private router: Router) { }

    async ngOnInit() {
    }

    goto(url: string): void {
        setTimeout(() => this.router.navigateByUrl(url));
    }
}