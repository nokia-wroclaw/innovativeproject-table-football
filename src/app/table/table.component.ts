import { Component, OnInit, Input } from '@angular/core';
import { Table } from '../model/sensor';

@Component({
  selector: 'app-table',
  templateUrl: './table.component.html',
  styleUrls: ['./table.component.css']
})
export class TableComponent implements OnInit, Table {
  @Input()
  tableData: Table;

  id: string;
  active: boolean;
  online: boolean;
  lastNotificationDate: Date;
  floor: number;
  room: number;

  constructor() {
  }

  ngOnInit() {
    this.id = this.tableData.id;
    this.active = this.tableData.active;
    this.online = this.tableData.online;
    this.lastNotificationDate = this.tableData.lastNotificationDate;
    this.floor = this.tableData.floor;
    this.room = this.tableData.room;
  }

  getTableStatusColor() {
    if (this.online === false) {
      return '#A7A7A7';
    }

    if (this.active === false) {
      return '#19B900';
    } else if (this.active === true) {
      return '#FF0000';
    }

    return '#000000';
  }
}
