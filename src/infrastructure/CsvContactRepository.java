package infrastructure;

import domain.Contact;
import domain.ContactRepository;
import domain.Name;
import domain.PhoneNumber;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class CsvContactRepository implements ContactRepository {
    private final Path filePath;
    private final Map<PhoneNumber, Contact> data = new HashMap<>();

    public CsvContactRepository(Path filePath) { this.filePath = filePath; }

    @Override public void load() throws IOException {
        data.clear();
        ensureFileExists();
        try (BufferedReader br = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                int comma = line.indexOf(',');
                if (comma <= 0 || comma == line.length() - 1) continue;
                String number = line.substring(0, comma).trim();
                String name = line.substring(comma + 1).trim();
                try {
                    PhoneNumber phone = PhoneNumber.of(number);
                    Name pname = Name.of(name);
                    data.put(phone, new Contact(phone, pname));
                } catch (IllegalArgumentException ex) {
                    // Ignorar, normalmente aqui podria hacer un logger, pero vamos a ignorar
                }
            }
        }
    }

    @Override public void save() throws IOException {
        ensureFileExists();
        try (BufferedWriter bw = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
            for (Contact c : data.values()) {
                // se almacena en el formato que espera la actividad phone,name
                // a no ser que sea {phone},{name}, supongo que es la primera
                bw.write(c.phone().value() + "," + c.name().value());
                bw.newLine();
            }
        }
    }

    // muestra la lista de una forma readonly
    @Override public Collection<Contact> list() {
        return Collections.unmodifiableCollection(data.values());
    }

    // un metodo para buscar por numero, esto seria algo extra, pero no creo que lo vaya a implementar
    @Override public Optional<Contact> findByPhone(PhoneNumber phone) {
        return Optional.ofNullable(data.get(phone));
    }

    @Override public boolean upsert(Contact contact) {
        boolean isNew = !data.containsKey(contact.phone());
        data.put(contact.phone(), contact);
        return isNew;
    }

    @Override public boolean delete(PhoneNumber phone) {
        return data.remove(phone) != null;
    }

    private void ensureFileExists() throws IOException {
        if (Files.notExists(filePath)) {
            if (filePath.getParent() != null && Files.notExists(filePath.getParent())) {
                Files.createDirectories(filePath.getParent());
            }
            Files.createFile(filePath);
        }
    }
}
