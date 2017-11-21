package almundo.callcenter;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DispatcherTest {

    Dispatcher dispatcher;
    Runnable caller;

    @Before
    public void setup() {
        caller = new Runnable() {
            public void run() {
                dispatcher.dispatchCall(new Call());
            }
        };
    }

    @Test
    public void hasta10CallsTest() throws InterruptedException {
        dispatcher = getDispatcher(5, 4, 2);

        ExecutorService fixedPool = Executors.newFixedThreadPool(10);

        for(int i=0; i<10; i++)
            fixedPool.submit(caller);

        fixedPool.awaitTermination(15, TimeUnit.SECONDS);
    }

    @Test
    public void mas10CallsTest() throws InterruptedException {
        dispatcher = getDispatcher(5, 4, 2);

        ExecutorService fixedPool = Executors.newFixedThreadPool(20);

        for(int i=0; i<20; i++)
            fixedPool.submit(caller);

        fixedPool.awaitTermination(30, TimeUnit.SECONDS);
    }

    @Test
    public void masCallsQueEmpleadosTest() throws InterruptedException {
        dispatcher = getDispatcher(1, 1, 1);

        ExecutorService fixedPool = Executors.newFixedThreadPool(5);

        for(int i=0; i<5; i++)
            fixedPool.submit(caller);

        fixedPool.awaitTermination(30, TimeUnit.SECONDS);
    }


    private Dispatcher getDispatcher(int operadoresQ, int supervisoresQ, int directoresQ) {
        List<Empleado> operadores = new ArrayList<Empleado>();
        List<Empleado> supervisores = new ArrayList<Empleado>();
        List<Empleado> directores = new ArrayList<Empleado>();

        for(int i=0; i<operadoresQ; i++)
            operadores.add( new Operador());

        for(int i=0; i<supervisoresQ; i++)
            supervisores.add( new Supervisor());

        for(int i=0; i<directoresQ; i++)
            directores.add( new Director());

        return new Dispatcher(operadores, supervisores, directores);
    }

}
