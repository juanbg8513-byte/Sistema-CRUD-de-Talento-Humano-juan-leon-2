import controlador.EmpleadoControlador;
import vista.VentanaEmpleados;
import javax.swing.SwingUtilities;
public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            EmpleadoControlador controlador = new EmpleadoControlador(); // + Modelo
            VentanaEmpleados ventana = new VentanaEmpleados(controlador); // Vista
            ventana.setVisible(true);
        });
    }
}