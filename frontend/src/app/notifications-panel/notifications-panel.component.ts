import {Component, EventEmitter, Input, OnChanges, OnInit, Output, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {Floor} from '../model/floor';
import {DataService} from '../services/data.service';
import {CookieService} from 'ngx-cookie-service';
import {NotificationsService} from '../services/notifications.service';
import {
  NotificationsEventData,
  TableNotificationsCheckboxComponent
} from '../table-notifications-checkbox/table-notifications-checkbox.component';

@Component({
  selector: 'app-notifications-panel',
  templateUrl: './notifications-panel.component.html',
  styleUrls: ['./notifications-panel.component.css']
})
export class NotificationsPanelComponent implements OnChanges {

  @Input()
  floors: Array<Floor>;

  @ViewChildren(TableNotificationsCheckboxComponent) tableComponents: QueryList<TableNotificationsCheckboxComponent>;

  constructor(private dataService: DataService, private cookieService: CookieService,
              private notificationsService: NotificationsService) {
  }

  ngOnChanges() {
    console.log('changes occured in notifications panel');
  }

  handleNotificationsChange(event: NotificationsEventData) {
    console.log('handleNotificationsChange called for table on floor '
      + event.table.floor + ' room ' + event.table.room + ' with turnOn set to ' + event.turnOn);
    this.notificationsService.switchNotificationsState(event.table, event.turnOn);
  }

  clearNotifications() {
    this.notificationsService.clearAll();
    this.tableComponents.forEach(item => item.checked = false);
  }
}
