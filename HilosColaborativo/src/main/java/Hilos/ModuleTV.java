/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Hilos;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Diego Silva
 */
public class ModuleTV extends Thread {

    private int cont;
    public String state;
    private String complete;
    private String name;
    private JFrameProject jfp;

    final ArrayList<String> listaTV;
    long Tinicio, Tfinal, time, seconds, second;

    public ModuleTV(JFrameProject jfp, String name) {
        this.jfp = jfp;
        this.name = name;
        this.listaTV = new ArrayList<>();
    }

    @Override
    public void run() {
        while (true) {
            synchronized (listaTV) {
                if (listaTV.isEmpty()) {
                    try {
                        listaTV.wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ModuleAwa.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                // String dato = clAwa.get(0);
                Tinicio = System.currentTimeMillis();
                for (int i = 0; i < jfp.TableClients.getRowCount(); i++) {
                    if (name == jfp.TableClients.getValueAt(i, 2)) {
                        state = "En servicio";
                        jfp.TableClients.setValueAt(state, i, 4);

                        try {
                            this.sleep((10000));
                        } catch (InterruptedException ex) {
                            Logger.getLogger(ModuleAwa.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        state = "Libre";
                        jfp.TableClients.setValueAt(state, i, 4);
                        complete = "Finalizado";
                        jfp.TableClients.setValueAt(complete, i, 2);
                    }
                }

                listaTV.remove(0);

            }

            cont = cont + 1;
            jfp.jStats.setValueAt(cont, 2, 1);

            Tfinal = System.currentTimeMillis() - Tinicio;
            seconds = (TimeUnit.MILLISECONDS.toSeconds(Tfinal)
                    % 60);
            System.out.println("el hilo tardo" + "; " + seconds + " Seg.");

            second += seconds;
            jfp.jStats.setValueAt(second + " Seg", 2, 2);

        }
    }

    public ArrayList<String> getListaTV() {
        return listaTV;
    }

}
