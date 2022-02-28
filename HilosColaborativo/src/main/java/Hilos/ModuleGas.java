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
public class ModuleGas extends Thread {

    private int contG;
    public String state;
    private String complete;
    private String name;
    private JFrameProject jfp;

    final ArrayList<String> listaGas;
    long TinicioG, TfinalG, timeG, secondsG, secondG;

    public ModuleGas(JFrameProject jfp, String name) {
        this.jfp = jfp;
        this.name = name;
        this.listaGas = new ArrayList<>();
    }

    @Override
    public void run() {
        while (true) {
            synchronized (listaGas) {
                if (listaGas.isEmpty()) {
                    try {
                        listaGas.wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ModuleAwa.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                TinicioG = System.currentTimeMillis();
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

                listaGas.remove(0);

            }

            contG = contG + 1;
            jfp.jStats.setValueAt(contG, 1, 1);

            TfinalG = System.currentTimeMillis() - TinicioG;
            secondsG = (TimeUnit.MILLISECONDS.toSeconds(TfinalG)
                    % 60);
            System.out.println("el hilo tardo" + "; " + secondsG + " Seg.");

            secondG += secondsG;
            jfp.jStats.setValueAt(secondG + " Seg", 1, 2);

        }
    }

    public ArrayList<String> getListaGas() {
        return listaGas;
    }
}
