import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Table } from '../model/table';

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

  updateSensorStatus(sensors: Table[]) {
    let headers = new HttpHeaders();
    headers = headers.append('Authorization', 'Basic ' + btoa('admin:admin'));

    return this.http.post<Table[]>('https://localhost:8443/admin/', sensors, { headers: headers });
  }
}
