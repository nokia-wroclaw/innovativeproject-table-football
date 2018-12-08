import { Component, OnInit } from '@angular/core';
import { DataService } from '../services/data.service';
import { Table } from '../model/table';

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

  constructor(private dataService: DataService) {
    this.fetchTables();
    this.fetchFloors();
    this.checksum = 0;
  }

  ngOnInit() {
  }

  fetchTables() {
    this.dataService.getSensorStatus().subscribe((data: Table[]) => this.tables = data);
  }

  fetchFloors() {
    this.dataService.getFloors().subscribe((data: number[]) => {
      data = data.sort();
      this.floorMin = data[0];
      this.floorMax = data[data.length - 1];
    });
  }

  updateTables() {
    this.tables.forEach((table: Table) => {
      console.log(table);
    });

    this.dataService.updateSensorStatus(this.tables).subscribe(resposne => console.log(resposne));
  }

  editStatusChanged(status: number) {
    this.checksum += status;
  }
}
