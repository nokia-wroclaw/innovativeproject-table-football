import { Injectable } from '@angular/core';
import { Floor } from '../model/floor';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class DataService {

  constructor(private http: HttpClient) { }

  getFloors() {
    return this.http.get('assets/floors.json');
  }
}
