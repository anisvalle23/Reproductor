

import java.io.File;

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
        
        if (!carpetaCancion.isDirectory()) {
            return null; 
        }

        File archivoImagen = null;
        File archivoMusica = null;

       
        for (File archivo : carpetaCancion.listFiles()) {
            if (archivo.getName().endsWith(".mp3")) {
                archivoMusica = archivo;
            } else {
                archivoImagen = archivo;
            }
        }

        
        return (archivoImagen != null && archivoMusica != null) ? new Cancion(nombreCancion, archivoImagen, archivoMusica) : null;
    }
}
