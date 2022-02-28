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
public class ModuleInternet extends Thread {

    private int contI;
    public String state;
    private String name;
    private String complete;
    private JFrameProject jfp;

    final ArrayList<String> listaIn;
    long TinicioI, TfinalI, timeI, secondsI, secondI;

    public ModuleInternet(JFrameProject jfp, String name) {
        this.jfp = jfp;
        this.name = name;
        this.listaIn = new ArrayList<>();
    }

    @Override
    public void run() {
        while (true) {
            synchronized (listaIn) {
                if (listaIn.isEmpty()) {
                    try {
                        listaIn.wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ModuleAwa.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                // String dato = clAwa.get(0);
                TinicioI = System.currentTimeMillis();
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

                listaIn.remove(0);

            }

            contI = contI + 1;
            jfp.jStats.setValueAt(contI, 3, 1);

            TfinalI = System.currentTimeMillis() - TinicioI;
            secondsI = (TimeUnit.MILLISECONDS.toSeconds(TfinalI)
                    % 60);
            System.out.println("el hilo tardo" + "; " + secondsI + " Seg.");

            secondI += secondsI;
            jfp.jStats.setValueAt(secondI + " Seg", 3, 2);

        }
    }

    public ArrayList<String> getListaIn() {
        return listaIn;
    }

}
