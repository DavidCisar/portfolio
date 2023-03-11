import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HeadComponentComponent } from './head-component.component';

describe('HeadComponentComponent', () => {
  let component: HeadComponentComponent;
  let fixture: ComponentFixture<HeadComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HeadComponentComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HeadComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
