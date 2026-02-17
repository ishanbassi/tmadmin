import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateTrademarkNameComponent } from './update-trademark-name.component';

describe('UpdateTrademarkNameComponent', () => {
  let component: UpdateTrademarkNameComponent;
  let fixture: ComponentFixture<UpdateTrademarkNameComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UpdateTrademarkNameComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(UpdateTrademarkNameComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
