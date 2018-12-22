import { Component, OnInit } from '@angular/core';
import { DataService } from '../services/data.service';
import { Floor } from '../model/floor';

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
    this.getFloors();
    if (window.screen.width <= 768) {
      this.smallDevice = true;
    }
  }

  ngOnInit() {
  }

  getFloors() {
    this.dataService.getFloors().subscribe((floor: Floor) => this.floors.push(floor));
  }
}
