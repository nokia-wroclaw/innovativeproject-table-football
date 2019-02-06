import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {Table} from '../model/table';
import {NotificationsPanelComponent} from '../notifications-panel/notifications-panel.component';
import {NotificationsService} from '../services/notifications.service';

@Component({
  selector: 'app-table-notifications-checkbox',
  templateUrl: './table-notifications-checkbox.component.html',
  styleUrls: ['./table-notifications-checkbox.component.css']
})
export class TableNotificationsCheckboxComponent implements OnInit {

  @Input()
  tableData: Table;
  @Output()
  changed = new EventEmitter();

  @Input()
  checked: boolean;

  constructor(private notificationsService: NotificationsService) {
  }

  ngOnInit() {
    this.checked = this.notificationsService.containsId(this.tableData.id);
  }

  handleNotificationsStateChange() {
    console.log('Notifications change for table with id ' + this.tableData.id);
    this.checked = !this.checked;
    const data: NotificationsEventData = {table: this.tableData, turnOn: this.checked};
    this.changed.emit(data);
  }

}

export interface NotificationsEventData {
  table: Table;
  turnOn: boolean;
}

