import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { AlgorithmParameters } from '../model/algorithm-parameters';
import { ParametersService } from '../services/parameters.service';

@Component({
  selector: 'app-admin-parameters',
  templateUrl: './admin-parameters.component.html',
  styleUrls: ['./admin-parameters.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class AdminParametersComponent implements OnInit {
  parameters: AlgorithmParameters;

  constructor(private parametersService: ParametersService) {
    this.parameters = new AlgorithmParameters();
  }

  ngOnInit() {
  }

  sendParameters() {
    this.parametersService.sendParameters(this.parameters)
    .subscribe(response => {
      if (response.status === 200) {
        window.alert('Success!');
      }
    });
  }
}
