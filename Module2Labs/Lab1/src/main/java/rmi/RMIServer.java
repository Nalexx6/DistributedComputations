package rmi;

import models.AirCompany;
import models.Flight;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RMIServer extends Remote {
    public AirCompany airCompanyFindById(long id) throws RemoteException;
    public AirCompany airCompanyFindByName(String name) throws RemoteException;
    public boolean airCompanyUpdate(AirCompany airCompany) throws RemoteException;
    public boolean airCompanyInsert(AirCompany airCompany) throws RemoteException;
    public boolean airCompanyDelete(AirCompany airCompany) throws RemoteException;
    public List<AirCompany> airCompanyFindAll() throws RemoteException;
    public Flight flightFindById(long id) throws RemoteException;
    public boolean flightUpdate(Flight flight) throws RemoteException;
    public boolean flightInsert(Flight flight) throws RemoteException;
    public boolean flightDelete(Flight flight) throws RemoteException;
    public List<Flight> flightFindAll() throws RemoteException;
    public List<Flight> flightFindByAirCompanyId(Long id) throws RemoteException;
}
