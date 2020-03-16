package ru.otus.core.model;

import ru.otus.core.sql.Id;

public class Account {
    @Id
    private final long id;
    private final String type;
    private final int rest;

    public Account(long id, String type, int rest) {
        this.id = id;
        this.type = type;
        this.rest = rest;
    }

    public long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public int getRest() {
        return rest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (id != account.id) return false;
        if (rest != account.rest) return false;
        return type.equals(account.type);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + type.hashCode();
        result = 31 * result + rest;
        return result;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", rest=" + rest +
                '}';
    }
}
