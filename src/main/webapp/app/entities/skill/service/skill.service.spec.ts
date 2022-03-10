import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISkill, Skill } from '../skill.model';

import { SkillService } from './skill.service';

describe('Skill Service', () => {
  let service: SkillService;
  let httpMock: HttpTestingController;
  let elemDefault: ISkill;
  let expectedResult: ISkill | ISkill[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SkillService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      agentSharing: false,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Skill', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Skill()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Skill', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          agentSharing: true,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Skill', () => {
      const patchObject = Object.assign(
        {
          agentSharing: true,
        },
        new Skill()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Skill', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          agentSharing: true,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Skill', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSkillToCollectionIfMissing', () => {
      it('should add a Skill to an empty array', () => {
        const skill: ISkill = { id: 123 };
        expectedResult = service.addSkillToCollectionIfMissing([], skill);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(skill);
      });

      it('should not add a Skill to an array that contains it', () => {
        const skill: ISkill = { id: 123 };
        const skillCollection: ISkill[] = [
          {
            ...skill,
          },
          { id: 456 },
        ];
        expectedResult = service.addSkillToCollectionIfMissing(skillCollection, skill);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Skill to an array that doesn't contain it", () => {
        const skill: ISkill = { id: 123 };
        const skillCollection: ISkill[] = [{ id: 456 }];
        expectedResult = service.addSkillToCollectionIfMissing(skillCollection, skill);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(skill);
      });

      it('should add only unique Skill to an array', () => {
        const skillArray: ISkill[] = [{ id: 123 }, { id: 456 }, { id: 19941 }];
        const skillCollection: ISkill[] = [{ id: 123 }];
        expectedResult = service.addSkillToCollectionIfMissing(skillCollection, ...skillArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const skill: ISkill = { id: 123 };
        const skill2: ISkill = { id: 456 };
        expectedResult = service.addSkillToCollectionIfMissing([], skill, skill2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(skill);
        expect(expectedResult).toContain(skill2);
      });

      it('should accept null and undefined values', () => {
        const skill: ISkill = { id: 123 };
        expectedResult = service.addSkillToCollectionIfMissing([], null, skill, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(skill);
      });

      it('should return initial array if no Skill is added', () => {
        const skillCollection: ISkill[] = [{ id: 123 }];
        expectedResult = service.addSkillToCollectionIfMissing(skillCollection, undefined, null);
        expect(expectedResult).toEqual(skillCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
