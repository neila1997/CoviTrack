import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StateTrackerComponent } from './state-tracker.component';

describe('StateTrackerComponent', () => {
  let component: StateTrackerComponent;
  let fixture: ComponentFixture<StateTrackerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StateTrackerComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StateTrackerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
