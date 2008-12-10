/*
 * Copyright 2005-2008 Roger Kapsi, Sam Berlin
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package org.ardverk.collection;


/**
 * An {@link KeyAnalyzer} for {@link String}s
 */
public class StringKeyAnalyzer implements KeyAnalyzer<String> {
    
    private static final long serialVersionUID = -7032449491269434877L;
    
    public static final StringKeyAnalyzer INSTANCE = new StringKeyAnalyzer();
    
    public static final int LENGTH = 16;
    
    private static final int MSB = 0x8000;
    
    /**
     * 
     */
    private static int mask(int bit) {
        return MSB >>> bit;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Class<String> getKeyClass() {
        return String.class;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int lengthInBits(String key) {
        return (key != null ? key.length() * LENGTH : 0);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int bitIndex(String key, int offsetInBits, int lengthInBits,
            String other, int otherOffsetInBits, int otherLengthInBits) {
        boolean allNull = true;
        
        if (offsetInBits % LENGTH != 0 || otherOffsetInBits % LENGTH != 0 
                || lengthInBits % LENGTH != 0 || otherLengthInBits % LENGTH != 0) {
            throw new IllegalArgumentException("offsets & lengths must be at character boundaries");
        }
        
        
        int beginIndex1 = offsetInBits / LENGTH;
        int beginIndex2 = otherOffsetInBits / LENGTH;
        
        int endIndex1 = beginIndex1 + lengthInBits / LENGTH;
        int endIndex2 = beginIndex2 + otherLengthInBits / LENGTH;
        
        int length = Math.max(endIndex1, endIndex2);
        
        // Look at each character, and if they're different
        // then figure out which bit makes the difference
        // and return it.
        char k = 0, f = 0;
        for(int i = 0; i < length; i++) {
            int index1 = i + beginIndex1;
            int index2 = i + beginIndex2;
            
            if (index1 >= endIndex1) {
                k = 0;
            } else {
                k = key.charAt(index1);
            }
            
            if (other == null || index2 >= endIndex2) {
                f = 0;
            } else {
                f = other.charAt(index2);
            }
            
            if (k != f) {
               int x = k ^ f;
               return i * LENGTH + (Integer.numberOfLeadingZeros(x) - LENGTH);
            }
            
            if (k != 0) {
                allNull = false;
            }
        }
        
        if (allNull) {
            return KeyAnalyzer.NULL_BIT_KEY;
        }
        
        return KeyAnalyzer.EQUAL_BIT_KEY;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isBitSet(String key, int bitIndex, int lengthInBits) {
        if (key == null || bitIndex >= lengthInBits) {
            return false;
        }
        
        int index = (int)(bitIndex / LENGTH);
        int bit = (int)(bitIndex % LENGTH);
        
        return (key.charAt(index) & mask(bit)) != 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compare(String o1, String o2) {
        return o1.compareTo(o2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int bitsPerElement() {
        return LENGTH;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPrefix(String prefix, int offset, 
            int lengthInBits, String key) {
        if (offset % LENGTH != 0 || lengthInBits % LENGTH != 0) {
            throw new IllegalArgumentException("Cannot determine prefix outside of character boundaries");
        }
    
        String s1 = prefix.substring(offset / LENGTH, lengthInBits / LENGTH);
        return key.startsWith(s1);
    }
}