import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PairingRequestService } from '../service/pairing-request.service';

import { PairingRequestComponent } from './pairing-request.component';

describe('PairingRequest Management Component', () => {
  let comp: PairingRequestComponent;
  let fixture: ComponentFixture<PairingRequestComponent>;
  let service: PairingRequestService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PairingRequestComponent],
    })
      .overrideTemplate(PairingRequestComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PairingRequestComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(PairingRequestService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.pairingRequests?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
