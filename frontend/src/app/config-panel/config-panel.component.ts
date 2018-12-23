import { Component, OnInit, ViewEncapsulation, Output, EventEmitter } from '@angular/core';
import { DataService } from '../services/data.service';
import { Floor } from '../model/floor';

@Component({
  selector: 'app-config-panel',
  templateUrl: './config-panel.component.html',
  styleUrls: ['./config-panel.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class ConfigPanelComponent implements OnInit {
  floors: Array<Floor>;

  @Output()
  filtered = new EventEmitter<number[]>();

  constructor(private dataService: DataService) {
    this.floors = new Array<Floor>();
    this.getFloors();
  }

  ngOnInit() {
  }

  getFloors() {
    this.dataService.getFloors().subscribe((floor: Floor) => this.floors.push(floor));
  }
}
