package be.planty.assistant.web.rest;

import be.planty.assistant.repository.PairingRequestRepository;
import be.planty.assistant.service.PairingRequestService;
import be.planty.assistant.service.dto.PairingRequestDTO;
import be.planty.assistant.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link be.planty.assistant.domain.PairingRequest}.
 */
@RestController
@RequestMapping("/api")
public class PairingRequestResource {

    private final Logger log = LoggerFactory.getLogger(PairingRequestResource.class);

    private static final String ENTITY_NAME = "pairingRequest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PairingRequestService pairingRequestService;

    private final PairingRequestRepository pairingRequestRepository;

    public PairingRequestResource(PairingRequestService pairingRequestService, PairingRequestRepository pairingRequestRepository) {
        this.pairingRequestService = pairingRequestService;
        this.pairingRequestRepository = pairingRequestRepository;
    }

    /**
     * {@code POST  /pairing-requests} : Create a new pairingRequest.
     *
     * @param pairingRequestDTO the pairingRequestDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pairingRequestDTO, or with status {@code 400 (Bad Request)} if the pairingRequest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pairing-requests")
    public ResponseEntity<PairingRequestDTO> createPairingRequest(@RequestBody PairingRequestDTO pairingRequestDTO)
        throws URISyntaxException {
        log.debug("REST request to save PairingRequest : {}", pairingRequestDTO);
        if (pairingRequestDTO.getId() != null) {
            throw new BadRequestAlertException("A new pairingRequest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PairingRequestDTO result = pairingRequestService.save(pairingRequestDTO);
        return ResponseEntity
            .created(new URI("/api/pairing-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pairing-requests/:id} : Updates an existing pairingRequest.
     *
     * @param id the id of the pairingRequestDTO to save.
     * @param pairingRequestDTO the pairingRequestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pairingRequestDTO,
     * or with status {@code 400 (Bad Request)} if the pairingRequestDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pairingRequestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pairing-requests/{id}")
    public ResponseEntity<PairingRequestDTO> updatePairingRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PairingRequestDTO pairingRequestDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PairingRequest : {}, {}", id, pairingRequestDTO);
        if (pairingRequestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pairingRequestDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pairingRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PairingRequestDTO result = pairingRequestService.save(pairingRequestDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pairingRequestDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /pairing-requests/:id} : Partial updates given fields of an existing pairingRequest, field will ignore if it is null
     *
     * @param id the id of the pairingRequestDTO to save.
     * @param pairingRequestDTO the pairingRequestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pairingRequestDTO,
     * or with status {@code 400 (Bad Request)} if the pairingRequestDTO is not valid,
     * or with status {@code 404 (Not Found)} if the pairingRequestDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the pairingRequestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/pairing-requests/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PairingRequestDTO> partialUpdatePairingRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PairingRequestDTO pairingRequestDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PairingRequest partially : {}, {}", id, pairingRequestDTO);
        if (pairingRequestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pairingRequestDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pairingRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PairingRequestDTO> result = pairingRequestService.partialUpdate(pairingRequestDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pairingRequestDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /pairing-requests} : get all the pairingRequests.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pairingRequests in body.
     */
    @GetMapping("/pairing-requests")
    public List<PairingRequestDTO> getAllPairingRequests() {
        log.debug("REST request to get all PairingRequests");
        return pairingRequestService.findAll();
    }

    /**
     * {@code GET  /pairing-requests/:id} : get the "id" pairingRequest.
     *
     * @param id the id of the pairingRequestDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pairingRequestDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pairing-requests/{id}")
    public ResponseEntity<PairingRequestDTO> getPairingRequest(@PathVariable Long id) {
        log.debug("REST request to get PairingRequest : {}", id);
        Optional<PairingRequestDTO> pairingRequestDTO = pairingRequestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pairingRequestDTO);
    }

    /**
     * {@code DELETE  /pairing-requests/:id} : delete the "id" pairingRequest.
     *
     * @param id the id of the pairingRequestDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pairing-requests/{id}")
    public ResponseEntity<Void> deletePairingRequest(@PathVariable Long id) {
        log.debug("REST request to delete PairingRequest : {}", id);
        pairingRequestService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
