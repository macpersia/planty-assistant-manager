package be.planty.managers.assistant.web.rest;
import be.planty.managers.assistant.service.SkillService;
import be.planty.managers.assistant.web.rest.errors.BadRequestAlertException;
import be.planty.managers.assistant.web.rest.util.HeaderUtil;
import be.planty.managers.assistant.service.dto.SkillDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Skill.
 */
@RestController
@RequestMapping("/api")
public class SkillResource {

    private final Logger log = LoggerFactory.getLogger(SkillResource.class);

    private static final String ENTITY_NAME = "skill";

    private final SkillService skillService;

    public SkillResource(SkillService skillService) {
        this.skillService = skillService;
    }

    /**
     * POST  /skills : Create a new skill.
     *
     * @param skillDTO the skillDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new skillDTO, or with status 400 (Bad Request) if the skill has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/skills")
    public ResponseEntity<SkillDTO> createSkill(@RequestBody SkillDTO skillDTO) throws URISyntaxException {
        log.debug("REST request to save Skill : {}", skillDTO);
        if (skillDTO.getId() != null) {
            throw new BadRequestAlertException("A new skill cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SkillDTO result = skillService.save(skillDTO);
        return ResponseEntity.created(new URI("/api/skills/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /skills : Updates an existing skill.
     *
     * @param skillDTO the skillDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated skillDTO,
     * or with status 400 (Bad Request) if the skillDTO is not valid,
     * or with status 500 (Internal Server Error) if the skillDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/skills")
    public ResponseEntity<SkillDTO> updateSkill(@RequestBody SkillDTO skillDTO) throws URISyntaxException {
        log.debug("REST request to update Skill : {}", skillDTO);
        if (skillDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SkillDTO result = skillService.save(skillDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, skillDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /skills : get all the skills.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of skills in body
     */
    @GetMapping("/skills")
    public List<SkillDTO> getAllSkills(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Skills");
        return skillService.findAll();
    }

    /**
     * GET  /skills/:id : get the "id" skill.
     *
     * @param id the id of the skillDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the skillDTO, or with status 404 (Not Found)
     */
    @GetMapping("/skills/{id}")
    public ResponseEntity<SkillDTO> getSkill(@PathVariable Long id) {
        log.debug("REST request to get Skill : {}", id);
        Optional<SkillDTO> skillDTO = skillService.findOne(id);
        return ResponseUtil.wrapOrNotFound(skillDTO);
    }

    /**
     * DELETE  /skills/:id : delete the "id" skill.
     *
     * @param id the id of the skillDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/skills/{id}")
    public ResponseEntity<Void> deleteSkill(@PathVariable Long id) {
        log.debug("REST request to delete Skill : {}", id);
        skillService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
