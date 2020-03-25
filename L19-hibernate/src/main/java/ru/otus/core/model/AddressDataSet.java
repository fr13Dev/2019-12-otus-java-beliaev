package ru.otus.core.model;

import javax.persistence.*;

@Entity
@Table(name = "address")
public class AddressDataSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "street", nullable = false)
    private String street;
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "address")
    private User owner;

    public AddressDataSet(String street) {
        this.street = street;
    }

    public AddressDataSet() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AddressDataSet that = (AddressDataSet) o;

        return street.equals(that.street);
    }

    @Override
    public int hashCode() {
        return street.hashCode();
    }

    @Override
    public String toString() {
        return "AddressDataSet{" +
                "street='" + street + '\'' +
                '}';
    }
}
