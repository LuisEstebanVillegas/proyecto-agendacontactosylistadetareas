import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AgendaApp {
    static Scanner scanner = new Scanner(System.in);
    static ArrayList<Contact> contacts = new ArrayList<>();
    static ArrayList<Task> tasks = new ArrayList<>();
    static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== AGENDA DE CONTACTOS Y TAREAS ===");
            System.out.println("1. Gestionar Contactos");
            System.out.println("2. Gestionar Tareas");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    manageContacts();
                    break;
                case "2":
                    manageTasks();
                    break;
                case "3":
                    System.out.println("¡Gracias por usar la agenda! Hasta luego.");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opción inválida. Intente nuevamente.");
            }
        }
    }

    // Contact methods
    static void manageContacts() {
        while (true) {
            System.out.println("\n--- Gestión de Contactos ---");
            System.out.println("1. Ver Contactos");
            System.out.println("2. Agregar Contacto");
            System.out.println("3. Modificar Contacto");
            System.out.println("4. Eliminar Contacto");
            System.out.println("5. Volver al menú principal");
            System.out.print("Seleccione una opción: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    viewContacts();
                    break;
                case "2":
                    addContact();
                    break;
                case "3":
                    updateContact();
                    break;
                case "4":
                    deleteContact();
                    break;
                case "5":
                    return;
                default:
                    System.out.println("Opción inválida. Intente nuevamente.");
            }
        }
    }

    static void viewContacts() {
        System.out.println("\nLista de Contactos:");
        if (contacts.isEmpty()) {
            System.out.println("No hay contactos registrados.");
            return;
        }
        for (int i = 0; i < contacts.size(); i++) {
            System.out.println((i + 1) + ". " + contacts.get(i));
        }
    }

    static void addContact() {
        System.out.print("Nombre: ");
        String name = scanner.nextLine().trim();
        System.out.print("Teléfono: ");
        String phone = scanner.nextLine().trim();
        System.out.print("Correo electrónico: ");
        String email = scanner.nextLine().trim();

        contacts.add(new Contact(name, phone, email));
        System.out.println("Contacto agregado con éxito.");
    }

    static void updateContact() {
        if (contacts.isEmpty()) {
            System.out.println("No hay contactos para modificar.");
            return;
        }
        viewContacts();
        System.out.print("Ingrese el número del contacto a modificar: ");
        int index = readIndex(contacts.size());
        if (index == -1) return;

        Contact contact = contacts.get(index);

        System.out.print("Nuevo nombre (enter para mantener \"" + contact.name + "\"): ");
        String name = scanner.nextLine().trim();
        if (!name.isEmpty()) contact.name = name;

        System.out.print("Nuevo teléfono (enter para mantener \"" + contact.phone + "\"): ");
        String phone = scanner.nextLine().trim();
        if (!phone.isEmpty()) contact.phone = phone;

        System.out.print("Nuevo correo electrónico (enter para mantener \"" + contact.email + "\"): ");
        String email = scanner.nextLine().trim();
        if (!email.isEmpty()) contact.email = email;

        System.out.println("Contacto modificado con éxito.");
    }

    static void deleteContact() {
        if (contacts.isEmpty()) {
            System.out.println("No hay contactos para eliminar.");
            return;
        }
        viewContacts();
        System.out.print("Ingrese el número del contacto a eliminar: ");
        int index = readIndex(contacts.size());
        if (index == -1) return;

        contacts.remove(index);
        System.out.println("Contacto eliminado con éxito.");
    }

    // Task methods
    static void manageTasks() {
        while (true) {
            System.out.println("\n--- Gestión de Tareas ---");
            System.out.println("1. Ver Tareas");
            System.out.println("2. Agregar Tarea");
            System.out.println("3. Modificar Tarea");
            System.out.println("4. Eliminar Tarea");
            System.out.println("5. Marcar Tarea como Completada");
            System.out.println("6. Volver al menú principal");
            System.out.print("Seleccione una opción: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    viewTasks();
                    break;
                case "2":
                    addTask();
                    break;
                case "3":
                    updateTask();
                    break;
                case "4":
                    deleteTask();
                    break;
                case "5":
                    completeTask();
                    break;
                case "6":
                    return;
                default:
                    System.out.println("Opción inválida. Intente nuevamente.");
            }
        }
    }

    static void viewTasks() {
        System.out.println("\nLista de Tareas:");
        if (tasks.isEmpty()) {
            System.out.println("No hay tareas registradas.");
            return;
        }
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
    }

    static void addTask() {
        System.out.print("Título: ");
        String title = scanner.nextLine().trim();
        System.out.print("Descripción: ");
        String description = scanner.nextLine().trim();
        LocalDate dueDate = null;
        while (true) {
            System.out.print("Fecha de vencimiento (YYYY-MM-DD): ");
            String dateInput = scanner.nextLine().trim();
            try {
                dueDate = LocalDate.parse(dateInput, dateFormatter);
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Formato de fecha inválido. Intente nuevamente.");
            }
        }
        tasks.add(new Task(title, description, dueDate));
        System.out.println("Tarea agregada con éxito.");
    }

    static void updateTask() {
        if (tasks.isEmpty()) {
            System.out.println("No hay tareas para modificar.");
            return;
        }
        viewTasks();
        System.out.print("Ingrese el número de la tarea a modificar: ");
        int index = readIndex(tasks.size());
        if (index == -1) return;

        Task task = tasks.get(index);

        System.out.print("Nuevo título (enter para mantener \"" + task.title + "\"): ");
        String title = scanner.nextLine().trim();
        if (!title.isEmpty()) task.title = title;

        System.out.print("Nueva descripción (enter para mantener): ");
        String description = scanner.nextLine().trim();
        if (!description.isEmpty()) task.description = description;

        while (true) {
            System.out.print("Nueva fecha de vencimiento (YYYY-MM-DD, enter para mantener " + task.dueDate.format(dateFormatter) + "): ");
            String dateInput = scanner.nextLine().trim();
            if (dateInput.isEmpty()) break;
            try {
                task.dueDate = LocalDate.parse(dateInput, dateFormatter);
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Formato de fecha inválido. Intente nuevamente.");
            }
        }
        System.out.println("Tarea modificada con éxito.");
    }

    static void deleteTask() {
        if (tasks.isEmpty()) {
            System.out.println("No hay tareas para eliminar.");
            return;
        }
        viewTasks();
        System.out.print("Ingrese el número de la tarea a eliminar: ");
        int index = readIndex(tasks.size());
        if (index == -1) return;

        tasks.remove(index);
        System.out.println("Tarea eliminada con éxito.");
    }

    static void completeTask() {
        if (tasks.isEmpty()) {
            System.out.println("No hay tareas para marcar como completadas.");
            return;
        }
        viewTasks();
        System.out.print("Ingrese el número de la tarea a marcar como completada: ");
        int index = readIndex(tasks.size());
        if (index == -1) return;

        Task task = tasks.get(index);
        if (task.completed) {
            System.out.println("La tarea ya está marcada como completada.");
        } else {
            task.completed = true;
            System.out.println("Tarea marcada como completada.");
        }
    }

    // Utility methods
    static int readIndex(int max) {
        try {
            int num = Integer.parseInt(scanner.nextLine());
            if (num < 1 || num > max) {
                System.out.println("Número fuera de rango.");
                return -1;
            }
            return num - 1;
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida.");
            return -1;
        }
    }

    // Classes
    static class Contact {
        String name;
        String phone;
        String email;

        Contact(String name, String phone, String email) {
            this.name = name;
            this.phone = phone;
            this.email = email;
        }

        public String toString() {
            return name + " | Tel: " + phone + " | Email: " + email;
        }
    }

    static class Task {
        String title;
        String description;
        LocalDate dueDate;
        boolean completed;

        Task(String title, String description, LocalDate dueDate) {
            this.title = title;
            this.description = description;
            this.dueDate = dueDate;
            this.completed = false;
        }

        public String toString() {
            return title + " (Vence: " + dueDate.format(dateFormatter) + ") - " +
                    (completed ? "Completada" : "Pendiente") + "\n  " + description;
        }
    }
}



// Gui - Interfaz gráfica


