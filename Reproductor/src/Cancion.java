import java.io.File;

public class Cancion {
    private String nombreCancion;
    private File rutaImagen;
    private File ubicacionArchivo;
    private String duracion;
    private String tipoMusica;
    private Cancion siguienteCancion;

    public Cancion(String nombreCancion, File rutaImagen, File ubicacionArchivo, String duracion, String tipoMusica) {
        this.nombreCancion = nombreCancion;
        this.rutaImagen = rutaImagen;
        this.ubicacionArchivo = ubicacionArchivo;
        this.duracion = duracion;
        this.tipoMusica = tipoMusica;
        this.siguienteCancion = null;
    }

    public String getNombreCancion() {
        return nombreCancion;
    }

    public File getRutaImagen() {
        return rutaImagen;
    }

    public File getUbicacionArchivo() {
        return ubicacionArchivo;
    }

    public String getDuracion() {
        return duracion;
    }

    public String getTipoMusica() {
        return tipoMusica;
    }

    public Cancion getSiguienteCancion() {
        return siguienteCancion;
    }

    public void setSiguienteCancion(Cancion siguienteCancion) {
        this.siguienteCancion = siguienteCancion;
    }
}
