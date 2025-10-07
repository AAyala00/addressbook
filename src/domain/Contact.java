package domain;

import java.util.Objects;

public final class Contact {
    private final PhoneNumber phone;
    private Name name;

    public Contact(PhoneNumber phone, Name name) {
        this.phone = Objects.requireNonNull(phone, "phone");
        this.name  = Objects.requireNonNull(name, "name");
    }

    // Factory Method
    public static Contact of(String rawNumber, String rawName) {
        return new Contact(PhoneNumber.of(rawNumber), Name.of(rawName));
    }

    // solo los getters
    public PhoneNumber phone() { return phone; }
    public Name name()         { return name; }

    // para renombrar se tiene que pasar un nuevo objeto
    public void rename(Name newName) {
        this.name = Objects.requireNonNull(newName, "newName");
    }

    // la identidad la da el numero, no el nombre
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contact)) return false;
        Contact that = (Contact) o;
        return phone.equals(that.phone);
    }

    @Override public int hashCode() { return phone.hashCode(); }

    // formato esperado para la lista de contactos
    @Override public String toString() {
        return phone.value() + " : " + name.value();
    }
}