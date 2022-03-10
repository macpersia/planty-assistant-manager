import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PairingRequestDetailComponent } from './pairing-request-detail.component';

describe('PairingRequest Management Detail Component', () => {
  let comp: PairingRequestDetailComponent;
  let fixture: ComponentFixture<PairingRequestDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PairingRequestDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ pairingRequest: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PairingRequestDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PairingRequestDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load pairingRequest on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.pairingRequest).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
