package edu.escuelaing.arem;

import static spark.Spark.*;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;

import org.bson.Document;

import edu.escuelaing.arem.persistence.MongoConnection;
import spark.Request;
import spark.Response;

public class App {

    private static final int PORT = 5000;
    private static MongoConnection mongo;

    public static void main(String[] args) throws UnknownHostException {
        port(getPort());
        mongo = new MongoConnection();
        get("/consultlogs", (req, res) -> consult(req, res));
        get("/savelogs", (req, res) -> save(req, res));
    }

    private static String save(Request req, Response res) {
        mongo.add(req.queryParams("message"), new Date());
        return consult(req, res);

    }

    private static String consult(Request req, Response res) {
        res.type("application/json");
        String datos[] = null;
        ArrayList<Document> list = mongo.consult();
        int cont = 1;
        if (list.size() <= 10) {
            datos = new String[list.size()];
            for (Document d : list) {
                datos[cont - 1] = String.valueOf(cont) + "-" +
                        d.get("message").toString() + "-" +
                        d.get("date").toString();
                cont++;
            }
        } else {
            datos = new String[10];
            for (int i = list.size() - 10; i < list.size(); i++) {
                datos[cont - 1] = String.valueOf(cont) + "-" +
                        (list.get(i)).get("message").toString() + "-" +
                        (list.get(i)).get("date").toString();
                cont++;
            }
        }
        return String.join(",", datos);
    }

    private static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return PORT;
    }
}