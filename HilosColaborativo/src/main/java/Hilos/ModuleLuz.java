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
public class ModuleLuz extends Thread {

    private int contL;
    public String state;
    private String complete;
    private String name;
    private JFrameProject jfp;

    final ArrayList<String> listaLuz;
    long TinicioL, TfinalL, timeL, secondsL, secondL;

    public ModuleLuz(JFrameProject jfp, String name) {
        this.jfp = jfp;
        this.name = name;
        this.listaLuz = new ArrayList<>();
    }

    @Override
    public void run() {
        while (true) {
            synchronized (listaLuz) {
                if (listaLuz.isEmpty()) {
                    try {
                        listaLuz.wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ModuleAwa.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                // String dato = clAwa.get(0);
                TinicioL = System.currentTimeMillis();
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

                listaLuz.remove(0);

            }

            contL = contL + 1;
            jfp.jStats.setValueAt(contL, 4, 1);

            TfinalL = System.currentTimeMillis() - TinicioL;
            secondsL = (TimeUnit.MILLISECONDS.toSeconds(TfinalL)
                    % 60);
            System.out.println("el hilo tardo" + "; " + secondsL + " Seg.");

            secondL += secondsL;
            jfp.jStats.setValueAt(secondL + " Seg", 4, 2);

        }
    }

    public ArrayList<String> getListaLuz() {
        return listaLuz;
    }

}
