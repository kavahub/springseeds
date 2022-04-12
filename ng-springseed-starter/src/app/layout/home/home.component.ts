import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { KeycloakService } from 'keycloak-angular';
import { KeycloakProfile } from 'keycloak-js';

@Component({
  selector: 'layout-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.less']
})
export class LayoutHomeComponent {
  userName = "未登录";
  isLoggedIn = false;
  constructor(private readonly keycloak: KeycloakService, private router: Router) { }

  async ngOnInit() {
    this.isLoggedIn = await this.keycloak.isLoggedIn();
    if (this.isLoggedIn) {
      const userProfile = await this.keycloak.loadUserProfile();
      this.userName = `${userProfile?.firstName}${userProfile?.lastName}(${userProfile?.username})`
    }
  }

  home(): void {
    setTimeout(() => this.router.navigateByUrl("/"));
  }
}