import { Component, OnInit } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { AlertService } from '../services/alert.service';
import {
  trigger,
  state,
  style,
  animate,
  transition,
  // ...
} from '@angular/animations';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  animations: [
    trigger('alertTrigger', [
      transition(':enter', [
        style({
          transform: 'translateY(-100%)'
        }),
        animate('150ms ease-out', style({
          transform: 'translateY(0%)'
        }))
      ]),
      transition(':leave', [
        animate('150ms ease-in', style({
          transform: 'translateY(-100%)'
        }))
      ])
    ])
  ]
})
export class LoginComponent implements OnInit {

  constructor(private authService: AuthService, public alertService: AlertService) {
  }

  ngOnInit() {
  }

  login(event) {
    const username = event.target.querySelector('#username').value;
    const password = event.target.querySelector('#password').value;
    this.authService.confirmAdmin(username, password);
  }
}
