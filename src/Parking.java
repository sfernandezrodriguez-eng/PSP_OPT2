public class Parking extends Thread {
    private static final Object BLOQUEO = new Object();
    private static int plazasLibres = 3;

    private static void entrar() throws InterruptedException {
        synchronized (BLOQUEO) {
            while (plazasLibres == 0) {
                System.out.println(Thread.currentThread().getName()
                        + " espera: el parking está lleno.");
                BLOQUEO.wait();
            }

            plazasLibres--;
            System.out.println(Thread.currentThread().getName()
                    + " aparca. Plazas libres: " + plazasLibres);
        }
    }

    private static void salir() {
        synchronized (BLOQUEO) {
            plazasLibres++;
            System.out.println(Thread.currentThread().getName()
                    + " sale. Plazas libres: " + plazasLibres);
            BLOQUEO.notifyAll();
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
