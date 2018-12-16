import { Component, ViewEncapsulation } from '@angular/core';
import { DataService } from './services/data.service';
import { Floor } from './model/floor';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class AppComponent {

  constructor() {
  }
}
