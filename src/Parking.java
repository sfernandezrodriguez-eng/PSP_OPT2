/**
 * Clase comprueba las plazas del parking para meter/sacar coches.
 * Cada coche es un hilo y el parking dispone de tres plazas libres,
 * comunes a todas las instancias. Los métodos entrar() y salir() estan
 * sincronizado (syncronized) para que el acceso al número de plazas sea el mismo
 * para todos y no haya varios coches en una plsaza. Si no quedan plazas, el coche
 * espera hasta que otro coche salga, entonces se avisa a los coches que estaban esperando.
 * Cuando consigue aparcar, el coche permanece allí cinco segundos (elegí esto para representar
 * un tiempo en el que el coche ocupa la plaza y saliera por pantalla) y después libera la plaza.
 * @author Sergio
 */
public class Parking extends Thread {
    private static final Object bloqueo = new Object();
    private static int plazasLibres = 3;

    private static void entrar() throws InterruptedException {
        synchronized (bloqueo) {
            while (plazasLibres == 0) {
                System.out.println(Thread.currentThread().getName()
                        + " espera: el parking está lleno.");
                bloqueo.wait();
            }

            plazasLibres--;
            System.out.println(Thread.currentThread().getName()
                    + " aparca. Plazas libres: " + plazasLibres);
        }
    }

    private static void salir() {
        synchronized (bloqueo) {
            plazasLibres++;
            System.out.println(Thread.currentThread().getName()
                    + " sale. Plazas libres: " + plazasLibres);
            bloqueo.notifyAll();
        }
    }

    @Override
    public void run() {
        boolean aparcado = false;

        try {
            entrar();
            aparcado = true;
            Thread.sleep(5_000); // Tiempo que permanece aparcado
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            if (aparcado) {
                salir();
            }
        }
    }
}
