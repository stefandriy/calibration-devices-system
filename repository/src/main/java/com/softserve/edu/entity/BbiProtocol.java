package com.softserve.edu.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

/**
 * Created by MAX on 25.07.2015.
 */

@Entity
@Table(name = "BbiProtocol")
public class BbiProtocol {
    @Id
    @GeneratedValue
    private Long id;

    @Lob
    @Column(length = 500000)
    private byte[] bbi;

    @ManyToOne
    private Verification verification;

    BbiProtocol() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getBbi() {
        return bbi;
    }

    public void setBbi(byte[] bbi) {
        this.bbi = bbi;
    }

    public Verification getVerification() {
        return verification;
    }

    public void setVerification(Verification verification) {
        this.verification = verification;
    }

    public BbiProtocol(byte[] bbi, Verification verification) {
        this.bbi = bbi;
        this.verification = verification;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("bbi", bbi).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
                .append(bbi)
                .toHashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof BbiProtocol) {
            final BbiProtocol other = (BbiProtocol) obj;
            return new EqualsBuilder()
                    .append(id, other.id)
                    .append(bbi, other.bbi)
                    .isEquals();
        } else {
            return false;
        }
    }
}