import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { DataService } from '../services/data.service';
import { Floor } from '../model/floor';
import { timer } from 'rxjs';

@Component({
  selector: 'app-config-panel',
  templateUrl: './config-panel.component.html',
  styleUrls: ['./config-panel.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class ConfigPanelComponent implements OnInit {
  floors: Array<Floor>;

  constructor(private dataService: DataService) {
    this.floors = new Array<Floor>();
    this.getFloors();
    timer(500, 3000).subscribe(() => {
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

  slideChanged(event) {
    this.dataService.onlyFreeTables = event.checked;
  }
}
