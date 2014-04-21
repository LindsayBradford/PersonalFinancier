/**
 * Copyright (c) 2012, Lindsay Bradford and other Contributors.
 * All rights reserved.
 * 
 * This program and the accompanying materials  are made available 
 * under the terms of the BSD 3-Clause licence  which accompanies 
 * this distribution, and is available at
 * http://opensource.org/licenses/BSD-3-Clause
 * 
 * Note that this is predominately the code of another author, taken from
 * a public form on software problem resolution. Detail below.
 */

package blacksmyth.general;

import java.util.ArrayList;

/**
 * Implements an insertion Sort from greatest to smallest (only if
 * all entries are inserted with {@link #insertSorted(Object)}.. 
 * Essentially a tweak of an insertion sort algorithm posted
 * to stackexchange by user http://stackoverflow.com/users/276052/aioobe
 * Author: Mostly ailbo. 
 * @param <T>
 */
public class SortedArrayList<T> extends ArrayList<T> {
    @SuppressWarnings("unchecked")
    public void insertSorted(T newValue) {
        add(0, newValue);
        Comparable<T> cmp = (Comparable<T>) newValue;
        for (int i = 0; i < size() -1 && cmp.compareTo(get(i+1)) < 0; i++) {
            T tmp = get(i);
            set(i, get(i+1));
            set(i+1, tmp);
        }
    }
    
    public T first() {
      return this.get(0);
    }
    
    public T last() {
      return this.get(this.size() - 1);
    }
}
