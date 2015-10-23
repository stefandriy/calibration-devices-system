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
    @Setter(AccessLevel.PRIVATE)
    private String fileName;

    private String filePath;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verificationId")
    private Verification verification;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Verification getVerification() {
        return verification;
    }

    public void setVerification(Verification verification) {
        this.verification = verification;
    }

    public BbiProtocol(String fileName, String filePath, Verification verification) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.verification = verification;
    }
}