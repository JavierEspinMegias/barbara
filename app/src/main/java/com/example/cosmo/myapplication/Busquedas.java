package com.example.cosmo.myapplication;

import org.json.JSONException;
import org.json.JSONObject;

public class Busquedas {
    //(codBus int primary key, email text, busqueda text, modo text, clase text, opcion text, link text)
    String emailBus, bus, modoBus, claseBus, opcionBus, linkBus;

    public Busquedas(){
        emailBus=bus=modoBus=claseBus=opcionBus=linkBus="";
    }

    public Busquedas(String emailBus, String bus, String modoBus, String claseBus, String opcionBus, String linkBus) {
        this.emailBus = emailBus;
        this.bus = bus;
        this.modoBus = modoBus;
        this.claseBus = claseBus;
        this.opcionBus = opcionBus;
        this.linkBus = linkBus;
    }

    public String toJSON(){

        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("emailBus", getEmailBus());
            jsonObject.put("bus", getBus());
            jsonObject.put("modoBus", getModoBus());
            jsonObject.put("claseBus", getClaseBus());
            jsonObject.put("opcionBus", getOpcionBus());
            jsonObject.put("linkBus", getLinkBus());

            return jsonObject.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "Fallo de parseo";
        }
    }

    public String toStringList(){
        return this.emailBus+">"+this.bus+">"+this.modoBus+">"+this.claseBus+">"+this.opcionBus+">"+this.linkBus;
    }


    public String getEmailBus() {
        return emailBus;
    }

    public void setEmailBus(String emailBus) {
        this.emailBus = emailBus;
    }

    public String getBus() {
        return bus;
    }

    public void setBus(String bus) {
        this.bus = bus;
    }

    public String getModoBus() {
        return modoBus;
    }

    public void setModoBus(String modoBus) {
        this.modoBus = modoBus;
    }

    public String getClaseBus() {
        return claseBus;
    }

    public void setClaseBus(String claseBus) {
        this.claseBus = claseBus;
    }

    public String getOpcionBus() {
        return opcionBus;
    }

    public void setOpcionBus(String opcionBus) {
        this.opcionBus = opcionBus;
    }

    public String getLinkBus() {
        return linkBus;
    }

    public void setLinkBus(String linkBus) {
        this.linkBus = linkBus;
    }
}
