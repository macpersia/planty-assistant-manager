package be.planty.managers.assistant.service.dto;
import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the PairingRequest entity.
 */
public class PairingRequestDTO implements Serializable {

    private Long id;

    private String name;

    private String verificationCode;

    private ZonedDateTime requestTime;

    private Boolean accepted;

    private String sessionId;

    private String publicKey;

    private String login;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public ZonedDateTime getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(ZonedDateTime requestTime) {
        this.requestTime = requestTime;
    }

    public Boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PairingRequestDTO pairingRequestDTO = (PairingRequestDTO) o;
        if (pairingRequestDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pairingRequestDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PairingRequestDTO{" +
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
