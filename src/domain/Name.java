package domain;

import java.text.Normalizer;

// objeto inmutable
public record Name(String value) {
    // limites
    private static final int MIN_LEN = 2;
    private static final int MAX_LEN = 100;

    // Constructor con validacion y normalizacione
    public Name {
        if (value == null) throw new IllegalArgumentException("El nombre no puede ser nulo.");
        String v = normalize(value);
        validateLength(v); // validacion minima
        value = v; // guardar normalizado
    }

    public static Name of(String raw) { return new Name(raw); }

    private static String normalize(String raw) {
        String v = raw.trim();
        // colapsar espacios multiples a uno
        v = v.replaceAll("\\s+", " ");
        // Normaliza Unicode
        v = Normalizer.normalize(v, Normalizer.Form.NFC);
        // Quita caracteres de control
        v = v.chars()
                .filter(c -> !Character.isISOControl(c))
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return v;
    }

    private static void validateLength(String v) {
        int len = v.codePointCount(0, v.length());
        if (len < MIN_LEN || len > MAX_LEN) {
            throw new IllegalArgumentException("El nombre debe tener entre " + MIN_LEN + " y " + MAX_LEN + " caracteres.");
        }
    }
}