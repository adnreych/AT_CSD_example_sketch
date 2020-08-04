package daemoncsd;

import java.util.Arrays;
import java.util.List;

public class CSDResponsePayloadParser {
	
	public CSDResponsePayloadParser() {}
	
	private int startAddress;
	private int count;
	private byte[] CSDcommandNumber;
	private String response;
	
	public int[] parsePayload() {
		System.out.println("CSDResponsePayloadParser" + this.toString());
		String command = new String(CSDcommandNumber);
		System.out.println("command" + command);
		switch (command) {
		case "3":
		case "7":
		case "17":
			return parseReadResponse();

		default:
			break;
		}
		return null;
	}
	
	private int[] parseReadResponse() {
		int[] result = new int[count];
		List<String> res = Arrays.asList(response.split(" "));
		System.out.println("resBefore" + res.toString());
		res = res.subList(3 + CSDcommandNumber.length, res.size() - 5);  // remove "CSD" in begin and "{CRC}ENDD" in end
		System.out.println("resAfter" + res.toString());
		for(int i = 0; i < res.size(); i = i + 2) {
			String currString = res.get(i) + res.get(i+1);
			result[i/2] = Integer.parseInt(currString, 16);
			System.out.println("currString" + currString + "|" + result[i/2] + "|" + "i=" + i);
		}
		return result;
	}
	
	private void parseError() {
		
	}

	public int getStartAddress() {
		return startAddress;
	}

	public void setStartAddress(int startAddress) {
		this.startAddress = startAddress;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public byte[] getCSDcommandNumber() {
		return CSDcommandNumber;
	}

	public void setCSDcommandNumber(byte[] cSDcommandNumber) {
		CSDcommandNumber = cSDcommandNumber;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	@Override
	public String toString() {
		return "CSDResponsePayloadParser [startAddress=" + startAddress + ", count=" + count + ", CSDcommandNumber="
				+ Arrays.toString(CSDcommandNumber) + ", response=" + response + "]";
	}
	
	
	
	
	

	
	
	

}
