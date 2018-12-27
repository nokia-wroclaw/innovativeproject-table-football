import { Component, OnInit } from '@angular/core';
import { Table } from '../model/table';
import { DataService } from '../services/data.service';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { Floor } from '../model/floor';

@Component({
  selector: 'app-admin-sensor-info',
  templateUrl: './admin-sensor-info.component.html',
  styleUrls: ['./admin-sensor-info.component.css']
})
export class AdminSensorInfoComponent implements OnInit {
  tables: Table[];
  floorMin: number;
  floorMax: number;
  checksum: number;

  constructor(private dataService: DataService, private authService: AuthService, private router: Router) {
    this.fetchTables();
    this.fetchFloors();
    this.checksum = 0;
  }

  ngOnInit() {
  }

  fetchTables() {
    this.dataService.getSensorStatus().subscribe((data: Table[]) => {
      this.tables = data;
      this.tables.sort((a: Table, b: Table) => a.floor - b.floor);
    });
  }

  fetchFloors() {
    const floors = new Array<number>();

    this.dataService.getFloors().subscribe((floor: Floor) => {
      floors.push(floor.floorNumber);
    });
  }

  updateTables() {
    this.dataService.updateSensorStatus(this.tables);
  }

  editStatusChanged(status: number) {
    this.checksum += status;
  }

}
