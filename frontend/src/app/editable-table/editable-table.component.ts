import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Table } from '../model/table';

@Component({
  selector: 'app-editable-table',
  templateUrl: './editable-table.component.html',
  styleUrls: ['./editable-table.component.css']
})
export class EditableTableComponent implements OnInit {
  @Input()
  tableData: Table;

  @Input()
  floorMin: number;

  @Input()
  floorMax: number;

  @Output() editStatus = new EventEmitter<number>();

  isEditable: boolean;

  constructor() { }

  ngOnInit() {
  }

  editOrSave() {
    if (this.tableData.floor.toString().length > 0 && this.tableData.room.toString().length > 0
        && this.tableData.floor >= this.floorMin && this.tableData.floor <= this.floorMax) {
      if (this.isEditable) {
        this.isEditable = false;
        this.editStatus.emit(-1);
      } else {
        this.isEditable = true;
        this.editStatus.emit(1);
      }
    } else {
      alert('Please insert a valid number!');
    }
  }
}
