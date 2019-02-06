import {Injectable} from '@angular/core';
import {HttpHeaders, HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import {AlertService} from './alert.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private authorized = false;
  private basicHash: string;

  constructor(private http: HttpClient, private router: Router, private alertService: AlertService) {
  }

  confirmAdmin(username: string, password: string) {
    let headers = new HttpHeaders();
    this.basicHash = 'Basic ' + btoa(username + ':' + password);
    headers = headers.append('Authorization', this.basicHash);

    return this.http.get('https://localhost:8443/admin/', {headers: headers, responseType: 'text'})
      .subscribe(response => {
          if (response === 'Admin confirmed') {
            this.authorized = true;
            this.router.navigateByUrl('/admin');
          } else {
            // window.alert('Invalid credentials!');
            console.error('Login error');
            this.alertService.publishAlert('error', response.toString());
          }
        },
        (e) => {
          console.error('Login error', e.error);
          this.alertService.publishAlert('error', JSON.parse(e.error).errorMessage);
        });
  }

  isAdminConfirmed() {
    return this.authorized;
  }

  getBasicHash(): string {
    return this.basicHash;
  }
}
