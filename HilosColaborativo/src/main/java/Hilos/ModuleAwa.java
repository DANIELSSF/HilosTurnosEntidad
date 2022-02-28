package Hilos;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Diego Silva
 */
public class ModuleAwa extends Thread {

    private int contA = 0;
    public String state;
    private String complete;
    private String name;
    private JFrameProject jfp;

    long TinicioA, TfinalA, timeA, secondsA, secondA;

    final ArrayList<String> lista;

    public ModuleAwa(JFrameProject jfp, String name) {
        this.jfp = jfp;
        this.name = name;
        this.lista = new ArrayList<>();
    }

    @Override
    public void run() {
        while (true) {
            synchronized (lista) {
                if (lista.isEmpty()) {
                    try {
                        lista.wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ModuleAwa.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                TinicioA = System.currentTimeMillis();
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

                lista.remove(0);

            }
           
                contA = contA + 1;
                jfp.jStats.setValueAt(contA, 0, 1);

                TfinalA = System.currentTimeMillis() - TinicioA;
                secondsA = (TimeUnit.MILLISECONDS.toSeconds(TfinalA)
                        % 60);
                secondA+=secondsA;
                jfp.jStats.setValueAt(secondA + " Seg", 0, 2);

        }

    }

    public int getCont() {
        return contA;
    }

    public ArrayList<String> getLista() {
        return lista;
    }
}
