/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package externals.apps;

/**
 *
 * @author Wellington
 */

public class MsOffice {
	public MsOffice() {
		super();
	}

	@SuppressWarnings("unused")
	public static void msWord() {
		try {
			Process p = Runtime.getRuntime().exec("cmd.exe /C start winword");
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	@SuppressWarnings("unused")
	public static void mspowerPoint() {
		try {
			Process p = Runtime.getRuntime().exec("cmd.exe /C start powerpnt");
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	@SuppressWarnings("unused")
	public static void msAccess() {
		try {
			Process p = Runtime.getRuntime().exec("cmd.exe /C start msaccess");
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	@SuppressWarnings("unused")
	public static void msLync() {
		try {
			Process p = Runtime.getRuntime().exec("cmd.exe /C start mspub");
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	@SuppressWarnings("unused")
	public static void msOnenote() {
		try {
			Process p = Runtime.getRuntime().exec("cmd.exe /C start onenote");
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	@SuppressWarnings("unused")
	public static void msExcel() {
		try {
			Process p = Runtime.getRuntime().exec("cmd.exe /C start excel");
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	@SuppressWarnings("unused")
	public static void msOutLook() {
		try {
			Process p = Runtime.getRuntime().exec("cmd.exe /C start outlook");
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}
