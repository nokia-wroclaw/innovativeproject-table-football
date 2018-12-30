import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule, Routes } from '@angular/router';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatRadioModule } from '@angular/material/radio';

import { DataService } from './services/data.service';
import { AuthService } from './services/auth.service';
import { CookieService } from 'ngx-cookie-service';

import { AppComponent } from './app.component';
import { NavComponent } from './nav/nav.component';
import { FloorComponent } from './floor/floor.component';
import { TableComponent } from './table/table.component';
import { ConfigPanelComponent } from './config-panel/config-panel.component';
import { CustomCheckboxComponent } from './custom-checkbox/custom-checkbox.component';
import { AdminPanelComponent } from './admin-panel/admin-panel.component';
import { MainComponent } from './main/main.component';
import { EditableTableComponent } from './editable-table/editable-table.component';
import { LoginComponent } from './login/login.component';
import { TableCheckboxComponent } from './table-checkbox/table-checkbox.component';
import { AdminSensorInfoComponent } from './admin-sensor-info/admin-sensor-info.component';
import { AdminParametersComponent } from './admin-calibration/admin-parameters.component';

@NgModule({
  declarations: [
    AppComponent,
    NavComponent,
    FloorComponent,
    TableComponent,
    ConfigPanelComponent,
    CustomCheckboxComponent,
    AdminPanelComponent,
    MainComponent,
    EditableTableComponent,
    LoginComponent,
    TableCheckboxComponent,
    AdminSensorInfoComponent,
    AdminParametersComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule.forRoot([
      { path: 'admin', component: AdminPanelComponent },
      { path: 'login', component: LoginComponent },
      { path: '', component: MainComponent }
    ]
    ),
    BrowserAnimationsModule,
    MatSidenavModule,
    MatSlideToggleModule,
    MatCheckboxModule,
    MatExpansionModule,
    MatButtonModule,
    MatInputModule,
    MatRadioModule
  ],
  providers: [DataService, AuthService, CookieService],
  bootstrap: [AppComponent]
})
export class AppModule { }
