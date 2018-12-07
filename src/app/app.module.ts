import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
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

@NgModule({
  declarations: [
    AppComponent,
    NavComponent,
    FloorComponent,
    TableComponent,
    ConfigPanelComponent,
    CustomCheckboxComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
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
