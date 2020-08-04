package daemoncsd;

import com.google.common.collect.ObjectArrays;

public class CSDCommand {
	
	private Byte CSDcommandNumber;
	private Byte[] CSDcommandPayload;
	private Byte[] CMD_COMMAND_PART;
    private final Byte[] END_PART = {'E', 'N', 'D', 0x0D};
    private Byte CRC;
    
    
    
	public CSDCommand(Byte CSDcommandNumber, Byte[] CSDcommandPayload) {
		this.CSDcommandNumber = CSDcommandNumber;
		this.CSDcommandPayload = CSDcommandPayload;
		CMD_COMMAND_PART = new Byte[] {'C', 'M', 'D', CSDcommandNumber};
		this.CRC = getCRC();
	}
	
	
	private Byte getCRC() {
		Byte summ = 0;
		Byte[] arrForCRC = ObjectArrays.concat(CMD_COMMAND_PART, CSDcommandPayload , Byte.class);
		for (Byte b : arrForCRC) {
			//System.out.println("b: " + b);
			summ = (byte) (summ + b);
		}
		//System.out.println("CRC: " + summ);
		return summ;
	}
	
	public byte[] getCommand() {
		Byte[] swap;
		swap = ObjectArrays.concat(CMD_COMMAND_PART, CSDcommandPayload , Byte.class);
		swap = ObjectArrays.concat(swap, CRC);
		swap = ObjectArrays.concat(swap, END_PART, Byte.class);
		byte[] command = new byte[swap.length];
		
		for(int i=0; i < swap.length; i++) {
			//System.out.println("CURR byte: " + swap[i]);
			command[i] = swap[i].byteValue();
		}
		
		return command;
	}


	public Byte getCSDcommandNumber() {
		return CSDcommandNumber;
	}


	public void setCSDcommandNumber(Byte cSDcommandNumber) {
		CSDcommandNumber = cSDcommandNumber;
	}


	public Byte[] getCSDcommandPayload() {
		return CSDcommandPayload;
	}


	public void setCSDcommandPayload(Byte[] cSDcommandPayload) {
		CSDcommandPayload = cSDcommandPayload;
	}
	
    
    
}
