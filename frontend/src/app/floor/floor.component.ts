import { Component, OnInit, Input } from '@angular/core';
import {
  trigger,
  state,
  style,
  animate,
  transition,
} from '@angular/animations';
import { DataService } from '../services/data.service';
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
}
