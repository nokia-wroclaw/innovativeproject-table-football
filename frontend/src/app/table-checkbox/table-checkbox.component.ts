import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Table } from '../model/table';

@Component({
  selector: 'app-table-checkbox',
  templateUrl: './table-checkbox.component.html',
  styleUrls: ['./table-checkbox.component.css']
})
export class TableCheckboxComponent implements OnInit {
  @Input()
  tableData: Table;
  @Output()
  changed = new EventEmitter();

  constructor() { }

  ngOnInit() {
  }

  checked(event) {
    this.tableData.visible = event.checked;
    this.changed.emit();
  }
}
