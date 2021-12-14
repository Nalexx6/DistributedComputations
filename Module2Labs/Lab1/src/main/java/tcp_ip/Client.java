package tcp_ip;

import models.AirCompany;
import models.Flight;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client {
    private final Socket socket;
    private final PrintWriter out;
    private final BufferedReader in;
    private static final String separator = "#";

    public Client(String ip, int port) throws IOException {
        socket = new Socket(ip, port);
        in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    public AirCompany airCompanyFindById(Long id) {
        String query = "AirCompanyFindById" + separator + id.toString();
        out.println(query);
        String response;
        try {
            response = in.readLine();
            return new AirCompany(id, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Flight flightFindByCityFrom(String cityFrom) {
        String query = "FlightFindByCityFrom" + separator + cityFrom;
        out.println(query);
        String response = "";
        try {
            response = in.readLine();
            String[] fields = response.split(separator);
            long id = Long.parseLong(fields[0]);
            String cityTo = fields[3];
            Integer passengersAmount = Integer.parseInt(fields[4]);
            long AirCompanyId = Long.parseLong(fields[5]);
            return new Flight(id,  cityFrom, cityTo, passengersAmount, AirCompanyId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public AirCompany airCompanyFindByName(String name) {
        String query = "AirCompanyFindByName" + separator + name;
        out.println(query);
        try {
            Long response = Long.parseLong(in.readLine());
            return new AirCompany(response, name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean flightUpdate(Flight Flight) {
        String query = "FlightUpdate" + separator + Flight.getId() + separator + Flight.getCityFrom() + separator + Flight.getCityFrom()
                + separator + Flight.getPassengersAmount() + separator + Flight.getCompanyId();
        out.println(query);
        try {
            String response = in.readLine();
            return "true".equals(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean airCompanyUpdate(AirCompany AirCompany) {
        String query = "AirCompanyUpdate" + separator + AirCompany.getId() +
                separator + AirCompany.getName();
        out.println(query);
        try {
            String response = in.readLine();
            return "true".equals(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean flightInsert(Flight Flight) {
        String query = "FlightInsert" +
                separator +  Flight.getId() + separator + Flight.getCityFrom() + separator + Flight.getCityFrom()
                + separator + Flight.getPassengersAmount() + separator + Flight.getCompanyId();
        out.println(query);
        try {
            String response = in.readLine();
            return "true".equals(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean airCompanyInsert(AirCompany AirCompany) {
        String query = "AirCompanyInsert" +
                separator + AirCompany.getName();
        out.println(query);
        try {
            String response = in.readLine();
            return "true".equals(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean airCompanyDelete(AirCompany AirCompany) {
        String query = "AirCompanyDelete" + separator + AirCompany.getId();
        out.println(query);
        try {
            String response = in.readLine();
            return "true".equals(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean flightDelete(Flight Flight) {
        String query = "FlightDelete" + separator + Flight.getId();
        out.println(query);
        try {
            String response = in.readLine();
            return "true".equals(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<AirCompany> airCompanyAll() {
        String query = "AirCompanyAll";
        out.println(query);
        List<AirCompany> list = new ArrayList<>();
        try {
            String response = in.readLine();
            String[] fields = response.split(separator);
            for (int i = 0; i < fields.length; i += 2) {
                Long id = Long.parseLong(fields[i]);
                String name = fields[i + 1];
                list.add(new AirCompany(id, name));
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Flight> flightAll() {
        String query = "FlightAll";
        return getFlights(query);
    }

    public List<Flight> flightFindByAirCompanyId(Long AirCompanyId) {
        String query = "FlightFindByAirCompanyId" + separator + AirCompanyId.toString();
        return getFlights(query);
    }

    private List<Flight> getFlights(String query) {
        out.println(query);
        List<Flight> list = new ArrayList<>();
        try {
            String response = in.readLine();
            String[] fields = response.split(separator);
            for (int i = 0; i < fields.length; i += 5) {
                long id = Long.parseLong(fields[i]);
                String cityFrom = fields[i + 1];
                String cityTo = fields[i + 2];
                Integer passengersAmount = Integer.parseInt(fields[i + 3]);
                long AirCompanyId = Long.parseLong(fields[i + 4]);
                list.add(new Flight(id,  cityFrom, cityTo, passengersAmount, AirCompanyId));
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void disconnect() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}