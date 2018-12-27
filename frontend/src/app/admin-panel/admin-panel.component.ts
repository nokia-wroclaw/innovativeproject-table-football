import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import {
  trigger,
  style,
  animate,
  transition
} from '@angular/animations';

@Component({
  selector: 'app-admin-panel',
  templateUrl: './admin-panel.component.html',
  styleUrls: ['./admin-panel.component.css'],
  encapsulation: ViewEncapsulation.None,
  animations: [
    trigger('sensorInfoAnimation', [
      transition(':enter', [
        style({
          transform: 'translateX(100%)'
        }),
        animate('500ms ease-out', style({
          transform: 'translateX(0%)'
        }))
      ]),
      transition(':leave', [
        animate('500ms ease-out', style({
          transform: 'translateX(100%)'
        }))
      ])
    ]),
    trigger('calibrationAnimation', [
      transition(':enter', [
        style({
          transform: 'translateX(-100%)'
        }),
        animate('500ms ease-out', style({
          transform: 'translateX(0%)'
        }))
      ]),
      transition(':leave', [
        animate('150ms ease-out', style({
          transform: 'translateX(-10%)'
        }))
      ])
    ])
  ]
})
export class AdminPanelComponent implements OnInit {
  activeTab: string;

  constructor(private authService: AuthService, private router: Router) {
    // if (!this.authService.isAdminConfirmed()) {
    //   this.router.navigateByUrl('/login');
    // }
    this.activeTab = 'sensorInfo';
  }

  ngOnInit() {
  }

  setActiveTab(tab: string) {
    this.activeTab = tab;
  }
}
