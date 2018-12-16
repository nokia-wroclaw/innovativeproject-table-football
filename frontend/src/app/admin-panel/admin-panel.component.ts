import { Component, OnInit } from '@angular/core';
import { DataService } from '../services/data.service';
import { Table } from '../model/table';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-panel',
  templateUrl: './admin-panel.component.html',
  styleUrls: ['./admin-panel.component.css']
})
export class AdminPanelComponent implements OnInit {
  tables: Table[];
  floorMin: number;
  floorMax: number;
  checksum: number;

  constructor(private dataService: DataService, private authService: AuthService, private router: Router) {
    if (!this.authService.isAdminConfirmed()) {
      this.router.navigateByUrl('/login');
    }
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
    this.dataService.getFloors().subscribe((data: number[]) => {
      data = data.sort();
      this.floorMin = data[0];
      this.floorMax = data[data.length - 1];
    });
  }

  updateTables() {
    this.dataService.updateSensorStatus(this.tables);
  }

  editStatusChanged(status: number) {
    this.checksum += status;
  }
}
