/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Hilos;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Diego Silva
 */
public class Hilos extends Thread {

    private JFrameProject jfp;
    public Clients[] cl = new Clients[13];
    private String[] tr = {"Awa", "Luz", "Internet", "Gas", "TV"};
    private int[] turnos = {1, 1, 1, 1, 1};

    public ArrayList<String> clAwa = new ArrayList<>();
    public ArrayList<String> clLuz = new ArrayList<>();
    public ArrayList<String> clIn = new ArrayList<>();
    public ArrayList<String> clGas = new ArrayList<>();
    public ArrayList<String> clTv = new ArrayList<>();

    ModuleAwa moduleAwa;
    ModuleLuz moduleLuz;
    ModuleInternet moduleIn;
    ModuleTV moduleTv;
    ModuleGas moduleGas;

    DefaultTableModel model2;
    DefaultTableModel model;

    public Hilos(JFrameProject jfp) {
        this.jfp = jfp;
        moduleAwa = new ModuleAwa(jfp, "Awa");
        moduleLuz = new ModuleLuz(jfp, "Luz");
        moduleIn = new ModuleInternet(jfp, "Internet");
        moduleTv = new ModuleTV(jfp, "TV");
        moduleGas = new ModuleGas(jfp, "Gas");
    }

    @Override
    public void run() {
        read();
        moduleAwa.start();
        moduleLuz.start();
        moduleGas.start();
        moduleIn.start();
        moduleTv.start();

        Random r = new Random();
        model = new DefaultTableModel();
        model.addColumn("Nombre");
        model.addColumn("Apellidos");
        model.addColumn("Tramite");
        model.addColumn("Turno");
        model.addColumn("Estado Modulo");
        jfp.TableClients.setModel(model);

        Thread th1 = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < cl.length; i++) {
                    String[] info = new String[13];
                    String name = cl[i].getName().replaceAll("^\"+|\"+$", "");
                    String lastName = cl[i].getLastName().replaceAll("^\"+|\"+$", "");
                    int e = r.nextInt(tr.length);
                    info[1] = lastName;
                    info[0] = name;
                    info[2] = tr[e];
                    info[3] = String.valueOf(turnos[e]++);

                    switch (tr[e]) {

                        case "Awa" -> {
                            synchronized (moduleAwa.getLista()) {
                                moduleAwa.getLista().add(String.valueOf(turnos[e] - 1));
                                moduleAwa.getLista().notify();
                            }
                        }
                        case "Luz" -> {
                            synchronized (moduleLuz.getListaLuz()) {
                                moduleLuz.getListaLuz().add(String.valueOf(turnos[e] - 1));
                                moduleLuz.getListaLuz().notify();
                            }
                        }
                        case "Internet" -> {
                            synchronized (moduleIn.getListaIn()) {
                                moduleIn.getListaIn().add(String.valueOf(turnos[e] - 1));
                                moduleIn.getListaIn().notify();
                            }
                        }
                        case "Gas" -> {
                            synchronized (moduleGas.getListaGas()) {
                                moduleGas.getListaGas().add(String.valueOf(turnos[e] - 1));
                                moduleGas.getListaGas().notify();
                            }
                        }
                        case "TV" -> {
                            synchronized (moduleTv.getListaTV()) {
                                moduleTv.getListaTV().add(String.valueOf(turnos[e] - 1));
                                moduleTv.getListaTV().notify();
                            }
                        }
                        default ->
                            throw new AssertionError();
                    }
                    model.addRow(info);

                    try {
                        this.sleep((long) (Math.random() * 3000));
                    } catch (InterruptedException ex) {
                        Logger.getLogger(JFrameProject.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }

        };

        th1.run();
        try {
            th1.wait(10000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Hilos.class.getName()).log(Level.SEVERE, null, ex);
        }

        model2 = new DefaultTableModel();
        model2.addColumn("Modulo");
        model2.addColumn("Número de Tramites");
        model2.addColumn("Tiempo de Uso");
        jfp.jStats.setModel(model2);

    }

    public void read() {
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader("src/main/java/Hilos/Cliente.json")) {
            ObjectMapper objectMapper = new ObjectMapper();
            Object obj = jsonParser.parse(reader);
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray clientsList = (JSONArray) jsonObject.get("Clientes");
            for (int i = 0; i < clientsList.size(); i++) {
                JSONObject jsonObject1 = (JSONObject) clientsList.get(i);
                cl[i] = new Clients(objectMapper.writeValueAsString(jsonObject1.get("name")), objectMapper.writeValueAsString(jsonObject1.get("lastName")), i + 1);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<String> getClAwa() {
        return clAwa;
    }

    public ArrayList<String> getClLuz() {
        return clLuz;
    }

    public ArrayList<String> getClIn() {
        return clIn;
    }

    public ArrayList<String> getClGas() {
        return clGas;
    }

    public ArrayList<String> getClTv() {
        return clTv;
    }

    public DefaultTableModel getModel() {
        return model;
    }

}
