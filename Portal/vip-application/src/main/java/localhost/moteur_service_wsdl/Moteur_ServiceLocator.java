/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL-B
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info". 
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability. 
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or 
 * data to be ensured and,  more generally, to use and operate it in the 
 * same conditions as regards security. 
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package localhost.moteur_service_wsdl;

public class Moteur_ServiceLocator extends org.apache.axis.client.Service implements localhost.moteur_service_wsdl.Moteur_Service {

    /**
     * gSOAP 2.7.13 generated service definition
     */
    public Moteur_ServiceLocator() {
    }

    public Moteur_ServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public Moteur_ServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    public Moteur_ServiceLocator(String address, org.apache.axis.EngineConfiguration config) {
        super(config);
        moteur_service_address = address;
    }
    // Use to get a proxy class for moteur_service
    private java.lang.String moteur_service_address = "http://localhost:18000";

    public java.lang.String getmoteur_serviceAddress() {
        return moteur_service_address;
    }
    // The WSDD service name defaults to the port name.
    private java.lang.String moteur_serviceWSDDServiceName = "moteur_service";

    public java.lang.String getmoteur_serviceWSDDServiceName() {
        return moteur_serviceWSDDServiceName;
    }

    public void setmoteur_serviceWSDDServiceName(java.lang.String name) {
        moteur_serviceWSDDServiceName = name;
    }

    public localhost.moteur_service_wsdl.Moteur_servicePortType getmoteur_service() throws javax.xml.rpc.ServiceException {
        java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(moteur_service_address);
        } catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getmoteur_service(endpoint);
    }

    public localhost.moteur_service_wsdl.Moteur_servicePortType getmoteur_service(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            localhost.moteur_service_wsdl.Moteur_BindingStub _stub = new localhost.moteur_service_wsdl.Moteur_BindingStub(portAddress, this);
            _stub.setPortName(getmoteur_serviceWSDDServiceName());
            return _stub;
        } catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setmoteur_serviceEndpointAddress(java.lang.String address) {
        moteur_service_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (localhost.moteur_service_wsdl.Moteur_servicePortType.class.isAssignableFrom(serviceEndpointInterface)) {
                localhost.moteur_service_wsdl.Moteur_BindingStub _stub = new localhost.moteur_service_wsdl.Moteur_BindingStub(new java.net.URL(moteur_service_address), this);
                _stub.setPortName(getmoteur_serviceWSDDServiceName());
                return _stub;
            }
        } catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("moteur_service".equals(inputPortName)) {
            return getmoteur_service();
        } else {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("moteur_service/moteur_service.wsdl", "moteur_service");
    }
    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("moteur_service/moteur_service.wsdl", "moteur_service"));
        }
        return ports.iterator();
    }

    /**
     * Set the endpoint address for the specified port name.
     */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {

        if ("moteur_service".equals(portName)) {
            setmoteur_serviceEndpointAddress(address);
        } else { // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
     * Set the endpoint address for the specified port name.
     */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }
}
