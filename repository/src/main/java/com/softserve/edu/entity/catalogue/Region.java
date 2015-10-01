package com.softserve.edu.entity.catalogue;

import lombok.*;

import javax.persistence.*;

import static com.softserve.edu.entity.catalogue.util.Checker.checkForEmptyText;

@Entity
@Table(name = "REGION")
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Region extends AbstractCatalogue {

    @Id
    @Setter
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String designation;

    public Region(String name) {
        this.designation = name;
    }

    private void setDesignation(String designation) {
        checkForEmptyText(designation);
        this.designation = designation;
    }
}
