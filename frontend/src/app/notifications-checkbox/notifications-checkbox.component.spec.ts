import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NotificationsCheckboxComponent } from './notifications-checkbox.component';

describe('NotificationsCheckboxComponent', () => {
  let component: NotificationsCheckboxComponent;
  let fixture: ComponentFixture<NotificationsCheckboxComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NotificationsCheckboxComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NotificationsCheckboxComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
