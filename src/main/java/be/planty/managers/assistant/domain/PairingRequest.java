package be.planty.managers.assistant.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A PairingRequest.
 */
@Entity
@Table(name = "pairing_request")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PairingRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "verification_code")
    private String verificationCode;

    @Column(name = "request_time")
    private ZonedDateTime requestTime;

    @Column(name = "accepted")
    private Boolean accepted;

    @Column(name = "session_id")
    private String sessionId;

    @Column(name = "public_key")
    private String publicKey;

    @Column(name = "login")
    private String login;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public PairingRequest name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public PairingRequest verificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
        return this;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public ZonedDateTime getRequestTime() {
        return requestTime;
    }

    public PairingRequest requestTime(ZonedDateTime requestTime) {
        this.requestTime = requestTime;
        return this;
    }

    public void setRequestTime(ZonedDateTime requestTime) {
        this.requestTime = requestTime;
    }

    public Boolean isAccepted() {
        return accepted;
    }

    public PairingRequest accepted(Boolean accepted) {
        this.accepted = accepted;
        return this;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    public String getSessionId() {
        return sessionId;
    }

    public PairingRequest sessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public PairingRequest publicKey(String publicKey) {
        this.publicKey = publicKey;
        return this;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getLogin() {
        return login;
    }

    public PairingRequest login(String login) {
        this.login = login;
        return this;
    }

    public void setLogin(String login) {
        this.login = login;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PairingRequest pairingRequest = (PairingRequest) o;
        if (pairingRequest.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pairingRequest.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PairingRequest{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", verificationCode='" + getVerificationCode() + "'" +
            ", requestTime='" + getRequestTime() + "'" +
            ", accepted='" + isAccepted() + "'" +
            ", sessionId='" + getSessionId() + "'" +
            ", publicKey='" + getPublicKey() + "'" +
            ", login='" + getLogin() + "'" +
            "}";
    }
}
