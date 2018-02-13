package com.grupostudium.carreracamellos;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;


public class Camello implements Runnable, Comparable<Camello>{
    private static int counter = 0;
    private final int id = counter++;
    private int pasos = 0;
    private static Random rand = new Random(47);
    private static CyclicBarrier barrier;
    private int tirada;        

    public Camello(CyclicBarrier b) {
        barrier = b;    
    }
 
    public synchronized int getPasos() {
        return pasos;
    }
 
    public void run() {
        try {
            while (!Thread.interrupted()) {
            	// Sincronizacion de todos los hilos
                synchronized (this) {
                    try {
						pasos += tirada();
					} catch (IOException e) {
						e.printStackTrace();
					}          
                }
                // El metodo await obliga a esperar a que todos los hilos detenido
                barrier.await();
            }
        } catch (InterruptedException e) {
            
        } catch (BrokenBarrierException e) {
            
            throw new RuntimeException(e);
        } 
    }
 
    public String recorrido() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < getPasos(); i++)
            s.append("*");
        s.append(id+1);
        return s.toString(); 
    }
    
    public int tirada() throws IOException{    	
    	int resultado=0;
    	int tirada=rand.nextInt(10);
    	if(tirada < 3){
    		resultado = 0;
    	}else if(tirada < 7) {
    		resultado = 1;
    	}else if(tirada < 9) {
    		resultado = 1;    		
    	}else {
    		resultado = 2;
    	}
    	setTirada(resultado);
    	return resultado;
    }

	public int getTirada() {
		return tirada;
	}

	public void setTirada(int tirada) {
		this.tirada = tirada;
	}

	public int getId() {
		return id;
	}   
	// Metodo requerido por Collections.sort para ordenar un Objeto
	public int compareTo(Camello comparaCamello) {
		int comparaPasos = ((Camello) comparaCamello).getPasos();
		//orden descendiente
		return comparaPasos - this.pasos;
	}
}
