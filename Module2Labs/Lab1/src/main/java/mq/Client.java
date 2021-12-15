package mq;

import models.AirCompany;
import models.Flight;

import java.io.IOException;
import java.util.ArrayList;
import javax.jms.*;
import java.util.List;

public class Client {

    private Connection connection;
    private Session session;
    private MessageProducer producer;
    private MessageConsumer consumer;
    private static final String separator = "#";

    public Client() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        try {
            connection = factory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Destination queueOut = session.createQueue("fromClient");
            Destination queueIn = session.createQueue("toClient");

            producer = session.createProducer(queueOut);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            consumer = session.createConsumer(queueIn);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    private String handleMessage(String query, int timeout) throws JMSException {
        TextMessage message = session.createTextMessage(query);
        producer.send(message);
        Message mes = consumer.receive(timeout);
        if (mes == null) {
            return null;
        }

        if (mes instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) mes;
            return textMessage.getText();
        }

        return "";
    }

    public AirCompany airCompanyFindById(Long id) {
        String query = "AirCompanyFindById" + separator + id.toString();
        try {
            String response = handleMessage(query, 15000);
            return new AirCompany(id, response);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return null;
    }

    public AirCompany airCompanyFindByName(String name) {
        String query = "AirCompanyFindByName" + separator + name;
        try {
            String response = handleMessage(query, 15000);
            Long responseId = Long.parseLong(response);
            return new AirCompany(responseId, name);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean flightUpdate(Flight flight) {
        String query = "FlightUpdate" + separator + flight.getId() + separator + flight.getCityFrom() + separator + flight.getCityFrom()
                + separator + flight.getPassengersAmount() + separator + flight.getCompanyId();
        try {
            String response = handleMessage(query, 15000);
            return "true".equals(response);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean airCompanyUpdate(AirCompany airCompany) {
        String query = "AirCompanyUpdate" + separator + airCompany.getId() +
                separator + airCompany.getName();
        try {
            String response = handleMessage(query, 15000);
            return "true".equals(response);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean flightInsert(Flight flight) {
        String query = "FlightInsert" +
                separator +  flight.getId() + separator + flight.getCityFrom() + separator + flight.getCityFrom()
                + separator + flight.getPassengersAmount() + separator + flight.getCompanyId();
        try {
            String response = handleMessage(query, 15000);
            return "true".equals(response);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean airCompanyInsert(AirCompany airCompany) {
        String query = "AirCompanyInsert" +
                separator + airCompany.getName();
        try {
            String response = handleMessage(query, 15000);
            return "true".equals(response);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean airCompanyDelete(AirCompany airCompany) {
        String query = "AirCompanyDelete" + separator + airCompany.getId();
        try {
            String response = handleMessage(query, 15000);
            return "true".equals(response);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean flightDelete(Flight flight) {
        String query = "FlightDelete" + separator + flight.getId();
        try {
            String response = handleMessage(query, 15000);
            return "true".equals(response);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<AirCompany> airCompanyAll() {
        String query = "AirCompanyAll";
        List<AirCompany> list = new ArrayList<>();
        try {
            String response = handleMessage(query, 15000);
            String[] fields = response.split(separator);
            for (int i = 0; i < fields.length; i += 2) {
                Long id = Long.parseLong(fields[i]);
                String name = fields[i + 1];
                list.add(new AirCompany(id, name));
            }
            return list;
        } catch (JMSException e) {
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
        List<Flight> list = new ArrayList<>();
        try {
            String response = handleMessage(query, 15000);
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
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void cleanMessages() {
        try {
            Message message = consumer.receiveNoWait();
            while (message!=null) {
                message = consumer.receiveNoWait();
            }
        } catch (JMSException e) {
            e.printStackTrace();
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
}
