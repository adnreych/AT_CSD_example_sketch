package daemoncsd;

import exceptions.CSDException;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class Test {

    private static SerialPort serialPort;
    static CSDResponsePayloadParser csdResponsePayloadParser;

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
                    	serialPort.writeString("ATD89805400316" + "\r");
					}
                    if (data.length() > 14 && data.substring(2, 14).equals("CONNECT 9600")) {
                    	int startAddress = 0x0011;
                    	int count = 3;
                    	String commandNumber = "3";
                    	Byte[] arr = new Byte[] {0 , (byte) startAddress, (byte) count};
                    	CSDCommand csdCommand = new CSDCommand(commandNumber.getBytes(), arr); 
                    	
                    	if (serialPort.removeEventListener()) {
                    		serialPort.addEventListener(new CSDPortReader(), SerialPort.MASK_RXCHAR);
                        	serialPort.writeBytes(csdCommand.getCommand());
                        	csdResponsePayloadParser = new CSDResponsePayloadParser();
                        	csdResponsePayloadParser.setStartAddress(startAddress);
                        	csdResponsePayloadParser.setCount(count);
                        	csdResponsePayloadParser.setCSDcommandNumber(commandNumber.getBytes());
                        	
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
                	csdResponsePayloadParser.setResponse(data);
                	System.out.println("DataFromCSD" + data);
                	for (int i : csdResponsePayloadParser.parsePayload()) {
                		System.out.println("i:" + i);
					}
                	
                }
                catch (SerialPortException | CSDException ex) {
                    System.out.println(ex);
                }
            }
		} 	
    }
}