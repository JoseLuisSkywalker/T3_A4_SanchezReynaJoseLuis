package ProgramasHilos;

class NewState implements Runnable{

    @Override
    public void run() {

    }

}

public class PruebaHiloNew {

    public static void main(String[] args) {
        Runnable runnable = new NewState();
        Thread t = new Thread(runnable);
        System.out.println(t.getState());

    }

}
