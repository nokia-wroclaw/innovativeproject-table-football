import {Injectable} from '@angular/core';
import {AlertComponent} from '../alert/alert.component';

@Injectable({
  providedIn: 'root'
})
export class AlertService {

  type: string;
  message: string;
  hasAlert: boolean;

  constructor() {}

  clearAlert() {
    this.message = '';
    this.type = 'none';
    this.hasAlert = false;
  }

  publishAlert(type: string, message: string) {
    console.log('publishAlert() called with parameters: ', type, message);
    this.message = message;
    this.type = type;
    this.hasAlert = true;
  }
}
