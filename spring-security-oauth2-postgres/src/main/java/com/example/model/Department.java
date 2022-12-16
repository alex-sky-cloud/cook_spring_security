package com.example.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "DEPARTMENT")
@Getter
@Setter
public class Department implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", updatable = false, nullable = false)
    private Long id = null;

    @Column(name = "NAME", nullable = false)
    private String name;

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "department",
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    @JsonManagedReference
    private Set<Employee> employees;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "department",
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    @JsonManagedReference
    private Set<Office> offices;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private Company company;

/*    public void setEmployees(Set<Employee> employees) {
        this.employees.clear();
        if (employees != null) {
            this.employees.addAll(employees);
        }
    }

    public void setOffices(Set<Office> offices) {
        this.offices.clear();
        if (offices != null) {
            this.offices.addAll(offices);
        }
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Department that = (Department) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
