package mq;

import jdbc.AirCompanyDAO;
import jdbc.FlightDAO;
import models.AirCompany;
import models.Flight;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;
import java.util.List;

public class Server {
    private Connection connection;
    private Session session;
    private MessageProducer producer;
    private MessageConsumer consumer;

    private static final String separator = "#";

    public void start() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        try {
            connection = factory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Destination queueTo = session.createQueue("toClient");
            Destination queueFrom = session.createQueue("fromClient");

            producer = session.createProducer(queueTo);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            consumer = session.createConsumer(queueFrom);

            while (processQuery()) {

            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    private boolean processQuery() {
        String response = "";
        String query = "";
        try {
            Message request = consumer.receive(500);
            if (request == null) {
                return true;
            }

            if (request instanceof TextMessage) {
                TextMessage message = (TextMessage) request;
                query = message.getText();
            } else { return true; }

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
                        TextMessage message = session.createTextMessage(response);
                        producer.send(message);
                        break;
                    case "FlightFindByAirCompanyId":
                        id = Long.parseLong(fields[1]);
                        List<Flight> list = FlightDAO.findByAirCompanyId(id);
                        StringBuilder str = new StringBuilder();
                        assert list != null;
                        flightsToString(str, list);
                        response = str.toString();
                        message = session.createTextMessage(response);
                        producer.send(message);
                        break;
                    case "AirCompanyFindByName":
                        String name = fields[1];
                        airCompany = AirCompanyDAO.findByName(name);
                        assert airCompany != null;
                        response = airCompany.getId() + "";
                        message = session.createTextMessage(response);
                        producer.send(message);
                        break;
                    case "FlightUpdate":
                        id = Long.parseLong(fields[1]);
                        String cityFrom = fields[2];
                        String cityTo = fields[3];
                        Integer passengersAmount = Integer.parseInt(fields[4]);
                        Long AirCompanyId = Long.parseLong(fields[5]);
                        flight = new Flight(id, cityFrom, cityTo, passengersAmount, AirCompanyId);
                        response = FlightDAO.update(flight) ? "true" : "false";
                        System.out.println(response);
                        message = session.createTextMessage(response);
                        producer.send(message);
                        break;
                    case "AirCompanyUpdate":
                        id = Long.parseLong(fields[1]);
                        name = fields[2];
                        airCompany = new AirCompany(id, name);
                        response = AirCompanyDAO.update(airCompany) ? "true" : "false";
                        message = session.createTextMessage(response);
                        producer.send(message);
                        break;
                    case "FlightInsert":
                        id = Long.parseLong(fields[1]);
                        cityFrom = fields[2];
                        cityTo = fields[3];
                        passengersAmount = Integer.parseInt(fields[4]);
                        AirCompanyId = Long.parseLong(fields[5]);
                        flight = new Flight(id, cityFrom, cityTo, passengersAmount, AirCompanyId);
                        response = FlightDAO.insert(flight) ? "true" : "false";
                        message = session.createTextMessage(response);
                        producer.send(message);
                        break;
                    case "AirCompanyInsert":
                        name = fields[1];
                        airCompany = new AirCompany();
                        airCompany.setName(name);
                        response = AirCompanyDAO.insert(airCompany) ? "true" : "false";
                        message = session.createTextMessage(response);
                        producer.send(message);
                        break;
                    case "FlightDelete":
                        id = Long.parseLong(fields[1]);
                        flight = new Flight();
                        flight.setId(id);
                        response = FlightDAO.delete(flight) ? "true" : "false";
                        message = session.createTextMessage(response);
                        producer.send(message);
                        break;
                    case "AirCompanyDelete":
                        id = Long.parseLong(fields[1]);
                        airCompany = new AirCompany();
                        airCompany.setId(id);
                        response = AirCompanyDAO.delete(airCompany) ? "true" : "false";
                        message = session.createTextMessage(response);
                        producer.send(message);
                        break;
                    case "FlightAll":
                        List<Flight> FlightsList = FlightDAO.findAll();
                        str = new StringBuilder();
                        assert FlightsList != null;
                        flightsToString(str, FlightsList);
                        response = str.toString();
                        message = session.createTextMessage(response);
                        producer.send(message);
                        break;
                    case "AirCompanyAll":
                        List<AirCompany> airCompaniesList = AirCompanyDAO.findAll();
                        str = new StringBuilder();
                        assert airCompaniesList != null;
                        for (AirCompany company : airCompaniesList) {
                            str.append(company.getId());
                            str.append(separator);
                            str.append(company.getName());
                            str.append(separator);
                        }
                        response = str.toString();
                        message = session.createTextMessage(response);
                        producer.send(message);
                        break;
                }
            }
            return true;
        } catch (JMSException ex) {
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

    public void disconnect() {
        try {
            session.close();
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
}
