import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class ParametersService {
  parametersUrl = 'https://localhost:8443/admin/calibration/algorithm/';

  constructor(private http: HttpClient, private authorizationService: AuthService) {
  }

  sendParameters(parameters: any) {
    if (this.authorizationService.isAdminConfirmed()) {

      const httpHeaders = new HttpHeaders({
        'Authorization': this.authorizationService.getBasicHash()
      });

      return this.http.post
      (this.parametersUrl, parameters,
        {headers: httpHeaders, observe: 'response'});
    }
  }
}
