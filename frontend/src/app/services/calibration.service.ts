import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {AuthService} from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class CalibrationService {
  private calibrationUrl = 'https://localhost:8443/admin/calibration/';

  constructor(private http: HttpClient, private authorizationService: AuthService) {
  }

  sendCalibrationRequest(sensorId: string) {
    if (this.authorizationService.isAdminConfirmed()) {

      const httpHeaders = new HttpHeaders({
        'Authorization': this.authorizationService.getBasicHash()
      });

      return this.http.post
      (this.calibrationUrl + sensorId, null,
        {headers: httpHeaders, observe: 'response'});
    }
  }
}
