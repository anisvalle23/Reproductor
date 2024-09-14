
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GestorCanciones {

    private Cancion primeraCancion;
    private int tamano;
    private final File archivoCanciones;

    public GestorCanciones() {
        primeraCancion = null;
        tamano = 0;
        archivoCanciones = new File("canciones.txt");
        cargarCancionesDesdeArchivo();
    }

    public int getTamano() {
        return tamano;
    }

    public boolean estaVacia() {
        return primeraCancion == null;
    }

    public boolean agregarCancion(String nombreCancion, File rutaImagen, File ubicacionArchivo) {
        String duracionPredeterminada = "3:30";
        String tipoMusicaPredeterminado = "Desconocido";
        Cancion nuevaCancion = new Cancion(nombreCancion, rutaImagen, ubicacionArchivo, duracionPredeterminada, tipoMusicaPredeterminado);

        boolean resultado = agregarCancion(nuevaCancion);
        if (resultado) {
            guardarCancionEnArchivo(nuevaCancion);
        }
        return resultado;
    }

    public boolean agregarCancion(Cancion nuevaCancion) {
        return agregarCancion(nuevaCancion, primeraCancion);
    }

    private boolean agregarCancion(Cancion nuevaCancion, Cancion actualCancion) {
        if (nuevaCancion == null) {
            return false;
        }

        if (primeraCancion == null) {
            primeraCancion = nuevaCancion;
            tamano++;
            return true;
        } else if (actualCancion != null) {
            if (actualCancion.getSiguienteCancion() == null) {
                actualCancion.setSiguienteCancion(nuevaCancion);
                tamano++;
                return true;
            } else {
                return agregarCancion(nuevaCancion, actualCancion.getSiguienteCancion());
            }
        }
        return false;
    }

    public Cancion obtenerCancion(int indice) {
        if (primeraCancion != null) {
            Cancion actualCancion = primeraCancion;
            for (int i = 0; i < tamano; i++) {
                if (i == indice) {
                    return actualCancion;
                }
                actualCancion = actualCancion.getSiguienteCancion();
            }
        }
        return null;
    }

    public String[] obtenerListaCanciones() {
        if (!estaVacia()) {
            String[] canciones = new String[tamano];
            Cancion actualCancion = primeraCancion;
            for (int i = 0; i < tamano; i++) {
                canciones[i] = actualCancion.getNombreCancion();
                actualCancion = actualCancion.getSiguienteCancion();
            }
            return canciones;
        }
        return null;
    }

    private void guardarCancionEnArchivo(Cancion cancion) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoCanciones, true))) {
            String linea = cancion.getNombreCancion() + "," + cancion.getDuracion() + "," + cancion.getTipoMusica() + ","
                    + (cancion.getRutaImagen() != null ? cancion.getRutaImagen().getAbsolutePath() : "null") + ","
                    + cancion.getUbicacionArchivo().getAbsolutePath();
            bw.write(linea);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cargarCancionesDesdeArchivo() {
        if (!archivoCanciones.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivoCanciones))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length == 5) {
                    String nombre = datos[0];
                    String duracion = datos[1];
                    String tipoMusica = datos[2];
                    File imagen = "null".equals(datos[3]) ? null : new File(datos[3]);
                    File archivo = new File(datos[4]);

                    Cancion cancion = new Cancion(nombre, imagen, archivo, duracion, tipoMusica);
                    agregarCancion(cancion);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean eliminarCancion(int indice) {
        if (indice < 0 || indice >= tamano || primeraCancion == null) {
            return false;
        }

        if (indice == 0) {
            primeraCancion = primeraCancion.getSiguienteCancion();
            tamano--;
            actualizarArchivoCanciones();
            return true;
        }

        Cancion actualCancion = primeraCancion;
        for (int i = 0; i < indice - 1; i++) {
            actualCancion = actualCancion.getSiguienteCancion();
        }

        if (actualCancion != null && actualCancion.getSiguienteCancion() != null) {
            actualCancion.setSiguienteCancion(actualCancion.getSiguienteCancion().getSiguienteCancion());
            tamano--;
            actualizarArchivoCanciones();
            return true;
        }

        return false;
    }

    private void actualizarArchivoCanciones() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoCanciones))) {
            Cancion actualCancion = primeraCancion;
            while (actualCancion != null) {
                String linea = actualCancion.getNombreCancion() + "," + actualCancion.getDuracion() + "," + actualCancion.getTipoMusica() + ","
                        + (actualCancion.getRutaImagen() != null ? actualCancion.getRutaImagen().getAbsolutePath() : "null") + ","
                        + actualCancion.getUbicacionArchivo().getAbsolutePath();
                bw.write(linea);
                bw.newLine();
                actualCancion = actualCancion.getSiguienteCancion();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
