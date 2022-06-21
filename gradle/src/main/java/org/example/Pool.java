package org.example;

import java.util.ArrayList;

public class Pool {
    Game spiel; //zugehoeriges Spiel
    ArrayList<Nutzer> mitglieder = new ArrayList<>();
    int level;


    //Nutzer eröffnet Pool unter Angabe von Schwierigkeit
    public Pool(Nutzer initiator, int level){
        this.level = level;
       this.spiel = new Game(level);  //Neues Spiel mit richtigem Schwierigkeitsgrad erschaffen
        Main.poolListe.add(this); //Zur Poolliste hinzufügen
    }
    public void newGame(){
        this.spiel = new Game(level);  //neues Spiel mit Pool assoziieren
    }


    public boolean startGame(){
        if(mitglieder.size()>=2){
            this.spiel.start(mitglieder);  //Neues Spiel starten
            return true;
        }
        else {
            System.out.println("Nicht genug Menschen da");
            return false;
        }
    }


    public String toString(){
        return "Nummer: " + Main.poolListe.indexOf(this) + ", Schwierigkeit: " + level;
    }
}