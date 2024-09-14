
import java.io.File;

public class GestorCanciones {

    private Cancion primeraCancion;
    private int tamano;

    public GestorCanciones() {
        primeraCancion = null;
        tamano = 0;
    }

    public int getTamano() {
        return tamano;
    }
    
    public boolean estaVacia() {
        return primeraCancion == null;
    }
    
    public boolean agregarCancion(String nombreCancion, File rutaImagen, File ubicacionArchivo) {
        return agregarCancion(new Cancion(nombreCancion, rutaImagen, ubicacionArchivo), primeraCancion);
    }
    
    public boolean agregarCancion(Cancion nuevaCancion) {
        return agregarCancion(nuevaCancion, primeraCancion);
    }
    
    private boolean agregarCancion(Cancion nuevaCancion, Cancion actualCancion) {
        if (nuevaCancion == null) return false;
        
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
}
