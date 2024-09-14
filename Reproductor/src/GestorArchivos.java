
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GestorArchivos {

    public GestorCanciones listaCanciones;

    public GestorArchivos() {
        this.listaCanciones = new GestorCanciones();

        File carpetaGeneral = new File("General");

        if (!carpetaGeneral.exists()) {
            carpetaGeneral.mkdir();
        }

        crearListaPorDefecto(carpetaGeneral);
    }

    private void crearListaPorDefecto(File carpetaCanciones) {
        String[] archivos = carpetaCanciones.list();

        if (archivos == null || archivos.length == 0) {
            System.out.println("No se encontraron canciones en la carpeta 'General'.");
            return;
        }

        for (String nombreCancion : archivos) {
            Cancion cancion = crearCancion(nombreCancion);
            if (cancion != null) {
                listaCanciones.agregarCancion(cancion);
            }
        }
    }

    private Cancion crearCancion(String nombreCancion) {
        File carpetaCancion = new File("General", nombreCancion);

        if (!carpetaCancion.exists() || !carpetaCancion.isDirectory() || nombreCancion.equals(".DS_Store")) {
            System.out.println("Carpeta no encontrada o no es un directorio: " + carpetaCancion.getAbsolutePath());
            return null;
        }

        File[] archivos = carpetaCancion.listFiles();

        if (archivos == null || archivos.length == 0) {
            System.out.println("No se encontraron archivos en la carpeta: " + carpetaCancion.getAbsolutePath());
            return null;
        }

        File archivoImagen = null;
        File archivoMusica = null;

        for (File archivo : archivos) {
            if (archivo.getName().endsWith(".mp3")) {
                archivoMusica = archivo;
            } else if (!archivo.getName().equals(".DS_Store")) {
                archivoImagen = archivo;
            }
        }

        String duracionPredeterminada = "3:30";
        String tipoMusicaPredeterminado = "Desconocido";

        return (archivoImagen != null && archivoMusica != null)
                ? new Cancion(nombreCancion, archivoImagen, archivoMusica, duracionPredeterminada, tipoMusicaPredeterminado)
                : null;
    }

    public String[] obtenerListaCanciones() {
        File carpeta = new File("General");

        if (!carpeta.exists() || !carpeta.isDirectory()) {
            System.out.println("Carpeta no encontrada: " + carpeta.getAbsolutePath());
            return new String[0];
        }

        File[] archivos = carpeta.listFiles();

        if (archivos == null || archivos.length == 0) {
            return new String[0];
        }

        List<String> canciones = new ArrayList<>();

        for (File archivo : archivos) {
            if (archivo.isDirectory() && !archivo.getName().equals(".DS_Store")) {
                canciones.add(archivo.getName());
            }
        }

        return canciones.toArray(new String[0]);
    }

    public void guardarCancionEnArchivo(Cancion cancion) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("canciones.txt", true))) {
            String linea = cancion.getNombreCancion() + "," + cancion.getDuracion() + "," + cancion.getTipoMusica() + ","
                    + (cancion.getRutaImagen() != null ? cancion.getRutaImagen().getAbsolutePath() : "null") + ","
                    + cancion.getUbicacionArchivo().getAbsolutePath();
            bw.write(linea);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
