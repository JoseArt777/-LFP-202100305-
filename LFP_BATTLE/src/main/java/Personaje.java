public class Personaje {
    private String nombre;
    private int salud;
    private int ataque;
    private int defensa;
    private int vida;

    public Personaje(String nombre, int salud, int ataque, int defensa) {
        this.nombre = nombre;
        this.salud = salud;
        this.ataque = ataque;
        this.defensa = defensa;
        this.vida = salud * 10; // La vida inicial es 10 veces la salud
    }

    public void recibirDano(int dano) {
        this.vida -= dano;
        if (this.vida < 0) this.vida = 0;
    }

    public boolean estaVivo() {
        return this.vida > 0;
    }

    // Getters y setters
    public String getNombre() { return nombre; }
    public int getAtaque() { return ataque; }
    public int getDefensa() { return defensa; }
    public int getVida() { return vida; }
}