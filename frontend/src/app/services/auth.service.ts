import {Injectable} from '@angular/core';
import {HttpHeaders, HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private authorized = false;
  private basicHash: string;

  constructor(private http: HttpClient, private router: Router) {
  }

  confirmAdmin(username: string, password: string) {
    let headers = new HttpHeaders();
    this.basicHash = 'Basic ' + btoa(username + ':' + password);
    headers = headers.append('Authorization', this.basicHash);

<<<<<<< HEAD
    return this.http.get('https://localhost:8443/admin/', { headers: headers, responseType: 'text' })
    .subscribe(response => {
      if (response === 'Admin confirmed') {
        this.authorized = true;
        this.router.navigateByUrl('/admin');
      } else {
        window.alert('Invalid credentials!');
      }
    });
=======
    return this.http.get('https://localhost:8443/admin/', {headers: headers})
      .subscribe(response => console.log(response), error => {
        if (error.status === 200) {
          this.authorized = true;
          this.router.navigateByUrl('/admin');
        } else {
          window.alert('Invalid credentials!');
        }
      });
>>>>>>> 19b67d2363119e110111f3298b09e46efd04151a
  }

  isAdminConfirmed() {
    return this.authorized;
  }

  getBasicHash(): string {
    return this.basicHash;
  }
}
