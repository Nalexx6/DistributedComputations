package rmi;

import jdbc.AirCompanyDAO;
import jdbc.FlightDAO;
import models.AirCompany;
import models.Flight;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class RMIServerImpl extends UnicastRemoteObject implements RMIServer {
    protected RMIServerImpl() throws RemoteException{
        super();
    }

    @Override
    public AirCompany airCompanyFindById(long id) throws RemoteException {
        return AirCompanyDAO.findById(id);
    }

    @Override
    public AirCompany airCompanyFindByName(String name) throws RemoteException {
        return AirCompanyDAO.findByName(name);
    }

    @Override
    public boolean airCompanyUpdate(AirCompany airCompany) throws RemoteException {
        return AirCompanyDAO.update(airCompany);
    }

    @Override
    public boolean airCompanyInsert(AirCompany airCompany) throws RemoteException {
        return AirCompanyDAO.insert(airCompany);
    }

    @Override
    public boolean airCompanyDelete(AirCompany airCompany) throws RemoteException {
        return AirCompanyDAO.delete(airCompany);
    }

    @Override
    public List<AirCompany> airCompanyFindAll() throws RemoteException {
        return AirCompanyDAO.findAll();
    }

    @Override
    public Flight flightFindById(long id) throws RemoteException {
        return FlightDAO.findById(id);
    }

    @Override
    public boolean flightUpdate(Flight flight) throws RemoteException {
        return FlightDAO.update(flight);
    }

    @Override
    public boolean flightInsert(Flight flight) throws RemoteException {
        return FlightDAO.insert(flight);
    }

    @Override
    public boolean flightDelete(Flight flight) throws RemoteException {
        return FlightDAO.delete(flight);
    }

    @Override
    public List<Flight> flightFindAll() throws RemoteException {
        return FlightDAO.findAll();
    }

    @Override
    public List<Flight> flightFindByAirCompanyId(Long id) throws RemoteException {
        return FlightDAO.findByAirCompanyId(id);
    }
}
