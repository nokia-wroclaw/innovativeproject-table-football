import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class DataService {

  constructor(private http: HttpClient) { }

  getFloors() {
    return this.http.get('assets/floors.json');
  }

  getSensorStatus() {
    return this.http.get('http://localhost:8080/sensorStatus');
  }

  getAdminData() {
    let headers = new HttpHeaders();
    headers = headers.append('Authorization', 'Basic ' + btoa('admin:admin'));
    headers = headers.append('Contsent-Type', 'application/x-www-form-urlencoded');

    return this.http.get('https://localhost:8443/admin', { headers: headers });
  }
}
