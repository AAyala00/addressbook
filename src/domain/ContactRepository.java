package domain;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

public interface ContactRepository {
    void load() throws IOException;
    void save() throws IOException;
    Collection<Contact> list();
    Optional<Contact> findByPhone(PhoneNumber phone);
    boolean upsert(Contact contact);   // true = nuevo, false = actualizado
    boolean delete(PhoneNumber phone);
}
