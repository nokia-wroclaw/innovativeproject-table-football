import { Component, OnInit } from '@angular/core';
import { DataService } from '../services/data.service';

@Component({
  selector: 'app-config-panel',
  templateUrl: './config-panel.component.html',
  styleUrls: ['./config-panel.component.css']
})
export class ConfigPanelComponent implements OnInit {
  floors: number[];

  constructor(private dataService: DataService) {
    this.fetchFloors();
  }

  ngOnInit() {
  }

  fetchFloors() {
    this.dataService.getFloors().subscribe((data: number[]) => this.floors = data);
  }
}
