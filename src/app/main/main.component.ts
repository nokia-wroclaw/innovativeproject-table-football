import { Component, OnInit } from '@angular/core';
import { DataService } from '../services/data.service';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent implements OnInit {
  floors: number[];
  smallDevice: boolean;

  constructor(private dataService: DataService) {
    this.fetchFloors();
    if (window.screen.width <= 768) {
      this.smallDevice = true;
    }
    this.dataService.getAdminData().subscribe((data) => {
      console.log(data);
    });
  }

  ngOnInit() {
  }

  fetchFloors() {
    this.dataService.getFloors().subscribe((data: number[]) => this.floors = data);
  }
}
