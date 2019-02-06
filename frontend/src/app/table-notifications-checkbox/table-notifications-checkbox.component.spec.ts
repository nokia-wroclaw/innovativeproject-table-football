import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TableNotificationsCheckboxComponent } from './table-notifications-checkbox.component';

describe('TableNotificationsCheckboxComponent', () => {
  let component: TableNotificationsCheckboxComponent;
  let fixture: ComponentFixture<TableNotificationsCheckboxComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TableNotificationsCheckboxComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TableNotificationsCheckboxComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
