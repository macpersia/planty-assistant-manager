package be.planty.managers.assistant.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the PairingRequest entity.
 */
public class PairingRequestDTO implements Serializable {

    private String id;

    private String name;

    private String verificationCode;

    private ZonedDateTime requestTime;

    private Boolean accepted;

    private String publicKey;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
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
            ", publicKey='" + getPublicKey() + "'" +
            "}";
    }
}
