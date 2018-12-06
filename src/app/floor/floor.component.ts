import { Component, OnInit, Input } from '@angular/core';
import {
  trigger,
  state,
  style,
  animate,
  transition,
} from '@angular/animations';
import { Table } from '../model/sensor';
import { DataService } from '../services/data.service';
import { interval, timer } from 'rxjs';

@Component({
  selector: 'app-floor',
  templateUrl: './floor.component.html',
  styleUrls: ['./floor.component.css'],
  animations: [
    trigger('openClose', [
      state('open', style({
        height: '*'
      })),
      state('closed', style({
        height: '0px'
      })),
      transition('open <=> closed', [
        animate('200ms ease-in-out')
      ])
    ])
  ]
})
export class FloorComponent implements OnInit {
  @Input()
  floorNumber: number;

  @Input()
  showOnlyFreeTables: boolean;

  @Input()
  sidenav;

  isOpen = false;
  smallDevice = false;
  tables: Array<Table>;
  timerSub;

  constructor(private dataService: DataService) {
    // this.fetchTables();
    if (window.screen.width <= 768) {
      this.smallDevice = true;
    }
    // this.initiateTimer();
  }

  ngOnInit() {
  }

  toggle() {
    this.isOpen = !this.isOpen;
  }

  initiateTimer() {
    this.timerSub = interval(2000);
    this.timerSub.subscribe(() => {
      this.fetchTables();
      console.log(this.tables);
    });
  }

  fetchTables() {
    this.dataService.getSensorStatus().subscribe((data: Table[]) => {
      this.tables = new Array<Table>();
      data.forEach((table: Table) => {
        if (table.floor === this.floorNumber) {
          this.tables.push(table);
        }
      });
    });
  }
}
