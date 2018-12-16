import { Component, OnInit } from '@angular/core';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  constructor(private authService: AuthService) { }

  ngOnInit() {
  }

  login(event) {
    const username = event.target.querySelector('#username').value;
    const password = event.target.querySelector('#password').value;
    this.authService.confirmAdmin(username, password);
  }
}
