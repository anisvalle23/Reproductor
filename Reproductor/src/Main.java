
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        GestorArchivos gestorArchivos = new GestorArchivos();

        SwingUtilities.invokeLater(() -> {

            VentanaPrincipal ventana = new VentanaPrincipal(gestorArchivos);
            ventana.setVisible(true);
        });
    }
}
