import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegisterAlertsComponent } from './register-alerts.component';

describe('RegisterAlertsComponent', () => {
  let component: RegisterAlertsComponent;
  let fixture: ComponentFixture<RegisterAlertsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RegisterAlertsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RegisterAlertsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
