import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Table } from '../model/table';
import { Floor } from '../model/floor';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DataService {
  floors: Array<Floor>;
  onlyFreeTables: boolean;

  constructor(private http: HttpClient) {
    this.onlyFreeTables = false;
    this.floors = new Array<Floor>();
  }

  getSensorStatus() {
    return this.http.get('http://localhost:8080/sensorStatus');
  }

  getFloors(): Observable<Floor> {
    return Observable.create(observer => {
      this.http.get('http://localhost:8080/sensorStatus').subscribe((tables: Table[]) => {
        if (this.floors.length === 0) {
          tables.forEach(table => {
            const floorNumber = table.floor;
            const floorIndex = this.floors.findIndex(elem => elem.floorNumber === floorNumber);

            if (floorIndex !== -1) {
              this.floors[floorIndex].tables.push(table);
            } else {
              const newFloorIndex = this.floors.push(new Floor(floorNumber)) - 1;
              this.floors[newFloorIndex].tables.push(table);
              this.floors.sort((a, b) => a.floorNumber - b.floorNumber);
            }
          });
        }
        this.floors.forEach(floor => {
          observer.next(floor);
        });
        observer.complete();
      });
    });
  }

  updateSensorStatus(sensors: Table[]) {
    let headers = new HttpHeaders();
    headers = headers.append('Authorization', 'Basic ' + btoa('admin:admin'));

    return this.http.post<Table[]>('https://localhost:8443/admin/', sensors, { headers: headers })
      .subscribe(response => {
        if (response == null) {
          window.alert('Updated successfully.');
        }
      }, error => {
        window.alert('Error occured. Status: ' + error.status);
      });
  }

  areFloorsEqual(first: Floor[], second: Floor[]) {
    if (!first || !second) {
      return false;
    }

    if (first.length !== second.length) {
      return false;
    }

    for (let i = 0; i < first.length; i++) {
      if (!this.areTablesEqual(first[i].tables, second[i].tables)) {
        return false;
      }
    }

    return true;
  }

  areTablesEqual(first: Table[], second: Table[]) {
    if (!first || !second) {
      return false;
    }

    if (first.length !== second.length) {
      return false;
    }

    for (let i = 0; i < first.length; i++) {
      if (!(
        first[i].id === second[i].id &&
        first[i].occupied === second[i].occupied &&
        first[i].online === second[i].online &&
        first[i].floor === second[i].floor &&
        first[i].room === second[i].room
      )) {
        return false;
      }
    }

    return true;
  }
}
