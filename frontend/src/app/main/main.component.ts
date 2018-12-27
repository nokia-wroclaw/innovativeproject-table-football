import { Component, OnInit } from '@angular/core';
import { DataService } from '../services/data.service';
import { Floor } from '../model/floor';
import { timer } from 'rxjs';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent implements OnInit {
  floors: Array<Floor>;
  smallDevice: boolean;

  constructor(private dataService: DataService) {
    this.floors = new Array<Floor>();
    if (window.screen.width <= 768) {
      this.smallDevice = true;
    }

    this.getFloors();
    timer(0, 3000).subscribe(() => {
      this.dataService.floors = new Array<Floor>();
      this.getFloors();
    });
  }

  ngOnInit() {
  }

  getFloors() {
    const tempFloors = new Array<Floor>();
    this.dataService.getFloors().subscribe((floor: Floor) => tempFloors.push(floor),
      error => console.log(error), () => {
        if (!this.dataService.areFloorsEqual(tempFloors, this.floors)) {
          this.floors = tempFloors;
        }
      });
  }
}
