import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-table',
  templateUrl: './table.component.html',
  styleUrls: ['./table.component.css']
})
export class TableComponent implements OnInit {
  @Input()
  tableStatus: string;

  @Input()
  roomNumber: number;

  constructor() { }

  ngOnInit() {
  }

  getTableStatusColor() {
    if (this.tableStatus === 'free') {
      return '#19B900';
    } else if (this.tableStatus === 'occupied') {
      return '#FF0000';
    } else if (this.tableStatus === 'inactive') {
      return '#A7A7A7';
    }

    return '#000000';
  }

}
