import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {animate, keyframes, state, style, transition, trigger} from '@angular/animations';
import {Floor} from '../model/floor';

@Component({
  selector: 'app-notifications-checkbox',
  templateUrl: './notifications-checkbox.component.html',
  styleUrls: ['./notifications-checkbox.component.css'],
  animations: [
    trigger('check', [
      state('checked', style({
        opacity: 1
      })),
      state('unchecked', style({
        opacity: .3
      })),
      transition('unchecked => checked', animate('0.5s', keyframes([
        style({opacity: 0.1, offset: 0.1}),
        style({opacity: 0.6, offset: 0.5}),
        style({opacity: 0.8, offset: 0.7})
      ]))),
      transition('checked => unchecked', animate('0.5s', keyframes([
        style({opacity: 0.7, offset: 0.1}),
        style({opacity: 0.4, offset: 0.4}),
        style({opacity: 0.2, offset: 0.6}),
        style({opacity: 0.25, offset: 0.9})
      ])))
    ]),
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
export class NotificationsCheckboxComponent implements OnInit {
  @Input()
  checked: boolean;
  @Input()
  floorData: Floor;
  @Output()
  changed = new EventEmitter();

  constructor() {
  }

  ngOnInit() {
    this.checked = this.floorData.visible;
  }

  toggle() {
    this.checked = !this.checked;
  }
}
