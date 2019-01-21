import {Injectable} from '@angular/core';
import {AlertComponent} from '../alert/alert.component';

@Injectable({
  providedIn: 'root'
})
export class AlertService {

  public alert: AlertComponent;
  hasAlert: boolean;

  constructor() {
    this.alert = new AlertComponent();
  }

  clearAlert() {
    this.alert.message = '';
    this.alert.type = 'none';
    this.hasAlert = false;
  }

  publishAlert(type: string, message: string) {
    console.log('publishAlert() called with parameters: ', type, message);
    this.alert.message = message;
    this.alert.type = type;
    this.hasAlert = true;
  }
}
