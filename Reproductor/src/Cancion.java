

import java.io.File;

public class Cancion {
    private Cancion siguienteCancion;
    private File ubicacionArchivo;
    private File imagenArchivo;
    private String nombreCancion;
    private String duracion;  
    private String tipoMusica;  
    
   
    public Cancion(String nombreCancion, File imagenArchivo, File ubicacionArchivo, String duracion, String tipoMusica) {
        this.imagenArchivo = imagenArchivo;
        this.ubicacionArchivo = ubicacionArchivo;
        this.nombreCancion = nombreCancion;
        this.duracion = duracion;
        this.tipoMusica = tipoMusica;
        this.siguienteCancion = null;
    }
    
  
    public String getDuracion() {
        return duracion;
    }

    public String getTipoMusica() {
        return tipoMusica;
    }

    public void setSiguienteCancion(Cancion siguienteCancion) {
        this.siguienteCancion = siguienteCancion;
    }
    
    public Cancion getSiguienteCancion() {
        return siguienteCancion;
    }
    
    public String getNombreCancion() {
        return nombreCancion;
    }
    
    public File getUbicacionArchivo() {
        return ubicacionArchivo;
    }
    
    public File getImagenArchivo() {
        return imagenArchivo;
    }
}
