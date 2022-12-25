import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PbtfComponent } from './pbtf.component';

describe('PbtfComponent', () => {
  let component: PbtfComponent;
  let fixture: ComponentFixture<PbtfComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PbtfComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PbtfComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
