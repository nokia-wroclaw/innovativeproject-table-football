import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { RouterModule, Routes } from '@angular/router';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatButtonModule } from '@angular/material/button';

import { DataService } from './services/data.service';

import { AppComponent } from './app.component';
import { NavComponent } from './nav/nav.component';
import { FloorComponent } from './floor/floor.component';
import { TableComponent } from './table/table.component';
import { ConfigPanelComponent } from './config-panel/config-panel.component';
import { CustomCheckboxComponent } from './custom-checkbox/custom-checkbox.component';
import { AdminPanelComponent } from './admin-panel/admin-panel.component';
import { MainComponent } from './main/main.component';

@NgModule({
  declarations: [
    AppComponent,
    NavComponent,
    FloorComponent,
    TableComponent,
    ConfigPanelComponent,
    CustomCheckboxComponent,
    AdminPanelComponent,
    MainComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    RouterModule.forRoot([
      { path: 'admin', component: AdminPanelComponent },
      { path: '', component: MainComponent }
    ]
    ),
    BrowserAnimationsModule,
    MatSidenavModule,
    MatSlideToggleModule,
    MatCheckboxModule,
    MatExpansionModule,
    MatButtonModule
  ],
  providers: [DataService],
  bootstrap: [AppComponent]
})
export class AppModule { }
