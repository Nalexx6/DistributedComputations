package tcp_ip;

import jdbc.AirCompanyDAO;
import jdbc.FlightDAO;
import models.AirCompany;
import models.Flight;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Server {
    private ServerSocket server = null;
    private Socket socket = null;
    private PrintWriter out = null;
    private BufferedReader in = null;
    private static final String separator = "#";

    public void start(int port) throws IOException {
        server = new ServerSocket(port);
        while (true) {
            socket = server.accept();
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            while (processQuery()) ;
        }
    }

    private boolean processQuery() {
        String response;
        try {
            String query = in.readLine();
            if (query == null) {
                return false;
            }

            String [] fields = query.split(separator);
            if (fields.length == 0) {
                return true;
            } else {
                String action = fields[0];
                AirCompany airCompany;
                Flight flight;

                switch (action) {
                    case "AirCompanyFindById":
                        Long id = Long.parseLong(fields[1]);
                        airCompany = AirCompanyDAO.findById(id);
                        response = airCompany.getName();
                        out.println(response);
                        break;
                    case "FlightFindByAirCompanyId":
                        id = Long.parseLong(fields[1]);
                        List<Flight> list = FlightDAO.findByAirCompanyId(id);
                        StringBuilder str = new StringBuilder();
                        assert list != null;
                        flightsToString(str, list);
                        response = str.toString();
                        out.println(response);
                        break;
                    case "AirCompanyFindByName":
                        String name = fields[1];
                        airCompany = AirCompanyDAO.findByName(name);
                        assert airCompany != null;
                        response = airCompany.getId() + "";
                        out.println(response);
                        break;
                    case "FlightUpdate":
                        id = Long.parseLong(fields[1]);
                        String cityFrom = fields[2];
                        String cityTo = fields[3];
                        Integer passengersAmount = Integer.parseInt(fields[4]);
                        Long AirCompanyId = Long.parseLong(fields[5]);
                        flight = new Flight(id, cityFrom, cityTo, passengersAmount, AirCompanyId);
                        if (FlightDAO.update(flight))
                            response = "true";
                        else
                            response = "false";
                        System.out.println(response);
                        out.println(response);
                        break;
                    case "AirCompanyUpdate":
                        id = Long.parseLong(fields[1]);
                        name = fields[2];
                        airCompany = new AirCompany(id, name);
                        if (AirCompanyDAO.update(airCompany)) {
                            response = "true";
                        } else {
                            response = "false";
                        }
                        out.println(response);
                        break;
                    case "FlightInsert":
                        cityFrom = fields[2];
                        cityTo = fields[3];
                        passengersAmount = Integer.parseInt(fields[4]);
                        AirCompanyId = Long.parseLong(fields[5]);
                        flight = new Flight(0,  cityFrom, cityTo, passengersAmount, AirCompanyId);
                        if (FlightDAO.insert(flight)) {
                            response = "true";
                        } else {
                            response = "false";
                        }
                        out.println(response);
                        break;
                    case "AirCompanyInsert":
                        name = fields[1];
                        airCompany = new AirCompany();
                        airCompany.setName(name);
                        if (AirCompanyDAO.insert(airCompany)) {
                            response = "true";
                        } else {
                            response = "false";
                        }
                        out.println(response);
                        break;
                    case "FlightDelete":
                        id = Long.parseLong(fields[1]);
                        flight = new Flight();
                        flight.setId(id);
                        if (FlightDAO.delete(flight)) {
                            response = "true";
                        } else {
                            response = "false";
                        }
                        out.println(response);
                        break;
                    case "AirCompanyDelete":
                        id = Long.parseLong(fields[1]);
                        airCompany = new AirCompany();
                        airCompany.setId(id);
                        if (AirCompanyDAO.delete(airCompany)) {
                            response = "true";
                        } else {
                            response = "false";
                        }
                        out.println(response);
                        break;
                    case "FlightAll":
                        List<Flight> FlightsList = FlightDAO.findAll();
                        str = new StringBuilder();
                        assert FlightsList != null;
                        flightsToString(str, FlightsList);
                        response = str.toString();
                        out.println(response);
                        break;
                    case "AirCompanyAll":
                        List<AirCompany> airCompaniesList = AirCompanyDAO.findAll();
                        str = new StringBuilder();
                        for (AirCompany company : airCompaniesList) {
                            str.append(company.getId());
                            str.append(separator);
                            str.append(company.getName());
                            str.append(separator);
                        }
                        response = str.toString();
                        out.println(response);
                        break;
                }
            }
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    private void flightsToString(StringBuilder str, List<Flight> list) {
        for (Flight flight : list) {
            str.append(flight.getId());
            str.append(separator);
            str.append(flight.getCityFrom());
            str.append(separator);
            str.append(flight.getCityTo());
            str.append(separator);
            str.append(flight.getPassengersAmount());
            str.append(separator);
            str.append(flight.getCompanyId());
            str.append(separator);
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        try {
            server.start(5433);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
