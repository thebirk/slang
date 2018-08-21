package net.birk.slang;

import java.util.ArrayList;
import java.util.Stack;

public class Timer {

	static class Timing {
		public long timeNano;
		public String desc;
	}

	public static Stack<Timing> timings = new Stack<Timing>();
	public static ArrayList<Timing> completedTimings = new ArrayList<Timing>();

	public static void startSection(String name) {
		Timing t = new Timing();
		t.desc = name;
		t.timeNano = System.nanoTime();
		timings.push(t);
	}

	public static void endSection() {
		long end = System.nanoTime();
		Timing t = timings.pop();
		t.timeNano = end - t.timeNano;
		completedTimings.add(t);
	}

	public static void printTimings() {
		for(Timing t : completedTimings) {
			System.out.println(t.desc + ": " + ((double)t.timeNano / 1000000.0) + " ms");
		}
	}

}
