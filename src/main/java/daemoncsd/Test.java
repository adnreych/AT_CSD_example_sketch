package daemoncsd;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class Test {

    private static SerialPort serialPort;

    public static void main(String[] args) {
  
        serialPort = new SerialPort("/dev/ttyUSB0");
        try {
        	serialPort.openPort();
            serialPort.setParams(SerialPort.BAUDRATE_19200,
                                 SerialPort.DATABITS_8,
                                 SerialPort.STOPBITS_1,
                                 SerialPort.PARITY_NONE);
            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN | 
                                          SerialPort.FLOWCONTROL_RTSCTS_OUT);
            serialPort.addEventListener(new ATPortReader(), SerialPort.MASK_RXCHAR);
            serialPort.writeString("AT" + "\r");
            
            
        }
        catch (SerialPortException ex) {
            System.out.println(ex);
        } 
    }
    
    
    
    

    private static class ATPortReader implements SerialPortEventListener {
    	
		public void serialEvent(SerialPortEvent event) {
            if(event.isRXCHAR() && event.getEventValue() > 0)	{
                try {
                	
                    String data = serialPort.readString(event.getEventValue());
                    System.out.println("Data" + data);
                   
                    if (data.length() > 4 && data.substring(2, 4).equals("OK")) {
                    	serialPort.writeString("SET_HERE_PHONENUMBER_REMOTEDEVICE" + "\r");
					}
                    if (data.length() > 14 && data.substring(2, 14).equals("CONNECT 9600")) {
                    	Byte[] arr = new Byte[] {0 ,2, 1};
                    	CSDCommand csdCommand = new CSDCommand("3".getBytes()[0], arr); 
                    	
                    	if (serialPort.removeEventListener()) {
                    		serialPort.addEventListener(new CSDPortReader(), SerialPort.MASK_RXCHAR);
                        	serialPort.writeBytes(csdCommand.getCommand());
                    	}
                    }
                }
                catch (SerialPortException ex) {
                    System.out.println(ex);
                }
            }
		} 	
    }
    
    private static class CSDPortReader implements SerialPortEventListener {
    	
		public void serialEvent(SerialPortEvent event) {
            if(event.isRXCHAR() && event.getEventValue() > 0)	{
                try {
                	String data = serialPort.readHexString(event.getEventValue());
                	System.out.println("DataFromCSD" + data);
                }
                catch (SerialPortException ex) {
                    System.out.println(ex);
                }
            }
		} 	
    }
}