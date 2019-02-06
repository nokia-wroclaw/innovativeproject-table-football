import {Injectable} from '@angular/core';
import {Table} from '../model/table';
import {DataService} from './data.service';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class NotificationsService {

  tables: Table[] = [];
  public permission = 'default';

  constructor(private dataService: DataService) {
    this.permission = NotificationsService.isSupported() ? 'default' : 'denied';
  }

  public static isSupported(): boolean {
    return 'Notification' in window;
  }

  requestPermission(): void {
    const self = this;
    if ('Notification' in window) {
      Notification.requestPermission((status) => {
        return self.permission = status;
      });
      console.log('permission requested');
    }
  }

  create(title: string, options ?: PushNotification): any {
    const self = this;
    return new Observable(function (obs) {
      if (!('Notification' in window)) {
        console.log('Notifications are not available in this environment');
        obs.complete();
      }
      if (self.permission !== 'granted') {
        console.log('The user hasn\'t granted you permission to send push notifications');
        obs.complete();
      }
      const notify = new Notification(title, options);
      notify.onshow = function (e) {
        return obs.next({
          notification: notify,
          event: e
        });
      };
      notify.onclick = function (e) {
        return obs.next({
          notification: notify,
          event: e
        });
      };
      notify.onerror = function (e) {
        return obs.error({
          notification: notify,
          event: e
        });
      };
      notify.onclose = function () {
        return obs.complete();
      };
    });
  }

  generateNotification(table: Table): void {
    const options = {
      body: 'Table on floor ' + table.floor + ' in room ' + table.room + ' is now free',
       icon: 'src/assets/ball_64.png'
    };
    const notify = this.create('Table Free', options).subscribe();
    console.log('Notification sent for table with id ' + table.id);
  }

  clearAll() {
    this.tables.splice(0, this.tables.length);
  }

  switchNotificationsState(table: Table, setNotifications: boolean) {
    console.log('switchNotificationsState called for table ' + table.id);
    const
      foundIndex = this.tables.indexOf(table);

    if (setNotifications) {
      if (foundIndex < 0) {
        this.tables.push(table);
      }
    } else {
      if (foundIndex > 0) {
        this.tables.splice(foundIndex, 1);
      }
    }

    console.log('tables are ');
    this.tables.forEach((t, i, a) => {
        console.log('Floor: ' + t.floor + ' Room: ' + t.room);
      }
    );
  }

  onDatasetChange() {
    console.log('onDatasetChange() called');

    console.log('tables before ');
    this.tables.forEach((t, i, a) => {
        console.log('Floor: ' + t.floor + ' Room: ' + t.room + ' Occupied: ' + t.occupied);
      }
    );

    this.dataService.updateTables(this.tables);

    console.log('tables after ');
    this.tables.forEach((t, i, a) => {
        console.log('Floor: ' + t.floor + ' Room: ' + t.room + ' Occupied: ' + t.occupied);
      }
    );

    this.tables.filter((table, index, array) => {
      return table.online && !table.occupied;
    }).forEach((table, index, tables) => {
      this.generateNotification(table);
    });

    this.tables = this.tables.filter((table) => {
      return table.occupied;
    });

  }

  containsId(id: string) {
    this.tables.forEach((table) => {
      if (table.id === id) {
        return true;
      }
    });
    return false;
  }
}

export interface PushNotification {
  body?: string;
  icon?: string;
  tag?: string;
  data?: any;
  renotify?: boolean;
  silent?: boolean;
  sound?: string;
  noscreen?: boolean;
  sticky?: boolean;
  dir?: 'auto' | 'ltr' | 'rtl';
  lang?: string;
  vibrate?: number[];
}
