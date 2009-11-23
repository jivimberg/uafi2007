package usb;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;

/**
 * @author Juan Ignacio
 *
 * 	Note that "input" and "output" refer to the parameter designations in calls
 *	to this function, which are the opposite of common sense from the
 *	perspective of an application making the calls.
 *
 *  Mapping References: 
 *  DWORD -> int
 *  HANDLE, PVOID -> Pointer
 *  PDWORD -> IntByReference
 *  PCHAR -> String
 *  BOOL -> boolean
 *  CHAR -> byte
 *  SHORT -> short
 *  
 *  Note: Unsigned types use the same mappings as signed types.
 */

public interface MPUSBApi extends StdCallLibrary {

	public static final int MPUSB_FAIL = 0;
	public static final int MPUSB_SUCCESS = 1;
	public static final int MP_WRITE = 0;
	public static final int MP_READ = 1;
	public static final int MAX_NUM_MPUSB_DEV = 127;
	
	public static final MPUSBApi INSTANCE = (MPUSBApi) Native.loadLibrary("mpusbapi", MPUSBApi.class);
	
	/**
	 * get mpusbapi.dll revision level
	 * 
	 * @param None
	 * @return 32-bit revision level MMmmddii MM - Major release mm - Minor
	 *         release dd - dot release or minor fix ii - test release revisions
	 */
	int _MPUSBGetDLLVersion();
	
	/**
	 * Returns the number of devices with matching VID & PID
	 * 
	 * @param pVID_PID
	 * @return
	 */
	int _MPUSBGetDeviceCount(char[] pVID_PID);

	/**
	 * Returns the handle to the endpoint pipe with matching VID & PID
	 * 
	 * All pipes are opened with the FILE_FLAG_OVERLAPPED attribute. This allows
	 * MPUSBRead,MPUSBWrite, and MPUSBReadInt to have a time-out value.
	 * 
	 * Note: Time-out value has no meaning for Isochronous pipes.
	 * 
	 * @param instance
	 *            -An instance number of the device to open. Typical usage is to
	 *            call MPUSBGetDeviceCount first to find out how many instances
	 *            there are. It is important to understand that the driver is
	 *            shared among different devices. The number of devices returned
	 *            by MPUSBGetDeviceCount could be equal to or less than the
	 *            number of all the devices that are currently connected & using
	 *            the generic driver.
	 * 
	 *            Example: if there are 3 device with the following PID&VID
	 *            connected: Device Instance 0, VID 0x04d8, PID 0x0001 Device
	 *            Instance 1, VID 0x04d8, PID 0x0002 Device Instance 2, VID
	 *            0x04d8, PID 0x0001
	 * 
	 *            If the device of interest has VID = 0x04d8 and PID = 0x0002
	 *            Then MPUSBGetDeviceCount will only return '1'. The calling
	 *            function should have a mechanism that attempts to call
	 *            MPUSBOpen up to the absolute maximum of MAX_NUM_MPUSB_DEV
	 *            (MAX_NUM_MPUSB_DEV is defined in _mpusbapi.h). It should also
	 *            keep track of the number of successful calls to MPUSBOpen().
	 *            Once the number of successes equals the number returned by
	 *            MPUSBGetDeviceCount, the attempts should be aborted because
	 *            there will no more devices with a matching vid&pid left.
	 * @param pVID_PID
	 *            -A string containing the PID&VID value of the target device.
	 *            The format is "vid_xxxx&pid_yyyy". Where xxxx is the VID value
	 *            in hex and yyyy is the PID value in hex. Example: If a device
	 *            has the VID value of 0x04d8 and PID value of 0x000b, then the
	 *            input string should be: "vid_04d8&pid_000b"
	 * @param pEP
	 *            - A string of the endpoint number on the target endpoint to
	 *            open. The format is "\\MCHP_EPz". Where z is the endpoint
	 *            number in decimal. Example: "\\MCHP_EP1"
	 * 
	 *            This arguement can be NULL. A NULL value should be used to
	 *            create a handles for non-specific endpoint functions.
	 *            MPUSBRead, MPUSBWrite, MPUSBReadInt are endpoint specific
	 *            functions. All others are not. Non-specific endpoint functions
	 *            will become available in the next release of the DLL.
	 * 
	 *            Note: To use MPUSBReadInt(), the format of pEP has to be
	 *            "\\MCHP_EPz_ASYNC". This option is only available for an IN
	 *            interrupt endpoint. A data pipe opened with the "_ASYNC"
	 *            keyword would buffer the data at the interval specified in the
	 *            endpoint descriptor upto the maximum of 100 data sets. Any
	 *            data received after the driver buffer is full will be ignored.
	 *            The user application should call MPUSBReadInt() often enough
	 *            so that the maximum limit of 100 is never reached.
	 * @param dwDir
	 *            - Specifies the direction of the endpoint. Use MP_READ for
	 *            MPUSBRead, MPSUBReadInt Use MP_WRITE for MPUSBWrite
	 * @param dwReserved
	 *            -Future Use
	 * @return Summary of transfer type usage:
	 *         ==================================
	 *         ========================================== Transfer Type
	 *         Functions Time-Out Applicable?
	 *         ====================================
	 *         ======================================== Interrupt - IN
	 *         MPUSBRead, MPUSBReadInt Yes Interrupt - OUT MPUSBWrite Yes Bulk -
	 *         IN MPUSBRead Yes Bulk - OUT MPUSBWrite Yes Isochronous - IN
	 *         MPUSBRead No Isochronous - OUT MPUSBWrite No
	 *         ======================
	 *         ======================================================
	 */
	Pointer _MPUSBOpen(int instance, // Input
			String pVID_PID, // Input
			String pEP, // Input
			int dwDir, // Input
			int dwReserved); // Input <Future Use>

	/**
	 * @param handle
	 *            - Identifies the endpoint pipe to be read. The pipe handle
	 *            must have been created with MP_READ access attribute.
	 * @param pData
	 *            - Points to the buffer that receives the data read from the
	 *            pipe.
	 * @param dwLen
	 *            - Specifies the number of bytes to be read from the pipe.
	 * @param pLength
	 *            - Points to the number of bytes read. MPUSBRead sets this
	 *            value to zero before doing any work or error checking.
	 * @param dwMilliseconds
	 *            - Specifies the time-out interval, in milliseconds. The
	 *            function returns if the interval elapses, even if the
	 *            operation is incomplete. If dwMilliseconds is zero, the
	 *            function tests the data pipe and returns immediately. If
	 *            dwMilliseconds is INFINITE, the function's time-out interval
	 *            never elapses.
	 * @return
	 */
	int _MPUSBRead(Pointer handle, // Input
			Pointer pData, // Output
			int dwLen, // Input
			IntByReference pLength, // Output **Check mapping**
			int dwMilliseconds); // Input

	/**
	 * @param handle
	 *            - Identifies the endpoint pipe to be written to. The pipe
	 *            handle must have been created with MP_WRITE access attribute.
	 * @param pData
	 *            - Points to the buffer containing the data to be written to
	 *            the pipe.
	 * @param dwLen
	 *            - Specifies the number of bytes to write to the pipe.
	 * @param pLength
	 *            - Points to the number of bytes written by this function call.
	 *            MPUSBWrite sets this value to zero before doing any work or
	 *            error checking.
	 * @param dwMilliseconds
	 *            - Specifies the time-out interval, in milliseconds. The
	 *            function returns if the interval elapses, even if the
	 *            operation is incomplete. If dwMilliseconds is zero, the
	 *            function tests the data pipe and returns immediately. If
	 *            dwMilliseconds is INFINITE, the function's time-out interval
	 *            never elapses.
	 * @return
	 */
	int _MPUSBWrite(Pointer handle, // Input
			Pointer pData, // Input
			int dwLen, // Input
			IntByReference pLength, // Output
			int dwMilliseconds); // Input

	/**
	 * @param handle   - Identifies the endpoint pipe to be read. The pipe handle must
	 *            have been created with MP_READ access attribute.
	 * @param pData    - Points to the buffer that receives the data read from the pipe.
	 * @param dwLen    - Specifies the number of bytes to be read from the pipe.
	 * @param pLength  - Points to the number of bytes read. MPUSBRead sets this value to
	 *            zero before doing any work or error checking.
	 * @param dwMilliseconds     - Specifies the time-out interval, in milliseconds. The function
	 *           returns if the interval elapses, even if the operation is
	 *           incomplete. If dwMilliseconds is zero, the function tests the
	 *           data pipe and returns immediately. If dwMilliseconds is INFINITE,
	 *           the function's time-out interval never elapses.
	 * @return
	 */
	int _MPUSBReadInt(Pointer handle, // Input
			Pointer pData, // Output
			int dwLen, // Input
			IntByReference pLength, // Output 
			int dwMilliseconds); // Input
	
	
	/**
	 * closes a given handle.
	 * 
	 * @param handle
	 * @return
	 */
	boolean _MPUSBClose (Pointer handle);
	
	/**
	 * Returns the Device Descriptor Data
	 * 
	 * @param handle    - Identifies the endpoint pipe to be read. The pipe handle must
	 *           have been created with MP_READ access attribute.
	 * @param pDevDsc   - pointer to where the resulting descriptor should be copied.
	 * @param dwLen     - the available data in the pDevDsc buffer
	 * @param pLength   - a pointer to a DWORD that will be updated with the amount of data
	 *           actually written to the pDevDsc buffer.  This number will be
	 *           less than or equal to dwLen.
	 * @return
	 */
	int _MPUSBGetDeviceDescriptor(Pointer handle, // Input
			Pointer pDevDsc, // Output
			int dwLen, // Input
			IntByReference pLength); // Output
	
	/**
	 * Returns the Configuration Descriptor
	 * 
	 * @param handle    - Identifies the endpoint pipe to be read. The pipe handle must
	 *            have been created with MP_READ access attribute.
	 * @param bIndex    - the index of the configuration descriptor desired.  Valid input
	 *            range is 1 - 255.
	 * @param pDevDsc   - pointer to where the resulting descriptor should be copied.
	 * @param dwLen     - the available data in the pDevDsc buffer
	 * @param pLength   - a pointer to a DWORD that will be updated with the amount of data
	 *           actually written to the pDevDsc buffer.  This number will be
	 *           less than or equal to dwLen.
	 * @return
	 */
	int _MPUSBGetConfigurationDescriptor(Pointer handle, // Input
			byte bIndex, // Input
			Pointer pDevDsc, // Output
			int dwLen, // Input
			IntByReference pLength); // Output
	
	/**
	 * Returns the requested string descriptor
	 * 
	 * @param handle   - Identifies the endpoint pipe to be read. The pipe handle must
	 *           have been created with MP_READ access attribute.
	 * @param bIndex   - the index of the configuration descriptor desired.  Valid input
	 *            range is 0 - 255.
	 * @param wLangId  - the language ID of the string that needs to be read
	 * @param pDevDsc  - pointer to where the resulting descriptor should be copied.
	 * @param dwLen    - the available data in the pDevDsc buffer
	 * @param pLength   - a pointer to a DWORD that will be updated with the amount of data
	 *           actually written to the pDevDsc buffer.  This number will be
	 *           less than or equal to dwLen.
	 * @return
	 */
	int _MPUSBGetStringDescriptor(Pointer handle, // Input
			byte bIndex, // Input
			short wLangId, // Input
			Pointer pDevDsc, // Output
			int dwLen, // Input
			IntByReference pLength); // Output
	
	/**
	 *  Sets the device configuration through a USB
	 *           SET_CONFIGURATION command.
	 * 
	 * @param handle   - Identifies the endpoint pipe to be written. The pipe handle must
	 *           have been created with MP_WRITE access attribute.
	 * @param bConfigSetting  - Denotes the configuration number that needs to be set.  If this
	 *           number does not fall in the devices allowed configurations then
	 *           this function will return with MP_FAIL
	 * @return
	 */
	int _MPUSBSetConfiguration(Pointer handle, // Input
			short bConfigSetting); // Input
}

