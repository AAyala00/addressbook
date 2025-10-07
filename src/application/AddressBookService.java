package application;

import domain.Contact;
import domain.ContactRepository;
import domain.Name;
import domain.PhoneNumber;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AddressBookService {
    private final ContactRepository repo;

    public AddressBookService(ContactRepository repo) { this.repo = repo; }

    public void load() throws IOException { repo.load(); }
    public void save() throws IOException { repo.save(); }

    public List<String> listLines() {
        return repo.list().stream()
                .sorted(Comparator.comparing(c -> c.name().value()))
                .map(c -> c.phone().value() + " : " + c.name().value())
                .collect(Collectors.toList());
    }

    public boolean createOrUpdate(String rawNumber, String rawName) {
        PhoneNumber phone = PhoneNumber.of(rawNumber);
        Name name = Name.of(rawName);
        return repo.upsert(new Contact(phone, name));
    }

    public boolean delete(String rawNumber) {
        PhoneNumber phone = PhoneNumber.of(rawNumber);
        return repo.delete(phone);
    }
}