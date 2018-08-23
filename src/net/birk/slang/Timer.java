package net.birk.slang;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

public class Timer {

	static class Timing {
		public long timeNano;
		public String desc;
	}

	private static Timing currentTiming = null;
	private static ArrayList<Timing> completedTimings = new ArrayList<Timing>();

	public static void startSection(String name) {
		if(currentTiming != null) {
			long end = System.nanoTime();
			currentTiming.timeNano = end - currentTiming.timeNano;
			completedTimings.add(currentTiming);

			currentTiming = new Timing();
			currentTiming.timeNano = System.nanoTime();
			currentTiming.desc = name;
		} else {
			Timing t = new Timing();
			t.desc = name;
			t.timeNano = System.nanoTime();
			currentTiming = t;
		}
	}

	public static void printTimings() {
		if(currentTiming != null) {
			long end = System.nanoTime();
			currentTiming.timeNano = end - currentTiming.timeNano;
			completedTimings.add(currentTiming);
		}

		long totalTime = 0;
		for(Timing t : completedTimings) {
			totalTime += t.timeNano;
		}

		DecimalFormat df = new DecimalFormat("#.##");
		df.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));

		for(Timing t : completedTimings) {
			double ms = (double)t.timeNano / 1000000.0;
			double percentage = (((double)t.timeNano/(double)totalTime) * 100.0);
			System.out.println(t.desc + ": " + df.format(ms) + " ms - " + df.format(percentage) + "%");
		}

		double totalMs = totalTime / 1000000.0;
		System.out.println("\nTotal time: " + df.format(totalMs) + " ms - " + ((totalTime/totalTime)*100.0) + "%");
	}

}
