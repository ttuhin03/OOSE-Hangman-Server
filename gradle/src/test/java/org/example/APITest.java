package org.example;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.example.PostTesten.PostClass;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Hier wird die API-Schnittstelle getestet.
 */
public class APITest {
    static String link = "http://localhost:4567/";
    PostClass posten = new PostClass();
    // neuen Nutzer fuer ein paar Tests anlegen
    String neuerNutzerRequest = posten.doPostRequest(link + "games/hangman/start/neuerNutzer", "{ 'name': '" + "TestNutzer" + "'}");
    String neuerNutzerRequest2 = posten.doPostRequest(link + "games/hangman/start/neuerNutzer", "{ 'name': '" + "TestNutzer2" + "'}");
    String neuerNutzerRequest3 = posten.doPostRequest(link + "games/hangman/start/neuerNutzer", "{ 'name': '" + "TestNutzer3" + "'}");
    static String args[];


    public APITest() throws IOException {
    }
     @BeforeAll
        public static void startServer() throws IOException {
         Main.main(args);
     }

    @Test
    void neuerNutzer() throws IOException {
        String antwortServer = posten.doPostRequest(link + "games/hangman/start/neuerNutzer", "{ 'name': '" + "coolerName" + "'}"); //Namen für Nutzerliste an Server schicken
        System.out.println(antwortServer);
        assertTrue(antwortServer.contains("true")); //Wurde der neue Nutzer erfolgreich angelegt?
    }

    @Test
    void neuerNutzerGleicherName() throws IOException {
        String antwortServer = posten.doPostRequest(link + "games/hangman/start/neuerNutzer", "{ 'name': '" + "coolerName" + "'}"); //Namen für Nutzerliste an Server schicken
        System.out.println(antwortServer);
        assertTrue(antwortServer.contains("false")); //Wurde der neue Nutzer nicht angelegt, da der Username bereits existiert?
    }

    @Test
    void istEinPoolda() throws IOException {
        String antwort = posten.doPostRequest(link + "games/hangman/start/poolSuchen/", "pools angefragt");
        System.out.println(antwort);
        String[] antwortSplit = antwort.split("Vorhanden: ");
        assertTrue(antwortSplit[1].contains("false"));   //es sind bereits Pools im Server, weshalb mit false geantwortet wird

    }

    @Test
    void neuerPool() throws IOException {
        String antwort = posten.doPostRequest(link + "games/hangman/start/neuerPool/", "{ 'name': '" + "TestNutzer" + "','pool': '" + "42" + "','level': '" + 1 + "'}");  //neuen Postrequest mit Eingabe an Server
        System.out.println(antwort);
        boolean antwort2 = Boolean.parseBoolean(antwort);
        assertTrue(antwort2);
    }

    @Test
    void neuerPoolGleicheId() throws IOException {
        posten.doPostRequest(link + "games/hangman/start/neuerPool/", "{ 'name': '" + "TestNutzer" + "','pool': '" + "42" + "','level': '" + 1 + "'}");
        String antwort = posten.doPostRequest(link + "games/hangman/start/neuerPool/", "{ 'name': '" + "TestNutzer" + "','pool': '" + "42" + "','level': '" + 1 + "'}");  //neuen Postrequest mit Eingabe an Server
        boolean antwort2 = Boolean.parseBoolean(antwort);
        assertFalse(antwort2);
    }

    @Test
    void poolLoeschen() throws IOException {
        String antwort = posten.doPostRequest(link + "games/hangman/start/spiel/loeschen", "{ 'poolID': '" + "42" + "'}");  //neuen Postrequest mit Eingabe an Server
        System.out.println(antwort);
        assertEquals("0", antwort);
    }

    @Test
    void poolbeitreten() throws IOException {
        posten.doPostRequest(link + "games/hangman/start/neuerPool/", "{ 'name': '" + "TestNutzer" + "','pool': '" + "100" + "','level': '" + 1 + "'}");
        String antwort = posten.doPostRequest(link + "games/hangman/start/beitreten/", "{ 'name': '" + "TestNutzer2" + "','pool': '" + "100" + "'}");
        System.out.println(antwort);
        assertTrue(antwort.contains("true"));
    }

    @Test
    void poolbeitreten_mindestanzahl_ueberschritten() throws IOException {
        String antwort = posten.doPostRequest(link + "games/hangman/start/beitreten/", "{ 'name': '" + "TestNutzer3" + "','pool': '" + "100" + "'}");
        System.out.println(antwort);
        assertTrue(antwort.contains("false"));
    }

    @Test
    void poolWarteraum() throws IOException {
        String antwort = posten.doPostRequest(link + "games/hangman/start/pool/warteRaum", "{ 'poolID':" + "100" + " }");
        System.out.println(antwort);
        assertTrue(antwort.contains("true"));
    }


    @Test
    void buchstabeRaten() throws IOException {
        Pool pool = new Pool(new Nutzer("megaName"), 2, 1);
        pool.spiel.geheimesWort = "testWort";
        String antwort = posten.doPostRequest(link + "games/hangman/start/neuesWort/" + 0, "{ 'name': '" + "megaName" + "','pool': '" + 1 + "','zeichen': '" + 'e' + "'}");  //neuen Postrequest mit Eingabe an Server
        JsonObject jObj = new Gson().fromJson(antwort, JsonObject.class);
        String rateVersuch = jObj.get("rateVersuch").toString();
        rateVersuch = rateVersuch.replace("\"", "");
        boolean antwort2 = Boolean.parseBoolean(rateVersuch);
        System.out.println(antwort2);
        assertTrue(antwort2);
    }
}
