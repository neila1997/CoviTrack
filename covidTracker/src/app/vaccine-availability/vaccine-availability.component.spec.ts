import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VaccineAvailabilityComponent } from './vaccine-availability.component';

describe('VaccineAvailabilityComponent', () => {
  let component: VaccineAvailabilityComponent;
  let fixture: ComponentFixture<VaccineAvailabilityComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VaccineAvailabilityComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(VaccineAvailabilityComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
