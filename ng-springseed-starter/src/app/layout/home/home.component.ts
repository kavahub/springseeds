import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'layout-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.less']
})
export class LayoutHomeComponent {
    constructor(private router: Router) { }

    home(): void {
        this.router.navigateByUrl("/");
    }
}