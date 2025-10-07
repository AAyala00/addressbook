# AddressBook (Java)

Agenda telefónica por consola con persistencia en CSV. Minimalista, robusta y lista para crecer.

## Características
- CRUD de contactos por consola (`list`, `create`, `delete`, `save`, `load`).
- Persistencia en `data/addressbook.csv` (creación automática).
- Value Objects: `Name` (limites 2–100, normalización) y `PhoneNumber` (`+` opcional, 7–15 dígitos, normalización).
- Entidad: `Contact` (identidad por número).
- Repositorio CSV con orden estable y guardado atómico (tmp → move).
- Servicio de aplicación: `AddressBookService`.
- Inyección de dependencias por constructor (DIP).

## Estructura del proyecto
```
/addressbook
├─ src/
│  ├─ Main.java
│  ├─ application/
│  │  └─ AddressBookService.java
│  ├─ domain/
│  │  ├─ Name.java
│  │  ├─ PhoneNumber.java
│  │  └─ Contact.java
│  └─ infrastructure/
│     └─ CsvContactRepository.java
├─ data/
│  └─ addressbook.csv        (se crea en runtime si no existe)
├─ .gitignore
└─ README.md
```

## Requisitos
- JDK 17+ (o 21 LTS).
- IntelliJ IDEA (recomendado) o cualquier IDE/terminal.

## Ejecución

### IntelliJ IDEA
1. Importa el proyecto.
2. **Run/Debug Configurations** → establece **Working Directory** en `$ProjectFileDir$`.
3. Ejecuta `Main`.

### Terminal
```bash
javac -d out src/**/*.java
java -cp out Main
```

## Uso (CLI)
```
=== Agenda Telefónica ===
1) list   - Listar contactos
2) create - Crear/actualizar contacto
3) delete - Eliminar contacto
4) save   - Guardar
5) load   - Recargar
0) exit   - Salir
```

## Formato CSV
Cada línea:
```
{Número},{Nombre}
```
> Nota: CSV simple (sin comillas). Se recomienda evitar comas en el nombre.