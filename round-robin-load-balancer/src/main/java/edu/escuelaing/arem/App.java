package edu.escuelaing.arem;

import static spark.Spark.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

import spark.Request;
import spark.Response;

public class App {

    private static final String URL_STRING = "ec2-100-24-59-5.compute-1.amazonaws.com";
    private static final int PORT = 4000;
    private static int ports[] = { 8001, 8002, 8003 };
    private static int selected = 0;

    public static void main(String[] args) throws UnknownHostException {
        port(getPort());
        get("/", (req, res) -> inputView(req, res));
        post("/", (req, res) -> register(req, res));
    }

    private static String inputView(Request req, Response res) {
        res.type("text/html");
        String server = "Esta actualmente en el LogService #" + String.valueOf(selected + 1);
        String logsToTable = "";
        String view = "";
        try {
            URL url = new URL("http://" + URL_STRING + ":" + String.valueOf(ports[selected])
                    + "/consultlogs");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String inputLine;
            String datos = "";
            while ((inputLine = reader.readLine()) != null) {
                System.out.println(inputLine);
                datos += inputLine;
            }
            String logs[] = datos.split(",");
            logsToTable = "";
            changePortBalance();
            for (String log : logs) {
                System.out.println("log  " + log);
                String info[] = log.split("-");
                logsToTable += "<tr><td>" + info[0] + "</td><td>" + info[1] + "</td><td>" + info[2] + "</td></tr>";

            }

        } catch (MalformedURLException e) {
            // Nothing to do
        } catch (IOException e) {
            // Nothing to do
        }
        view = "<!DOCTYPE html>"
                + "<html>"
                + "<body style=\"background-color:#32CD32;\">"
                + "<style>"
                + "table, th, td {"
                + "border: 1px solid black;"
                + "border-collapse: collapse;"
                + "}"
                + "</style>"
                + "<center>"
                + "<h1>Lista de Logs</h1>"
                + "<br/>"
                + "<h2>" + server + "</h2>"
                + "<form name='loginForm' method='post' action='/'>"
                + "Log: <input type='text' name='message'/> <br/>"
                + "<br/>"
                + "<input type='submit' value='submit' />"
                + "</form>"
                + "<br/>"
                + "<Table>"
                + "<tr>"
                + "<th>No</th>"
                + "<th>Message</th>"
                + "<th>Date</th>"
                + "</tr>"
                + logsToTable
                + "</Table>"
                + "</center>"
                + "</body>"
                + "</html>";
        return view;
    }

    private static String register(Request req, Response res) {
        try {
            URL url = new URL("http://"
                    + URL_STRING + ":" + String.valueOf(ports[selected])
                    + "/savelogs?message=" + (req.queryParams("message").replace(' ', '_')));
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            changePortBalance();
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                System.out.println(inputLine);
            }
        } catch (MalformedURLException e) {
            // Nothing to do
        } catch (IOException e) {
            // Nothing to do
        }
        return inputView(req, res);

    }

    private static void changePortBalance() {
        if (selected < 2) {
            selected += 1;
        } else {
            selected = 0;
        }
    }

    private static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return PORT;
    }
}
