package utils;

import java.util.ArrayList;
import java.util.List;

import models.Movie;

/**
 * This is a MergeX class, created by authors specified below. Initially it was
 * created to sort arrays, however this version is adapted to sort ArrayLists of
 * objects
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class MergeX {
	private static final int CUTOFF = 7; // cutoff to insertion sort

	// This class should not be instantiated.
	private MergeX() {
	}

	private static void merge(List<Movie> src, List<Movie> dst, int lo, int mid, int hi) {
		assert isSorted(src, lo, mid);
		assert isSorted(src, mid + 1, hi);

		int i = lo, j = mid + 1;
		for (int k = lo; k <= hi; k++) {
			if (i > mid)
				dst.set(k, src.get(j++));
			else if (j > hi)
				dst.set(k, src.get(i++));
			else if (less(src.get(j), src.get(i)))
				dst.set(k, src.get(j++));
			else
				dst.set(k, src.get(i++));
		}
		assert isSorted(dst, lo, hi);
	}

	
	private static void sort(List<Movie> src, List<Movie> dst, int lo, int hi) {
		if (hi <= lo + CUTOFF) {
			insertionSort(dst, lo, hi);
			return;
		}
		int mid = lo + (hi - lo) / 2;
		sort(dst, src, lo, mid);
		sort(dst, src, mid + 1, hi);

		if (!less(src.get(mid + 1), src.get(mid))) {
			for (int i = lo; i <= hi; i++)
				dst.set(i, src.get(i));
			return;
		}

		merge(src, dst, lo, mid, hi);
	}

	/**
	 * Rearranges the List in ascending order, using the natural order.
	 * 
	 * @param a
	 *            the array to be sorted
	 */
	public static void sort(List<Movie> a) {
		List<Movie> aux = new ArrayList<Movie>();
		aux.addAll(a);
		sort(aux, a, 0, a.size() - 1);
		assert isSorted(a);
	}

	/**
	 * insertion sort used to sort the List
	 */
	private static void insertionSort(List<Movie> a, int lo, int hi) {
		for (int i = lo; i <= hi; i++)
			for (int j = i; j > lo && less(a.get(j), a.get(j - 1)); j--)
				exch(a, j, j - 1);
	}
	/**
	 *  exchange a.get(i) and a.get(j)
	 */
	private static void exch(List<Movie> a, int i, int j) {
		Movie lowIndex = a.get(i);
		Movie highIndex = a.get(j);
		a.set(i, highIndex);
		a.set(j, lowIndex);
	}

	/** 
	 * is a.get(i) < a.get(j)?
	 */
	private static boolean less(Movie a, Movie b) {
		return b.compareTo(a) > 0;
	}

	/**
	 * checks, if List is sorted
	 */
	private static boolean isSorted(List<Movie> a) {
		return isSorted(a, 0, a.size() - 1);
	}

	/**
	 * checks, if List is sorted
	 */
	private static boolean isSorted(List<Movie> a, int lo, int hi) {
		for (int i = lo + 1; i <= hi; i++)
			if (less(a.get(i), a.get(i - 1)))
				return false;
		return true;
	}

}
