import {Component, OnInit, Input, Output, EventEmitter} from '@angular/core';
import {Table} from '../model/table';
import {CalibrationService} from '../services/calibration.service';
import {HttpResponse} from '@angular/common/http';
import {error} from '@angular/compiler/src/util';

@Component({
  selector: 'app-editable-table',
  templateUrl: './editable-table.component.html',
  styleUrls: ['./editable-table.component.css']
})
export class EditableTableComponent implements OnInit {
  @Input()
  tableData: Table;

  @Output() editStatus = new EventEmitter<number>();

  isEditable: boolean;
  calibrationStatus: boolean;
  isCalibrationPending = false;

  constructor(private calibrationService: CalibrationService) {
    this.calibrationService = calibrationService;
  }

  ngOnInit() {
    this.calibrationStatus = true;
  }

  editOrSave() {
    if (this.tableData.floor.toString().length > 0 && this.tableData.room.toString().length > 0) {
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

  sendCalibrationRequest() {
    this.isCalibrationPending = true;
    this.calibrationService.sendCalibrationRequest(this.tableData.id)
      .subscribe((response: HttpResponse<string>) => {
        this.calibrationStatus = response.ok;
        this.isCalibrationPending = false;
      }, (err: Error) => {
        console.error('Error sending calibration request' + err);
        this.calibrationStatus = false;
        this.isCalibrationPending = false;
      });
  }
}
