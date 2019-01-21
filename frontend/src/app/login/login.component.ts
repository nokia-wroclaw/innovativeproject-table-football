import {Component, OnInit} from '@angular/core';
import {AuthService} from '../services/auth.service';
import {AlertService} from '../services/alert.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
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

  dismissAlert() {
    this.alertService.clearAlert();
  }
}
