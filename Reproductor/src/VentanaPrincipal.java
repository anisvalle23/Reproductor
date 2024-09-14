
import javax.swing.JFrame;

public class VentanaPrincipal extends JFrame {
    private final GestorArchivos gestorArchivos;
    
    public VentanaPrincipal(GestorArchivos gestorArchivos) {
        this.gestorArchivos = gestorArchivos;
        add(new PanelReproductor(gestorArchivos));
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
