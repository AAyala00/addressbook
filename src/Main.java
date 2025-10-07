import application.AddressBookService;
import infrastructure.CsvContactRepository;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Path DEFAULT_PATH = Path.of("data", "addressbook.csv");

    public static void main(String[] args) {
        // DI manual xD no tengo un archivo program.cs y esto es mas rapido xDD
        AddressBookService svc = new AddressBookService(new CsvContactRepository(DEFAULT_PATH));
        try { svc.load(); } catch (IOException e) { System.err.println("No se pudo cargar: " + e.getMessage()); }

        Scanner sc = new Scanner(System.in);
        boolean exit = false;
        while (!exit) {
            printMenu();
            String option = sc.nextLine().trim();
            switch (option) {
                case "1":
                case "list":
                    List<String> lines = svc.listLines();
                    System.out.println("Contactos:");
                    if (lines.isEmpty()) System.out.println("(sin contactos)");
                    else lines.forEach(System.out::println);
                    break;
                case "2":
                case "create":
                    System.out.print("Nombre: ");
                    String name = sc.nextLine().trim();
                    System.out.print("Número: ");
                    String number = sc.nextLine().trim();
                    try {
                        boolean isNew = svc.createOrUpdate(number, name);
                        svc.save();
                        System.out.println(isNew ? "Contacto creado." : "Contacto actualizado.");
                    } catch (IllegalArgumentException | IOException ex) {
                        System.out.println("Error: " + ex.getMessage());
                    }
                    break;
                case "3":
                case "delete":
                    System.out.print("Número a eliminar: ");
                    String del = sc.nextLine().trim();
                    try {
                        boolean removed = svc.delete(del);
                        if (removed) { svc.save(); System.out.println("Contacto eliminado."); }
                        else System.out.println("No se encontró el número.");
                    } catch (IllegalArgumentException | IOException ex) {
                        System.out.println("Error: " + ex.getMessage());
                    }
                    break;
                case "4":
                case "save":
                    try { svc.save(); System.out.println("Cambios guardados."); }
                    catch (IOException e) { System.out.println("No se pudo guardar: " + e.getMessage()); }
                    break;
                case "5":
                case "load":
                    try { svc.load(); System.out.println("Contactos recargados."); }
                    catch (IOException e) { System.out.println("No se pudo cargar: " + e.getMessage()); }
                    break;
                case "0":
                case "exit":
                    exit = true; break;
                default:
                    System.out.println("Opción inválida.");
            }
        }
        System.out.println("Hasta luego.");
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("=== Agenda Telefónica ===");
        System.out.println("1) list   - Listar contactos");
        System.out.println("2) create - Crear/actualizar contacto");
        System.out.println("3) delete - Eliminar contacto");
        System.out.println("4) save   - Guardar");
        System.out.println("5) load   - Recargar");
        System.out.println("0) exit   - Salir");
        System.out.print("Selecciona una opción: ");
    }
}