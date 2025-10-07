package domain;

public record PhoneNumber(String value) {

    public PhoneNumber {
        if (value == null) throw new IllegalArgumentException("El número no puede ser nulo.");
        String trimmed = value.trim();
        if (trimmed.isEmpty()) throw new IllegalArgumentException("El número no puede estar vacío.");

        // Normaliza: conserva '+' inicial, elimina espacios y guiones; solo dígitos
        StringBuilder sb = new StringBuilder();
        int i = 0;
        if (trimmed.charAt(0) == '+') { sb.append('+'); i = 1; }
        for (; i < trimmed.length(); i++) {
            char c = trimmed.charAt(i);
            if (c == ' ' || c == '-') continue;
            if (!Character.isDigit(c)) {
                throw new IllegalArgumentException("Número inválido: caracteres no permitidos.");
            }
            sb.append(c);
        }
        String normalized = sb.toString();

        // Validación: + opcional y 7–15 dígitos
        if (!normalized.matches("^\\+?\\d{7,15}$")) {
            throw new IllegalArgumentException("Número inválido. Debe tener 7 a 15 dígitos (con + opcional).");
        }

        value = normalized;
    }

    public static PhoneNumber of(String raw) {
        return new PhoneNumber(raw);
    }

    @Override public String toString() {
        return value;
    }
}