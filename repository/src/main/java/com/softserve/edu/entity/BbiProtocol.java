package com.softserve.edu.entity;

import javax.persistence.*;
import java.sql.Blob;

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

    public BbiProtocol(byte[] bbi) {
        this.bbi = bbi;
    }
}