package org.ttair.dataaccess;

import com.primesense.nite.NiTE;
import java.io.Serializable;
import java.util.ArrayList;
import org.openni.DeviceInfo;
import org.openni.OpenNI;
import org.openni.VideoStream;

/**
 * Class responsible for providing connections to multiple devices that conform
 * to the Standard OpenNI 2.2.
 *
 * This in turn will provide faster access and automated devices, but it will be
 * only one class of internal control connections.
 *
 * @since 2013
 * @author Lucas - Member TTAir Research Group
 * @version 2.0
 */
public class DeviceManager implements Serializable {

	private static final long serialVersionUID = -2581882996799424120L;
	/**
     * This class uses the Singleton designer partner, so that only one instance
     * of it is created in order to ensure proper connection to the devices
     */
    private static final DeviceManager INSTANCE = new DeviceManager();
    private static TTAirDevice[] ttaDevices;
    private static ArrayList<DeviceInfo> dmDevicesInfo;

    /**
     * Private constructor of the class, it is where all the connections to the
     * devices that is initialized.
     */
    private DeviceManager() {
        OpenNI.initialize();
        NiTE.initialize();
        dmDevicesInfo = (ArrayList<DeviceInfo>) OpenNI.enumerateDevices();
        ttaDevices = new TTAirDevice[dmDevicesInfo.size()];
        createDevices(dmDevicesInfo);
    }

    private void createDevices(ArrayList<DeviceInfo> devices) {
        for (int i = 0; i < dmDevicesInfo.size(); i++) {
            ttaDevices[i] = new TTAirDevice(dmDevicesInfo.get(i).getUri());
        }
    }

    public static VideoStream getRGB(int id_sensor) {
        return ttaDevices[id_sensor].getRGB();
    }

    public static VideoStream getDepth(int id_sensor) {
        return ttaDevices[id_sensor].getDepth();
    }

    public static VideoStream getIR(int id_sensor) {
        return ttaDevices[id_sensor].getIR();
    }

    public static String getVendorDevice(int id_sensor) {
        return ttaDevices[id_sensor].getVendor();
    }

    public static String getNameDevice(int id_sensor) {
        return ttaDevices[id_sensor].getName();
    }

    public static String getURIDevice(int id_sensor) {
        return ttaDevices[id_sensor].getURI();
    }

    public static TTAirDevice getTTAirDevice(int id_sensor) {
        return ttaDevices[id_sensor];
    }
    public static TTAirDevice getTTAirDevice() {
        return ttaDevices[0];
    }
    public static ArrayList<DeviceInfo> getDmDevicesInfo() {
        return dmDevicesInfo;
    }

    public static void closeDevices() {
        OpenNI.shutdown();
        NiTE.shutdown();
    }

    public static DeviceManager getINSTANCE() {
        return INSTANCE;
    }
}
