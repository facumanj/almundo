package almundo.callcenter;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by facundo on 20/11/2017.
 */
public class Dispatcher {

    private ArrayBlockingQueue<Empleado> operadores;
    private ArrayBlockingQueue<Empleado> supervisores;
    private ArrayBlockingQueue<Empleado> directores;

    ExecutorService fixedPool = Executors.newFixedThreadPool(10);

    public Dispatcher(List<Empleado> operadores, List<Empleado> supervisores, List<Empleado> directores) {
        if( CollectionUtils.isEmpty(operadores)
                || CollectionUtils.isEmpty(supervisores)
            || CollectionUtils.isEmpty(directores))
            new RuntimeException("Ningun grupo de empleados puede ser nulo o vacio");

        this.operadores = new ArrayBlockingQueue<Empleado>(operadores.size(), true, operadores);
        this.supervisores = new ArrayBlockingQueue<Empleado>(operadores.size(), true, supervisores);
        this.directores = new ArrayBlockingQueue<Empleado>(operadores.size(), true, directores);
    }

    public void dispatchCall(Call call) {
        Empleado empleado = null;
        BlockingQueue queue = null;

        try {
            // Busca un empleado del tipo operador libre, sin esperar en caso que ninguno este libre
            empleado = operadores.poll(0, TimeUnit.SECONDS);
            queue = operadores;

            // Busca un empleado del tipo supervisor libre, sin esperar en caso que ninguno este libre
            if (empleado == null) {
                empleado = supervisores.poll(0, TimeUnit.SECONDS);
                queue = supervisores;
            }

            // Busca un empleado del tipo director libre, sin esperar en caso que ninguno este libre
            if (empleado == null) {
                empleado = directores.poll(0, TimeUnit.SECONDS);
                queue = directores;
            }

            // Si no hay ningun empleado libre, queda a la espera de que haya algun empleado del tipo operador libre
            if (empleado == null) {
                empleado = operadores.take();
                queue = operadores;
            }

            empleado.setCall(call);
            empleado.setQueue(queue);

            fixedPool.submit(empleado);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String [] args) {
        // Crear operadores
        List<Empleado> operadores = new ArrayList<Empleado>();

        operadores.add( new Operador());
        operadores.add( new Operador());
        operadores.add( new Operador());
        operadores.add( new Operador());
        operadores.add( new Operador());
        operadores.add( new Operador());

        // Crear supervisores
        List<Empleado> supervisores = new ArrayList<Empleado>();

        supervisores.add( new Supervisor());
        supervisores.add( new Supervisor());
        supervisores.add( new Supervisor());

        // Crear directores
        List<Empleado> directores = new ArrayList<Empleado>();

        supervisores.add( new Director());
        supervisores.add( new Director());
        supervisores.add( new Director());

        // Crear dispatcher
        Dispatcher dispatcher = new Dispatcher(operadores, supervisores, directores);
    }
}
