import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { AgentService } from '../service/agent.service';

import { AgentComponent } from './agent.component';

describe('Agent Management Component', () => {
  let comp: AgentComponent;
  let fixture: ComponentFixture<AgentComponent>;
  let service: AgentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [AgentComponent],
    })
      .overrideTemplate(AgentComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AgentComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(AgentService);

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
    expect(comp.agents?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
