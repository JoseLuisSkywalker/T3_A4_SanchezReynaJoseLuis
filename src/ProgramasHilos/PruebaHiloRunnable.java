package ProgramasHilos;

class Newstate implements Runnable {
    @Override
    public void run() {
        // Aquí podrías poner algo para que el hilo haga
        System.out.println("Hilo en ejecución");
    }

}
public class PruebaHiloRunnable {

    public static void main(String[] args) {
        Runnable runnable = new Newstate();
        Thread t = new Thread(runnable);
        t.start();
        System.out.println(t.getState());

    }

}
