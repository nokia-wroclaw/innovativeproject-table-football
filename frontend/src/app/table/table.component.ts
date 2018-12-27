import { Component, OnInit, Input } from '@angular/core';
import { Table } from '../model/table';

@Component({
  selector: 'app-table',
  templateUrl: './table.component.html',
  styleUrls: ['./table.component.css']
})
export class TableComponent implements OnInit {
  @Input()
  tableData: Table;

  constructor() {
  }

  ngOnInit() {
  }

  getTableStatusColor() {
    if (this.tableData.online === false) {
      return '#A7A7A7';
    }

    if (this.tableData.occupied === false) {
      return '#19B900';
    } else if (this.tableData.occupied === true) {
      return '#FF0000';
    }

    return '#000000';
  }
}
