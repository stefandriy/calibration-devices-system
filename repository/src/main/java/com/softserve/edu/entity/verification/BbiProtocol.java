package com.softserve.edu.entity.verification;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "fileName")
@Table(name = "BBI_PROTOCOL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BbiProtocol {

    @Id
    private String fileName;

    @Lob
    private byte[] bbi;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verificationId")
    private Verification verification;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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

    public BbiProtocol(String fileName, byte[] bbi, Verification verification) {
        this.fileName = fileName;
        this.bbi = bbi;
        this.verification = verification;
    }
}