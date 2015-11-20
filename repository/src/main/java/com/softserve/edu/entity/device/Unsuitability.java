package com.softserve.edu.entity.device;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by Sonka on 20.11.2015.
 */
@Entity
@Table(name = "REASONS_UNSUITABILITY _COUNTER")
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class Unsuitability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "unsuitability")
    private Set<CounterType> counterTypeSet;

    public Unsuitability(String name){
        this.name = name;
    }
}
