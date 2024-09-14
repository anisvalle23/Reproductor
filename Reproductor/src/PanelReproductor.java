import java.awt.*;
import java.io.*;
import javax.swing.*;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

public class PanelReproductor extends JPanel implements Runnable {

    private final GestorArchivos gestorArchivos;
    private AdvancedPlayer reproductor;
    private FileInputStream archivoMusica;
    private boolean reproduciendo;
    private boolean enPausa;
    private long bytePosicionActual;
    private File archivoActual;

    private JComboBox<String> listaCanciones;
    private JLabel imagenCancion;
    private JLabel duracionCancion;
    private JLabel tipoMusicaCancion;
    private JButton botonPlay, botonStop, botonPause, botonAdd, botonSelect, botonUpdate;

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
        gbc.insets.set(10, 10, 10, 10);

        JLabel textoLista = new JLabel("Lista de Canciones:");
        textoLista.setForeground(Color.WHITE);
        textoLista.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
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
        gbc.gridx = 0;
        add(botonPlay, gbc);
        botonPlay.addActionListener(e -> reproducirCancion());

        botonPause = new JButton("Pause");
        botonPause.setPreferredSize(new Dimension(100, 40));
        gbc.gridx = 1;
        add(botonPause, gbc);
        botonPause.addActionListener(e -> pausarReanudarCancion());

        gbc.gridx = 0;
        gbc.gridy = 5;
        botonStop = new JButton("Stop");
        botonStop.setPreferredSize(new Dimension(100, 40));
        add(botonStop, gbc);
        botonStop.addActionListener(e -> detenerCancion());

        gbc.gridx = 1;
        botonAdd = new JButton("Add");
        botonAdd.setPreferredSize(new Dimension(100, 40));
        add(botonAdd, gbc);
        botonAdd.addActionListener(e -> agregarCancion());

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        botonSelect = new JButton("Select");
        botonSelect.setPreferredSize(new Dimension(100, 40));
        add(botonSelect, gbc);
        botonSelect.addActionListener(e -> seleccionarCancion());

        gbc.gridy = 7;
        gbc.gridwidth = 2;
        botonUpdate = new JButton("Update");
        botonUpdate.setPreferredSize(new Dimension(100, 40));
        add(botonUpdate, gbc);
        botonUpdate.addActionListener(e -> actualizarListaCanciones());

        cambiarImagenCancion();
    }

    private void cambiarImagenCancion() {
        Cancion cancion = gestorArchivos.listaCanciones.obtenerCancion(listaCanciones.getSelectedIndex());
        if (cancion != null && cancion.getRutaImagen() != null) {
            int ancho = (imagenCancion.getWidth() > 0) ? imagenCancion.getWidth() : 300;
            int alto = (imagenCancion.getHeight() > 0) ? imagenCancion.getHeight() : 300;
            imagenCancion.setIcon(escalarImagen(cancion.getRutaImagen().getAbsolutePath(), ancho, alto));

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
            Cancion cancion = gestorArchivos.listaCanciones.obtenerCancion(listaCanciones.getSelectedIndex());
            if (cancion != null && cancion.getUbicacionArchivo() != null) {
                detenerCancion();

                archivoActual = cancion.getUbicacionArchivo();

                if (!enPausa) {
                    archivoMusica = new FileInputStream(archivoActual);
                    bytePosicionActual = 0;  // Comenzar desde el principio
                } else {
                    archivoMusica = new FileInputStream(archivoActual);
                    archivoMusica.skip(bytePosicionActual);  // Reanudar desde la posición guardada
                }

                reproductor = new AdvancedPlayer(archivoMusica);
                reproduciendo = true;

                new Thread(() -> {
                    try {
                        reproductor.play();
                    } catch (JavaLayerException e) {
                        e.printStackTrace();
                    }
                }).start();

                enPausa = false;
                botonPause.setText("Pause");
            } else {
                System.out.println("Archivo de música no encontrado o ruta inválida.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void pausarReanudarCancion() {
        if (enPausa) {
            reproducirCancion();  // Reanudar
        } else {
            pausarCancion();  // Pausar
        }
    }

    private void pausarCancion() {
        if (reproduciendo && reproductor != null) {
            try {
                // Guardamos la posición actual en bytes
                bytePosicionActual = archivoMusica.getChannel().position();

                // Cerramos el reproductor para detener la música, pero mantenemos la posición en el archivo
                reproductor.close();  // Detener el reproductor actual

                archivoMusica.close();  // Cerramos el archivo pero mantenemos la posición en bytes
                enPausa = true;  // Cambiamos el estado a 'enPausa'
                botonPause.setText("Resume");  // Cambiamos el texto del botón a 'Resume' para que se reanude al presionarlo de nuevo
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void detenerCancion() {
        if (reproductor != null) {
            reproductor.close();
            enPausa = false;
            botonPause.setText("Pause");
            bytePosicionActual = 0;  // Reiniciar la posición cuando se detiene la canción
        }
    }

    private void agregarCancion() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecciona un archivo de música");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Archivos MP3", "mp3"));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            fileChooser.setDialogTitle("Selecciona una imagen para la canción (opcional)");
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Archivos de Imagen", "jpg", "png"));
            result = fileChooser.showOpenDialog(this);
            File imagen = null;
            if (result == JFileChooser.APPROVE_OPTION) {
                imagen = fileChooser.getSelectedFile();
            }

            String nombreCancion = JOptionPane.showInputDialog(this, "Nombre de la canción:");
            String duracion = JOptionPane.showInputDialog(this, "Duración (en formato mm:ss):");
            String tipoMusica = JOptionPane.showInputDialog(this, "Tipo de música:");

            if (nombreCancion != null && !nombreCancion.trim().isEmpty()) {
                Cancion nuevaCancion = new Cancion(nombreCancion, imagen, archivo, duracion, tipoMusica);
                gestorArchivos.listaCanciones.agregarCancion(nuevaCancion);
                gestorArchivos.guardarCancionEnArchivo(nuevaCancion);
                listaCanciones.addItem(nombreCancion + " - " + tipoMusica);
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, proporciona un nombre válido para la canción.");
            }
        }
    }

    private void seleccionarCancion() {
        cambiarImagenCancion();
    }

    private void actualizarListaCanciones() {
        listaCanciones.removeAllItems();
        String[] nuevasCanciones = gestorArchivos.listaCanciones.obtenerListaCanciones();
        for (String cancion : nuevasCanciones) {
            listaCanciones.addItem(cancion);
        }
        if (nuevasCanciones.length > 0) {
            cambiarImagenCancion();
        }
    }

    @Override
    public void run() {
        while (true) {
            repaint();
        }
    }
}
