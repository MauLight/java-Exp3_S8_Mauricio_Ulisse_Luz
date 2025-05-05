/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package exp3_s8_mauricio_ulisse_luz;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Asiento {

    private static int count = 1;

    int id;
    String row;
    int number;
    int precioSinDescuento;
    double precioConDescuento;
    boolean isOccupied;
    boolean isReserved;
    int purchaseId;
    int clientId;
    int discountType;

    public Asiento(String row, int number, int precio) {
        this.id = count++;
        this.row = row;
        this.number = number;
        this.isOccupied = false;
        this.isReserved = false;
        this.purchaseId = -1;
        this.clientId = -1;
        this.discountType = -1;
        this.precioConDescuento = -1;
        this.precioSinDescuento = precio;
    }

    @Override
    public String toString() {
        return String.format("[%s%02d]", row, number);
    }
}

class Reservacion {

    private static int count = 1;

    int id;
    String row;
    int number;
    int clientId;

    public Reservacion(String row, int number, int clientId) {
        this.id = count++;
        this.row = row;
        this.number = number;
        this.clientId = clientId;
    }

    @Override
    public String toString() {
        return String.format("[%s%02d]", row, number);
    }
}

public class Exp3_S8_Mauricio_Ulisse_Luz {

    static List<Map<String, double[]>> entradasCurrUsuario = new ArrayList<>();

    static int eleccionMenu = -1;
    static double total = 0;

    static int currentClientId = 1;
    static int currentPurchaseId = 1;

    static String titulo = "Teatro Moro";
    static String despedida = "Gracias por su visita al Teatro Moro";

    /* Metodos estáticos */
    public static void imprimirTituloMenu(String titulo) {
        System.out.println();
        System.out.println("######## Menu " + titulo + " #########");
        System.out.println();
    }

    public static void imprimirMenu(String[] listaDeOpciones) {
        for (int i = 0; i < listaDeOpciones.length; i++) {
            System.out.println("[" + (i + 1) + "] " + listaDeOpciones[i]);
        }
    }

    static void imprimirAsientos(Asiento[] asientos) {
        for (int i = 0; i < asientos.length; i++) {
            if (i % 12 == 0) {
                System.out.println();
            }
            System.out.print(asientos[i]);
        }
    }

    public static int getAsientoIndex(String asiento) {
        String numberPart = asiento.substring(1);
        int seatNumber = Integer.parseInt(numberPart);
        return seatNumber - 1;
    }

    public static String formatAsientoLabel(String input) {
        if (input == null || input.length() < 2) {
            return "[INVALID]";
        }

        // Extract the row letter and seat number
        char row = Character.toUpperCase(input.charAt(0));
        String numberPart = input.substring(1);

        try {
            int seatNumber = Integer.parseInt(numberPart);
            return String.format("[%c%02d]", row, seatNumber);
        } catch (NumberFormatException e) {
            return "[INVALID]";
        }
    }

    public static void imprimirMensaje(String mensaje) {
        System.out.println();
        System.out.println(mensaje);
        System.out.println();
    }

    public static int validarConfirmacion(Scanner scanner, String mensaje, int[] opcionesValidas) {
        String input = scanner.nextLine();
        try {
            int opcionConfirmar = Integer.parseInt(input);
            for (int opcion : opcionesValidas) {
                if (opcionConfirmar == opcion) {
                    return opcionConfirmar;
                }
            }
            imprimirMensaje(mensaje);
            return -1;
        } catch (NumberFormatException e) {
            imprimirMensaje(mensaje);
            return -1;
        }
    }

    public static boolean checkIfSeatExists(List<Map<String, double[]>> entradas, String asientoElegido) {
        for (Map<String, double[]> entrada : entradas) {
            for (String key : entrada.keySet()) {
                if (key.equalsIgnoreCase(asientoElegido)) {
                    System.out.println("\n**Este asiento ya fue ingresado**\n");
                    return false;
                }
            }
        }
        return true;
    }

    static void mostrarResumenDePago(Asiento[] asientos, int clientId) {

        for (int i = 0; i < despedida.length(); i++) {
            System.out.print("_");
        }
        System.out.println();
        System.out.println();
        for (int i = 0; i < ((despedida.length() / 2) - (titulo.length() / 2)) - 1; i++) {
            System.out.print(" ");
        }
        System.out.print(titulo);
        for (int i = 0; i < ((despedida.length() / 2) - (titulo.length() / 2)) - 1; i++) {
            System.out.print(" ");
        }
        System.out.println();
        for (int i = 0; i < despedida.length(); i++) {
            System.out.print("_");
        }

        System.out.println();
        mostrarDetallesEntrada(asientos, clientId, true);

        for (Asiento asiento : asientos) {
            if (asiento.isOccupied) {
                total += asiento.precioConDescuento;
            }
        }

        for (int i = 0; i < despedida.length(); i++) {
            System.out.print("_");
        }
        System.out.println();
        System.out.println();
        System.out.println("Total: " + total);
        System.out.println();

        for (int i = 0; i < despedida.length(); i++) {
            System.out.print("_");
        }
        System.out.println();
        System.out.println();
        System.out.println(despedida);
        for (int i = 0; i < despedida.length(); i++) {
            System.out.print("_");
        }
        System.out.println();
    }

    static void mostrarDetallesEntrada(Asiento[] asientos, int clientId, boolean buy) {
        int count = 1;
        for (Asiento asiento : asientos) {
            if (asiento.clientId == clientId) {
                String descuento = switch (asiento.discountType) {
                    case 1 ->
                        "Estudiante (10%)";
                    case 2 ->
                        "Adulto mayor (15%)";
                    default ->
                        "No aplica";
                };

                System.out.println("\nEntrada " + count++);
                System.out.println("Ubicación del asiento: " + asiento.row + asiento.number);
                System.out.println("Precio base: " + asiento.precioSinDescuento);
                System.out.println("Descuento: " + descuento);
                System.out.println("Precio final: " + asiento.precioConDescuento);
                if (!buy) {
                    System.out.println("** Entrada reservada con éxito. **");
                }
                System.out.println();
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        int newClientId = currentClientId++;
        int newPurchaseId = currentPurchaseId++;
        boolean puedePagar = false;
        boolean tieneReserva = false;

        // Definir la lista de precios.
        Map<String, Integer> precios = Map.of(
                "A", 20000,
                "B", 12000,
                "C", 9000);

        // Definir lista de descuentos
        ArrayList<Double> descuentos = new ArrayList<>();
        descuentos.add(0.10);
        descuentos.add(0.15);
        descuentos.add(0.00);

        // Crear el array de asientos usando Seat class.
        Asiento[] asientos = new Asiento[60];
        for (int i = 0; i < asientos.length; i++) {
            String row;
            if (i < 3 || (i > 8 && i < 15) || (i > 20 && i < 36)) {
                row = "B";
            } else if (i > 35) {
                row = "C";
            } else {
                row = "A";
            }

            asientos[i] = new Asiento(row, i + 1, precios.get(row));
        }

        // Crear la ArayList de reservaciones
        List<Reservacion> reservaciones = new ArrayList<>();

        String regex = "^(?i:[abc])(?:[1-9]|[1-5][0-9]|60)$";
        Pattern pattern = Pattern.compile(regex);
        Scanner scanner = new Scanner(System.in);
        String[] opcionesMenuInicial = { "Comprar entrada", "Reservar entrada", "Pagar", "Salir" };
        String[] opcionesMenuDescuentos = { "Estudiante", "Tercera edad", "No aplica" };
        int[] opciones = java.util.stream.IntStream.rangeClosed(1, opcionesMenuInicial.length).toArray();

        String asientoElegido;
        boolean entradaValida;

        do {
            imprimirTituloMenu("Principal");
            imprimirMenu(opcionesMenuInicial);
            imprimirMensaje("Seleccione el número de la opción que desea realizar:");
            do {
                eleccionMenu = validarConfirmacion(scanner, "** Ingrese un número válido (1-4) **", opciones);
            } while (eleccionMenu < 0);

            switch (eleccionMenu) {
                case 1 -> {
                    entradaValida = false;
                    opciones = java.util.stream.IntStream.rangeClosed(1, opcionesMenuDescuentos.length).toArray();

                    imprimirTituloMenu("Comprar entradas");

                    do {
                        imprimirAsientos(asientos);
                        System.out.println("\n");

                        imprimirMensaje("Por favor escoja el número de asiento:");
                        asientoElegido = scanner.nextLine();

                        // Chequear asiento disponible.
                        Matcher matcher = pattern.matcher(asientoElegido);
                        if (matcher.matches()) {
                            for (Asiento asiento : asientos) {
                                if (asiento.toString().equalsIgnoreCase(formatAsientoLabel(asientoElegido))
                                        && !asiento.isOccupied && !asiento.isReserved) {
                                    entradaValida = true;
                                    break;
                                } else if (asiento.toString().equalsIgnoreCase(formatAsientoLabel(asientoElegido))
                                        && !asiento.isOccupied && asiento.isReserved) {
                                    imprimirMensaje("**La entrada ingresada está reservada.**\n");
                                    entradaValida = false;
                                } else if (asiento.toString().equalsIgnoreCase(formatAsientoLabel(asientoElegido))
                                        && asiento.isOccupied) {
                                    imprimirMensaje("**La entrada ingresada no está disponible.**\n");
                                    entradaValida = false;
                                } else {
                                    entradaValida = false;
                                }
                            }
                        } else {
                            imprimirMensaje("**La entrada ingresada no existe**\n");
                            entradaValida = false;
                        }
                    } while (!entradaValida);

                    System.out.println();
                    imprimirMenu(opcionesMenuDescuentos);
                    imprimirMensaje("Seleccione el número del descuento que desea aplicar:");

                    do {
                        eleccionMenu = validarConfirmacion(scanner, "** Ingrese un número válido (1-3) **", opciones);
                    } while (eleccionMenu < 0);

                    int index = getAsientoIndex(asientoElegido);
                    int precio = asientos[index].precioSinDescuento;

                    asientos[index].clientId = newClientId;
                    asientos[index].purchaseId = newPurchaseId;
                    asientos[index].isOccupied = true;

                    int indexDescuento = (eleccionMenu >= 1 && eleccionMenu <= 2) ? eleccionMenu - 1 : 2;
                    double descuento = descuentos.get(indexDescuento);

                    asientos[index].precioConDescuento = precio - (precio * descuento);
                    asientos[index].discountType = eleccionMenu;

                    mostrarDetallesEntrada(asientos, newClientId, true);
                    puedePagar = true;

                    opciones = java.util.stream.IntStream.rangeClosed(1, opcionesMenuInicial.length).toArray();

                }
                case 2 -> {
                    entradaValida = false;
                    imprimirTituloMenu("Resumen compra");

                    do {
                        imprimirAsientos(asientos);
                        System.out.println("\n");

                        imprimirMensaje("Por favor escoja el número de asiento:");
                        asientoElegido = scanner.nextLine();

                        // Chequear asiento disponible.
                        Matcher matcher = pattern.matcher(asientoElegido);
                        if (matcher.matches()) {
                            for (Asiento asiento : asientos) {
                                if (asiento.toString().equalsIgnoreCase(formatAsientoLabel(asientoElegido))
                                        && !asiento.isOccupied && !asiento.isReserved) {
                                    entradaValida = true;
                                    break;
                                } else if (asiento.toString().equalsIgnoreCase(formatAsientoLabel(asientoElegido))
                                        && !asiento.isOccupied && asiento.isReserved) {
                                    imprimirMensaje("**La entrada ingresada está reservada.**\n");
                                    entradaValida = false;
                                } else if (asiento.toString().equalsIgnoreCase(formatAsientoLabel(asientoElegido))
                                        && asiento.isOccupied) {
                                    imprimirMensaje("**La entrada ingresada no está disponible.**\n");
                                    entradaValida = false;
                                } else {
                                    entradaValida = false;
                                }
                            }
                        } else {
                            imprimirMensaje("**La entrada ingresada no existe**\n");
                            entradaValida = false;
                        }
                    } while (!entradaValida);

                    int index = getAsientoIndex(asientoElegido);
                    asientos[index].isReserved = true;
                    reservaciones.add(new Reservacion(asientos[index].row, asientos[index].number, newClientId));
                    tieneReserva = true;

                    for (Reservacion r : reservaciones) {
                        System.out.println("Asiento " + r + " reservado con exito.");
                    }

                }
                case 3 -> {
                    imprimirTituloMenu("Pagar");
                    eleccionMenu = -1;

                    if (tieneReserva) {
                        String[] opcionesMenuReserva = { "Si", "No" };
                        opciones = java.util.stream.IntStream.rangeClosed(1, opcionesMenuReserva.length).toArray();
                        for (Reservacion r : reservaciones) {
                            System.out.println("Asiento " + r + " ha sido reservado.");
                        }

                        System.out.println();
                        imprimirMenu(opcionesMenuReserva);
                        imprimirMensaje("Desea comprar las entradas reservadas?");

                        do {
                            eleccionMenu = validarConfirmacion(scanner, "** Ingrese un número válido (1-2) **",
                                    opciones);
                        } while (eleccionMenu < 0);

                        if (eleccionMenu == 1) {
                            opciones = java.util.stream.IntStream.rangeClosed(1, opcionesMenuDescuentos.length)
                                    .toArray();
                            for (Asiento asiento : asientos) {
                                if (asiento.isReserved) {

                                    imprimirMensaje("Asiento " + asiento.toString());
                                    System.out.println();
                                    imprimirMenu(opcionesMenuDescuentos);
                                    imprimirMensaje("Seleccione el número del descuento que desea aplicar:");

                                    do {
                                        eleccionMenu = validarConfirmacion(scanner,
                                                "** Ingrese un número válido (1-3) **", opciones);
                                    } while (eleccionMenu < 0);

                                    asiento.clientId = newClientId;
                                    asiento.purchaseId = newPurchaseId;
                                    asiento.isOccupied = true;
                                    asiento.isReserved = false;

                                    int indexDescuento = (eleccionMenu >= 1 && eleccionMenu <= 2) ? eleccionMenu - 1
                                            : 2;
                                    double descuento = descuentos.get(indexDescuento);

                                    asiento.precioConDescuento = asiento.precioSinDescuento
                                            - (asiento.precioSinDescuento * descuento);
                                    asiento.discountType = eleccionMenu;

                                    Iterator<Reservacion> iterator = reservaciones.iterator();
                                    while (iterator.hasNext()) {
                                        Reservacion r = iterator.next();
                                        if (r.clientId == asiento.clientId) {
                                            iterator.remove();
                                        }
                                    }

                                }
                            }
                            puedePagar = true;
                            mostrarDetallesEntrada(asientos, newClientId, true);

                        }
                    }

                    if (puedePagar) {

                        mostrarResumenDePago(asientos, newClientId);
                        eleccionMenu = 4;

                    } else {
                        imprimirMensaje("** Debe ingresar entradas antes de pagar. **");
                    }
                }
            }
        } while (eleccionMenu != 4);

        imprimirMensaje("Gracias por su compra");
    }

}
