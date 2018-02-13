package com.grupostudium.carreracamellos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CarreraMain{
	
	static final int DISTANCIA_META = 30;
    private List<Camello> camellos = new ArrayList<Camello>();
    private ExecutorService exec = Executors.newCachedThreadPool();
    private CyclicBarrier barrier;
    private int lider;
    private int posiciones[];
    private boolean llegadaMeta;
   
    public CarreraMain(int numCamellos, final int pause) {
    	lider = 0;
    	llegadaMeta = false;
    	posiciones = new int[numCamellos];
    	
    	// Clase CyclicBarrier para sincronizar los hilos
    	// Se instancia pasandole los hilos a sincronizar
        barrier = new CyclicBarrier(numCamellos, new Runnable() {
            public void run() {
                StringBuilder s = new StringBuilder();
                // linea de la valla =====
                for (int i = 0; i < DISTANCIA_META; i++)
                    s.append("=");
                System.out.println(s);

                // linea del camello *****1
                for (Camello camello : camellos) {
                	System.out.println(camello.recorrido());
                	// obtenemos el lider
                	if(camello.getPasos() > lider) {
                		lider = camello.getPasos();
                	}
                }
                // linea de valla inferior	
                System.out.println(s);
                
                // A cada tirada, imprimimos las posiciones
                for (Camello camello : camellos) {
                	if(camello.getPasos() == lider) {
                		System.out.println("El Camello " + (camello.getId()+1) + " " 
                				+ "lleva: "+camello.getPasos()+" >>>>>>>>"
                				+ " Va en cabeza<<<<<<");
                	}else {
                		System.out.println("El Camello " + (camello.getId()+1) + " " 
                				+ "lleva: "+camello.getPasos()+" >>"
                				+ " a "+(lider-camello.getPasos())
                				+" posiciones del lider");
                	}
                	if (camello.getPasos() >= DISTANCIA_META) {
                		llegadaMeta = true;
                	} 
                }
                // Se llego a la meta
                if(llegadaMeta) {
                	System.out.println(s);
                	System.out.println("  RESULTADO FINAL: ");
                	System.out.println(s);
                	// Ordenar las posiciones e imprimir                 	
                	Collections.sort(camellos);
                	int i=1;
                	for (Camello camello : camellos) {
                		if(i==1) {
                			System.out.println(i+"º: Camello "+ (camello.getId()+1));
                		}else {
                			System.out.println(i+"º: Camello "+ (camello.getId()+1)
                					+ " a "+(DISTANCIA_META - camello.getPasos())+" posiciones");
                		}                		
                		i++;
                	}
                	exec.shutdownNow();
                    return;
                }                	               
                
                try {
                    TimeUnit.MILLISECONDS.sleep(pause);
                } catch (InterruptedException e) {
                	System.out.println("barrier-action sleep interrupted");
                }
            }
        });
        // instanciamos el numero de Camellos y creamos sus hilos
        for (int i = 0; i < numCamellos; i++) {
            Camello camello = new Camello(barrier);
            camellos.add(camello);
            exec.execute(camello);         
        }
    }

	public static void main(String[] args) throws InterruptedException{
		int numCamellos = 4;
		// Solicitamos el numero de camellos a utilizar
    	BufferedReader lectura = new BufferedReader(new InputStreamReader(System.in));
    	System.out.println("Cuantos camellos van a correr?: ");
    	try {
    		numCamellos= Integer.parseInt(lectura.readLine());
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    	
        int pause = 500;
        new CarreraMain(numCamellos, pause);      
    }
}

