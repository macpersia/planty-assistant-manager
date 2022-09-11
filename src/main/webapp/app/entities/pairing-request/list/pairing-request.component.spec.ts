import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { PairingRequestService } from '../service/pairing-request.service';

import { PairingRequestComponent } from './pairing-request.component';

describe('PairingRequest Management Component', () => {
  let comp: PairingRequestComponent;
  let fixture: ComponentFixture<PairingRequestComponent>;
  let service: PairingRequestService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'pairing-request', component: PairingRequestComponent }]), HttpClientTestingModule],
      declarations: [PairingRequestComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
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

  describe('trackId', () => {
    it('Should forward to pairingRequestService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getPairingRequestIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getPairingRequestIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
