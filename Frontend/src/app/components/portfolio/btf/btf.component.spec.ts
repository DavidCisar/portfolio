import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BtfComponent } from './btf.component';

describe('BtfComponent', () => {
  let component: BtfComponent;
  let fixture: ComponentFixture<BtfComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BtfComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BtfComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
