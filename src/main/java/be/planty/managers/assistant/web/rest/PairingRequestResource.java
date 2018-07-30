package be.planty.managers.assistant.web.rest;

import com.codahale.metrics.annotation.Timed;
import be.planty.managers.assistant.service.PairingRequestService;
import be.planty.managers.assistant.web.rest.errors.BadRequestAlertException;
import be.planty.managers.assistant.web.rest.util.HeaderUtil;
import be.planty.managers.assistant.service.dto.PairingRequestDTO;
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
 * REST controller for managing PairingRequest.
 */
@RestController
@RequestMapping("/api")
public class PairingRequestResource {

    private final Logger log = LoggerFactory.getLogger(PairingRequestResource.class);

    private static final String ENTITY_NAME = "pairingRequest";

    private final PairingRequestService pairingRequestService;

    public PairingRequestResource(PairingRequestService pairingRequestService) {
        this.pairingRequestService = pairingRequestService;
    }

    /**
     * POST  /pairing-requests : Create a new pairingRequest.
     *
     * @param pairingRequestDTO the pairingRequestDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pairingRequestDTO, or with status 400 (Bad Request) if the pairingRequest has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pairing-requests")
    @Timed
    public ResponseEntity<PairingRequestDTO> createPairingRequest(@RequestBody PairingRequestDTO pairingRequestDTO) throws URISyntaxException {
        log.debug("REST request to save PairingRequest : {}", pairingRequestDTO);
        if (pairingRequestDTO.getId() != null) {
            throw new BadRequestAlertException("A new pairingRequest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PairingRequestDTO result = pairingRequestService.save(pairingRequestDTO);
        return ResponseEntity.created(new URI("/api/pairing-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pairing-requests : Updates an existing pairingRequest.
     *
     * @param pairingRequestDTO the pairingRequestDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pairingRequestDTO,
     * or with status 400 (Bad Request) if the pairingRequestDTO is not valid,
     * or with status 500 (Internal Server Error) if the pairingRequestDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pairing-requests")
    @Timed
    public ResponseEntity<PairingRequestDTO> updatePairingRequest(@RequestBody PairingRequestDTO pairingRequestDTO) throws URISyntaxException {
        log.debug("REST request to update PairingRequest : {}", pairingRequestDTO);
        if (pairingRequestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PairingRequestDTO result = pairingRequestService.save(pairingRequestDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pairingRequestDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pairing-requests : get all the pairingRequests.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of pairingRequests in body
     */
    @GetMapping("/pairing-requests")
    @Timed
    public List<PairingRequestDTO> getAllPairingRequests() {
        log.debug("REST request to get all PairingRequests");
        return pairingRequestService.findAll();
    }

    /**
     * GET  /pairing-requests/:id : get the "id" pairingRequest.
     *
     * @param id the id of the pairingRequestDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pairingRequestDTO, or with status 404 (Not Found)
     */
    @GetMapping("/pairing-requests/{id}")
    @Timed
    public ResponseEntity<PairingRequestDTO> getPairingRequest(@PathVariable String id) {
        log.debug("REST request to get PairingRequest : {}", id);
        Optional<PairingRequestDTO> pairingRequestDTO = pairingRequestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pairingRequestDTO);
    }

    /**
     * DELETE  /pairing-requests/:id : delete the "id" pairingRequest.
     *
     * @param id the id of the pairingRequestDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pairing-requests/{id}")
    @Timed
    public ResponseEntity<Void> deletePairingRequest(@PathVariable String id) {
        log.debug("REST request to delete PairingRequest : {}", id);
        pairingRequestService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
