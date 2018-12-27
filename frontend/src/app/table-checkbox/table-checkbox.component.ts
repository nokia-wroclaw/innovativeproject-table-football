import { Component, OnInit, Input } from '@angular/core';
import { Table } from '../model/table';
import {
  trigger,
  state,
  style,
  animate,
  transition,
} from '@angular/animations';

@Component({
  selector: 'app-table-checkbox',
  templateUrl: './table-checkbox.component.html',
  styleUrls: ['./table-checkbox.component.css']
})
export class TableCheckboxComponent implements OnInit {
  @Input()
  tableData: Table;

  constructor() { }

  ngOnInit() {
    this.tableData.visible = true;
  }

  changed(event) {
    this.tableData.visible = event.checked;
  }
}
