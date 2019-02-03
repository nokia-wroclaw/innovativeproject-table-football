import {Component, Input, OnInit} from '@angular/core';
import { AlertService } from '../services/alert.service';

@Component({
  selector: 'app-alert',
  templateUrl: './alert.component.html',
  styleUrls: ['./alert.component.css']
})
export class AlertComponent implements OnInit {

  @Input() type: string;
  @Input() message: string;

  constructor(private alertService: AlertService) {
  }

  ngOnInit() {
  }

  dismissAlert() {
    this.alertService.clearAlert();
  }
}
