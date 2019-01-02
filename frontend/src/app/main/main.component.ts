import { Component, OnInit } from '@angular/core';
import { DataService } from '../services/data.service';
import { Floor } from '../model/floor';
import { timer } from 'rxjs';
import { Table } from '../model/table';

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
          // this.floors = tempFloors;
          this.updateFloors(tempFloors);
          this.sort();
        }
      });
  }

  sort() {
    this.floors.sort((a, b) => a.floorNumber - b.floorNumber);
    this.floors.forEach(floor => floor.tables.sort((a, b) => a.room - b.room));
  }

  updateFloors(incomingFloors: Floor[]) {
    // filtering floors
    this.floors = this.floors
    .filter(floor => incomingFloors.find(incomingFloor => incomingFloor.floorNumber === floor.floorNumber) !== undefined);

    // filtering tables
    this.floors.forEach(floor => {
      const incomingTables = incomingFloors.find(incomingFloor => incomingFloor.floorNumber === floor.floorNumber).tables;

      floor.tables = floor.tables
      .filter(table => incomingTables.find(incomingTable => this.areTablesEqual(incomingTable, table)) !== undefined);
    });

    // inserting new floors
    incomingFloors.forEach(incomingFloor => {
      const foundFloor = this.floors.find(floor => floor.floorNumber === incomingFloor.floorNumber);
      if (foundFloor === undefined) {
        this.floors.push(incomingFloor);
      } else {
        // inserting new tables
        incomingFloor.tables.forEach(incomingTable => {
          const foundTable = foundFloor.tables.find(table => this.areTablesEqual(table, incomingTable));
          if (foundTable === undefined) {
            foundFloor.tables.push(incomingTable);
          }
        });
      }
    });
  }

  areTablesEqual(first: Table, second: Table) {
    if (!(
      first.id === second.id &&
      first.occupied === second.occupied &&
      first.online === second.online &&
      first.room === second.room
    )) {
      return false;
    }

    return true;
  }
}
