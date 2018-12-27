import { Component, OnInit, Input } from '@angular/core';
import {
  trigger,
  state,
  style,
  animate,
  transition
} from '@angular/animations';
import { DataService } from '../services/data.service';
import { Floor } from '../model/floor';
import { Table } from '../model/table';

@Component({
  selector: 'app-floor',
  templateUrl: './floor.component.html',
  styleUrls: ['./floor.component.css'],
  animations: [
    trigger('openClose', [
      state('open', style({
        height: '*',
        opacity: 1
      })),
      state('closed', style({
        height: '0px',
        opacity: 0
      })),
      transition('open <=> closed', [
        animate('200ms ease-in-out')
      ])
    ]),
    trigger('showHide', [
      state('hidden', style({
        height: '0px',
        opacity: 0,
        display: 'none'
      })),
      state('visible', style({
        height: '*',
        opacity: 1,
        display: 'block'
      })),
      transition('visible <=> hidden', [
        animate('150ms ease-in-out')
      ])
    ]),
    trigger('showHideTable', [
      transition(':enter', [
        style({
          transform: 'translateX(-100%)'
        }),
        animate('100ms ease-in-out', style({
          transform: 'translateX(0%)'
        }))
      ]),
      transition(':leave', [
        animate('100ms ease-in-out', style({
          transform: 'translateX(-100%)'
        }))
      ])
    ]),
    trigger('noTablesVisible', [
      transition(':enter', [
        style({ height: '0px', opacity: 0 }),
        animate('200ms ease-in-out', style({
          height: '*',
          opacity: 1
        }))
      ]),
      transition(':leave', [
        animate('200ms ease-in-out', style({
          height: '0px',
          opacity: 0
        }))
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
    this.isOpen = false;
  }

  toggle() {
    this.isOpen = !this.isOpen;
  }

  isVisible() {
    return this.floorData.visible === true || this.floorData.visible === undefined;
  }

  isTableVisible(table: Table) {
    return (table.visible === true || table.visible === undefined) &&
     ((this.dataService.onlyFreeTables && !table.occupied) || !this.dataService.onlyFreeTables);
  }

  anyTableVisible() {
    return !(this.floorData.getVisibleTablesCount() <= 0 ||
    (this.dataService.onlyFreeTables && this.floorData.getFreeTablesCount() <= 0));
  }
}
