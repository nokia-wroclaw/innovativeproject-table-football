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
  floors: number[];
  showOnlyFreeTables = false;
  smallDevice: boolean;

  constructor(private dataService: DataService) {
    this.fetchFloors();
    if (window.screen.width <= 768) {
      this.smallDevice = true;
    }
  }

  fetchFloors() {
    this.dataService.getFloors().subscribe((data: number[]) => this.floors = data);
  }

  slideToggleChanged(event) {
    this.showOnlyFreeTables = event.checked;
  }
}
