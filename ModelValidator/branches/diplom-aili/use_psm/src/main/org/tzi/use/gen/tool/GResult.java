/*
 * This is source code of the Snapshot Generator, an extension for USE
 * to generate (valid) system states of UML models.
 * Copyright (C) 2001 Joern Bohling, University of Bremen
 *
 * About USE:
 *   USE - UML based specification environment
 *   Copyright (C) 1999,2000,2001 Mark Richters, University of Bremen
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */


package org.tzi.use.gen.tool;

/**
 * Knows the last checker, collector and
 * random number of the last search.
 * @author  Joern Bohling
 */
public class GResult {
    private GCollectorImpl fCollector;
    
    private GChecker fChecker;
    
    private long fRandomNr;
    
    /**
     * How long did it take to calculate the result?
     */
    private long duration;
    
    public GResult(GCollectorImpl collector, GChecker checker, long r, long duration) {
        fCollector = collector;
        fChecker = checker;
        fRandomNr = r;
        this.duration = duration;
    }

    public GCollectorImpl collector() {
        return fCollector;
    }
    
    public GChecker checker() {
        return fChecker;
    }

    public long randomNr() {
        return fRandomNr;
    }
    
    /**
     * The time in milliseconds it took to calculate the result.
     * @return
     */
    public long getDuration() {
    	return this.duration;
    }
}
