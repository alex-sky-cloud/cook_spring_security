package com.example.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "COMPANY", uniqueConstraints = {@UniqueConstraint(columnNames = {"NAME"})})
@Getter
@Setter
public class Company implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(
            name = "ID",
            updatable = false,
            nullable = false)
    private Long id;

    @Column(
            name = "NAME",
            nullable = false)
    private String name;

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "company",
            fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonManagedReference
    private Set<Department> departments;

    /**
     * Для решения бесконечной рекурсии, при десериализации полученных
     * сущностей с ассоциациированными данными, используются @JsonManagedReference
     * и @JsonBackReference
     * Ссылка @JsonManagedReference используется на стороне OneToMany, в то время как
     * ссылка @JsonBackReference используется на стороне @ManyToOne.
     * @JsonManagedReference - это прямая часть сопоставления / ссылки,
     * и данные сериализуются нормально.
     *
     * @JsonBackReference - это обратная сторона сопоставления,
     * и данные не сериализуются
     */
    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "company",
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    @JsonManagedReference
    private Set<Car> cars;

/*    public void setDepartments(Set<Department> departments) {
        this.departments.clear();
        if (departments != null) {
            this.departments.addAll(departments);
        }
    }

    public void setCars(Set<Car> cars) {
        this.cars.clear();
        if (cars != null) {
            this.cars.addAll(cars);
        }
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Company company = (Company) o;
        return id != null && Objects.equals(id, company.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
