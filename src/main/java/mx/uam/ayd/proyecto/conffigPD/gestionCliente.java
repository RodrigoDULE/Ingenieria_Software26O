package mx.uam.ayd.proyecto.conffigPD;

public class gestionCliente {

    // 1. Única instancia de la clase
    private static gestionCliente instance;

    // 2. Atributo de instancia
    private long idActivo;

    // 3. Constructor privado
    private gestionCliente() {}

    // 4. Método global para obtener la instancia única
    public static gestionCliente getInstance() {
        if (instance == null) {
            instance = new gestionCliente();
        }
        return instance;
    }

    // 5. Métodos de instancia donde SÍ puedes usar 'this'
    public void iniciarSesion(long idActivo) {
        this.idActivo = idActivo;
    }

    public long getIdActivo() {
        return this.idActivo;
    }
}