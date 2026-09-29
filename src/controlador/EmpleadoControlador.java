package controlador;

import modelo.LaclaseEmpleadoAdministrativo;
import modelo.EmpleadoBase;
import modelo.Elrepositoriodeempleados;
import modelo.EmpleadoComercial;

import java.util.ArrayList;

public class EmpleadoControlador {

    public static final String[] TIPOS_EMPLEADO = {"Operativo", "Administrativo", "Comercial"};

    private final Elrepositoriodeempleados repositorio;
    private final ArrayList<String> historial;

    public EmpleadoControlador() {
        repositorio = new Elrepositoriodeempleados();
        historial = new ArrayList<>();
        cargarDatosDePrueba();
    }

    private void cargarDatosDePrueba() {
        String[] cedulas = {"1001", "1002", "1003", "1004"};
        String[] nombres = {"Ana Torres", "Luis Gómez", "Marta Ríos", "Pedro Cano"};
        double[] salarios = {1800000, 2500000, 1750000, 3200000};

        for (int i = 0; i < cedulas.length; i++) {
            EmpleadoBase empleado;
            if (i % 2 == 0) {
                empleado = new EmpleadoBase(cedulas[i], nombres[i], salarios[i]);
            } else {
                empleado = new LaclaseEmpleadoAdministrativo(cedulas[i], nombres[i],
                        salarios[i], 300000);
            }
            repositorio.agregar(empleado);
        }
    }

    private boolean esNumeroValido(String texto) {
        if (texto.isEmpty() || texto.equals(".")) {
            return false;
        }
        int puntos = 0;
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            if (c == '.') {
                puntos++;
            } else if (!Character.isDigit(c)) {
                return false;
            }
        }
        return puntos <= 1;
    }

    private String validar(String cedula, String nombre, String salario,
                           String tipo, String bonificacion) {

        if ((tipo.equals("Administrativo") || tipo.equals("Comercial"))
                && !esNumeroValido(bonificacion)) {
            return "La bonificación o comisión debe ser un número válido.";
        }

        if (tipo.equals("Comercial")) {
            double porcentaje = Double.parseDouble(bonificacion);

            if (porcentaje > 50) {
                return "La comisión no puede ser mayor al 50%.";
            }
        }

        return null;
    }

    private EmpleadoBase construirEmpleado(String cedula, String nombre, String salario,
                                           String tipo, String bonificacion) {

        double salarioBase = Double.parseDouble(salario);

        if (tipo.equals("Administrativo")) {
            double bono = Double.parseDouble(bonificacion);

            return new LaclaseEmpleadoAdministrativo(
                    cedula, nombre, salarioBase, bono);
        }

        if (tipo.equals("Comercial")) {
            double porcentaje = Double.parseDouble(bonificacion);

            return new EmpleadoComercial(
                    cedula, nombre, salarioBase, porcentaje);
        }

        return new EmpleadoBase(cedula, nombre, salarioBase);
    }

    public String agregarEmpleado(String cedula, String nombre, String salario,
                                  String tipo, String bonificacion) {
        String error = validar(cedula, nombre, salario, tipo, bonificacion);
        if (error != null) {
            return error;
        }
        EmpleadoBase nuevo = construirEmpleado(cedula, nombre, salario, tipo, bonificacion);
        if (repositorio.agregar(nuevo)) {
            historial.add("AGREGADO: " + cedula + " - " + nombre);
            return "Empleado agregado correctamente.";
        }
        return "Ya existe un empleado con la cédula " + cedula + ".";
    }
    public EmpleadoBase buscarEmpleado(String cedula) {
        historial.add("BÚSQUEDA: " + cedula);
        return repositorio.buscar(cedula);
    }
    public String actualizarEmpleado(String cedula, String nombre, String salario,
                                     String tipo, String bonificacion) {
        String error = validar(cedula, nombre, salario, tipo, bonificacion);
        if (error != null) {
            return error;
        }
        EmpleadoBase actualizado =
                construirEmpleado(cedula, nombre, salario, tipo, bonificacion);
        if (repositorio.actualizar(actualizado)) {
            historial.add("ACTUALIZADO: " + cedula + " - " + nombre);
            return "Empleado actualizado correctamente.";
        }
        return "No existe ningún empleado con la cédula " + cedula + ".";
    }
    public String eliminarEmpleado(String cedula) {
        if (repositorio.eliminar(cedula)) {
            historial.add("ELIMINADO: " + cedula);
            return "Empleado eliminado correctamente.";
        }
        return "No existe ningún empleado con la cédula " + cedula + ".";
    }

    public ArrayList<EmpleadoBase> obtenerEmpleados() {
        return repositorio.listarTodos();
    }

    public double calcularTotalNomina() {
        double total = 0;
        for (EmpleadoBase empleado : repositorio.listarTodos()) {
            total += empleado.calcularSalarioTotal();
        }
        return total;
    }
    public ArrayList<String> obtenerHistorial() {
        return historial;
    }
}

