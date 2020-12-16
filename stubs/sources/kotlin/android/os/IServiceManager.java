package android.os;

public interface IServiceManager extends IInterface
{
    /**
     * Retrieve an existing service called @a name from the
     * service manager.  Blocks for a few seconds waiting for it to be
     * published if it does not already exist.
     */
    IBinder getService(String name) throws RemoteException;
    
    /**
     * Retrieve an existing service called @a name from the
     * service manager.  Non-blocking.
     */
    IBinder checkService(String name) throws RemoteException;
    /**
     * Place a new @a service called @a name into the service
     * manager.
     */
    void addService(String name, IBinder service, boolean allowIsolated)
                throws RemoteException;
    /**
     * Return a list of all currently running services.
     */
    String[] listServices() throws RemoteException;
    
    String descriptor = "android.os.IServiceManager";
    int GET_SERVICE_TRANSACTION = IBinder.FIRST_CALL_TRANSACTION;
    int CHECK_SERVICE_TRANSACTION = IBinder.FIRST_CALL_TRANSACTION+1;
    int ADD_SERVICE_TRANSACTION = IBinder.FIRST_CALL_TRANSACTION+2;
    int LIST_SERVICES_TRANSACTION = IBinder.FIRST_CALL_TRANSACTION+3;
    int CHECK_SERVICES_TRANSACTION = IBinder.FIRST_CALL_TRANSACTION+4;
    int SET_PERMISSION_CONTROLLER_TRANSACTION = IBinder.FIRST_CALL_TRANSACTION+5;
}