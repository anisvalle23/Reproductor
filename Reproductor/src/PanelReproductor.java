
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import javax.swing.*;
import javazoom.jl.player.Player;

public class PanelReproductor extends JPanel implements Runnable {

    private final GestorArchivos gestorArchivos;
    private boolean reproduciendo;
    private int ultimoFrame = 0;
    private Player reproductor;
    private JComboBox<String> listaCanciones;
    private JLabel imagenCancion;
    private JLabel duracionCancion;
    private JLabel tipoMusicaCancion;
    private JButton botonPlay, botonStop, botonPause, botonAdd, botonSelect;

    public PanelReproductor(GestorArchivos gestorArchivos) {
        this.gestorArchivos = gestorArchivos;
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.DARK_GRAY);
        setLayout(new GridBagLayout());
        inicializarElementos();
        new Thread(this).start();
    }

    private void inicializarElementos() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        JLabel textoLista = new JLabel("Lista de Canciones:");
        textoLista.setForeground(Color.WHITE);
        textoLista.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.insets.set(10, 10, 10, 10);
        add(textoLista, gbc);

        listaCanciones = new JComboBox<>(gestorArchivos.listaCanciones.obtenerListaCanciones());
        gbc.gridy = 1;
        listaCanciones.setPreferredSize(new Dimension(250, 30));
        add(listaCanciones, gbc);

        imagenCancion = new JLabel();
        imagenCancion.setPreferredSize(new Dimension(300, 300));
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(imagenCancion, gbc);
        cambiarImagenCancion();

        duracionCancion = new JLabel("Duración: ");
        duracionCancion.setForeground(Color.WHITE);
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        add(duracionCancion, gbc);

        tipoMusicaCancion = new JLabel("Tipo de Música: ");
        tipoMusicaCancion.setForeground(Color.WHITE);
        gbc.gridx = 1;
        add(tipoMusicaCancion, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 4;
        gbc.insets.set(20, 10, 10, 10);

        botonPlay = new JButton("Play");
        botonPlay.setPreferredSize(new Dimension(100, 40));
        botonPlay.addActionListener(e -> reproducirCancion());
        add(botonPlay, gbc);

        botonPause = new JButton("Pause");
        gbc.gridx = 1;
        botonPause.setPreferredSize(new Dimension(100, 40));
        botonPause.addActionListener(e -> pausarCancion());
        add(botonPause, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        botonStop = new JButton("Stop");
        botonStop.setPreferredSize(new Dimension(100, 40));
        botonStop.addActionListener(e -> detenerCancion());
        add(botonStop, gbc);

        botonAdd = new JButton("Add");
        gbc.gridx = 1;
        botonAdd.setPreferredSize(new Dimension(100, 40));
        botonAdd.addActionListener(e -> agregarCancion());
        add(botonAdd, gbc);

        botonSelect = new JButton("Select");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        botonSelect.setPreferredSize(new Dimension(100, 40));
        botonSelect.addActionListener(e -> seleccionarCancion());
        add(botonSelect, gbc);
    }

    private void cambiarImagenCancion() {
        Cancion cancion = gestorArchivos.listaCanciones.obtenerCancion(listaCanciones.getSelectedIndex());
        if (cancion != null && cancion.getImagenArchivo() != null) {
            int ancho = (imagenCancion.getWidth() > 0) ? imagenCancion.getWidth() : 300;
            int alto = (imagenCancion.getHeight() > 0) ? imagenCancion.getHeight() : 300;
            imagenCancion.setIcon(escalarImagen(cancion.getImagenArchivo().getAbsolutePath(), ancho, alto));

            // Actualizar duración y tipo de música
            duracionCancion.setText("Duración: " + cancion.getDuracion());
            tipoMusicaCancion.setText("Tipo de Música: " + cancion.getTipoMusica());
        }
    }

    private ImageIcon escalarImagen(String ruta, int ancho, int alto) {
        ImageIcon icono = new ImageIcon(ruta);
        Image imagen = icono.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        return new ImageIcon(imagen);
    }

    private void reproducirCancion() {
        try {
            if (!reproduciendo) {
                Cancion cancion = gestorArchivos.listaCanciones.obtenerCancion(listaCanciones.getSelectedIndex());
                if (cancion != null) {
                    FileInputStream archivoMusica = new FileInputStream(cancion.getUbicacionArchivo());
                    BufferedInputStream entrada = new BufferedInputStream(archivoMusica);
                    reproductor = new Player(entrada);
                    new Thread(() -> {
                        try {
                            reproductor.play();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }).start();
                    reproduciendo = true;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void pausarCancion() {
        if (reproduciendo) {
            reproductor.close();
            reproduciendo = false;
        }
    }

    private void detenerCancion() {
        if (reproduciendo) {
            reproductor.close();
            reproduciendo = false;
        }
    }

    private void agregarCancion() {
        String nombreCancion = JOptionPane.showInputDialog(this, "Nombre de la canción:");
        String artista = JOptionPane.showInputDialog(this, "Artista:");
        String duracion = JOptionPane.showInputDialog(this, "Duración:");
        String tipoMusica = JOptionPane.showInputDialog(this, "Tipo de música:");
        File archivo = new File("ruta/al/archivo.mp3");
        File imagen = new File("ruta/a/la/imagen.jpg");

        Cancion nuevaCancion = new Cancion(nombreCancion, imagen, archivo, duracion, tipoMusica);
        gestorArchivos.listaCanciones.agregarCancion(nuevaCancion);
        listaCanciones.addItem(nombreCancion + " - " + artista);
    }

    private void seleccionarCancion() {
        cambiarImagenCancion();
    }

    @Override
    public void run() {
        while (true) {
            repaint();
        }
    }
}
