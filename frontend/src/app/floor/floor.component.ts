import { Component, OnInit, Input } from '@angular/core';
import {
  trigger,
  state,
  style,
  animate,
  transition,
} from '@angular/animations';
import { Table } from '../model/table';
import { DataService } from '../services/data.service';
import { interval, timer } from 'rxjs';
import { Floor } from '../model/floor';

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
  floorData: Floor;

  @Input()
  sidenav;

  isOpen = false;
  smallDevice = false;

  constructor(private dataService: DataService) {
    if (window.screen.width <= 768) {
      this.smallDevice = true;
    }
  }

  ngOnInit() {
  }

  toggle() {
    this.isOpen = !this.isOpen;
  }
}
