package com.softserve.edu.entity.verification;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Table(name = "BBI_PROTOCOL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BbiProtocol {

    @Id
    @GeneratedValue
    private Long id;

    @Lob
    private byte[] bbi;

    private String fileName;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verificationId")
    private Verification verification;

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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public BbiProtocol(byte[] bbi, Verification verification, String fileName) {
        this.bbi = bbi;
        this.verification = verification;
        this.fileName = fileName;
    }
}