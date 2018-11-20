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
  floors: Floor[];
  showOnlyFreeTables: boolean;
  smallDevice: boolean;

  constructor(private dataService: DataService) {
    this.getFloors();
    if (window.screen.width <= 768) {
      this.smallDevice = true;
    }
  }

  getFloors() {
    this.dataService.getFloors().subscribe((data: Floor[]) => {
      this.floors = data;
    });
  }
}
