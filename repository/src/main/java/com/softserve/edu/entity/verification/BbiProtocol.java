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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verificationId")
    private Verification verification;
    
    public BbiProtocol(String fileName, Verification verification) {
        this.fileName = fileName;
        this.verification = verification;
    }
}