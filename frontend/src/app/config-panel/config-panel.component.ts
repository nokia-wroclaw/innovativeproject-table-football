import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { DataService } from '../services/data.service';
import { Floor } from '../model/floor';
import { timer } from 'rxjs';
import { CookieService } from 'ngx-cookie-service';
import { Table } from '../model/table';
import { fillProperties } from '@angular/core/src/util/property';

@Component({
  selector: 'app-config-panel',
  templateUrl: './config-panel.component.html',
  styleUrls: ['./config-panel.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class ConfigPanelComponent implements OnInit {
  floors: Array<Floor>;

  constructor(private dataService: DataService, private cookieService: CookieService) {
    this.floors = new Array<Floor>();
    this.getFloors();
    timer(500, 3000).subscribe(() => {
      this.getFloors();
    });
  }

  ngOnInit() {
  }

  getFloors() {
    const tempFloors = new Array<Floor>();
    this.dataService.getFloors().subscribe((floor: Floor) => tempFloors.push(floor),
      error => console.log(error), () => {
        if (!this.dataService.areFloorsEqual(tempFloors, this.floors)) {
          this.floors = tempFloors;
          this.getCookies();
        }
      });
  }

  getCookies() {
    const cookie = this.cookieService.get('table-football-config');

    if (cookie.length === 0) {
      this.floors.forEach(floor => {
        floor.visible = true;
        floor.tables.forEach(table => table.visible = true);
      });
    } else {
      this.setVisibleFloors(cookie);
      this.setVisibleTables(cookie);
    }
  }

  setVisibleFloors(cookie: string) {
    const cookieObject: Floor[] = JSON.parse(cookie);
    cookieObject.forEach(cookieFloor => {
      const foundFloor = this.floors.find(element => element.floorNumber === cookieFloor.floorNumber);

      if (foundFloor !== undefined) {
        foundFloor.visible = cookieFloor.visible;
      }

    });
  }

  setVisibleTables(cookie: string) {
    const tables = new Array<Table>();
    const cookieTables = new Array<Table>();
    const cookieObject: Floor[] = JSON.parse(cookie);

    cookieObject.forEach(floor => {
      floor.tables.forEach(table => cookieTables.push(table));
    });

    this.floors.forEach(floor => {
      floor.tables.forEach(table => tables.push(table));
    });

    cookieTables.forEach(table => {
      const foundTable = tables.find(element => element.id === table.id);

      if (foundTable !== undefined) {
        console.log('chuj');
        foundTable.visible = table.visible;
      }
    });
  }

  setCookies() {
    this.cookieService.set('table-football-config', JSON.stringify(this.floors));
  }

  slideChanged(event) {
    this.dataService.onlyFreeTables = event.checked;
  }

  clear() {
    this.floors.forEach(floor => {
      floor.visible = true;
      floor.tables.forEach(table => table.visible = true);
    });

    this.setCookies();
  }
}
