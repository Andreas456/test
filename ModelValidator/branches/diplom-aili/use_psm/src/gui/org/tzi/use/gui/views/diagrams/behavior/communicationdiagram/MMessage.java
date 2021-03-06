/*
 * USE - UML based specification environment
 * Copyright (C) 1999-2010 Mark Richters, University of Bremen
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

// $Id$

package org.tzi.use.gui.views.diagrams.behavior.communicationdiagram;

import org.tzi.use.uml.mm.MNamedElement;

/**
 * This class represent a single message in communication diagrams
 * 
 * @author Quang Dung Nguyen
 * 
 */
public class MMessage implements MNamedElement {

	public static final int FORWARD = 1;
	public static final int BACKWARD = 2;
	public static final int RETURN = 3;

	private String sequenceNumber;
	private String name;
	private int direction = FORWARD;
	private boolean failedReturn;

	public MMessage(String sequenceNumber, String message) {
		this.sequenceNumber = sequenceNumber;
		this.name = message;
		this.failedReturn = false;
	}

	/**
     * 
     */
	public MMessage() {
		this.sequenceNumber = "";
		this.name = "";
		this.failedReturn = false;
	}

	public String getSequenceNumber() {
		return sequenceNumber;
	}

	public String getMessageName() {
		return name;
	}

	public String getSequenceNumberMessage() {
		return sequenceNumber + " : " + name;
	}

	/**
	 * @return the fDirection
	 */
	public int getDirection() {
		return direction;
	}

	/**
	 * @param direction the fDirection to set
	 */
	public void setDirection(int direction) {
		this.direction = direction;
	}

	/**
	 * @return the failedReturn
	 */
	public boolean isFailedReturn() {
		return failedReturn;
	}

	/**
	 * @param failedReturn the failedReturn to set
	 */
	public void setFailedReturn(boolean failedReturn) {
		this.failedReturn = failedReturn;
	}

	@Override
	public String name() {
		if (sequenceNumber.equals("") && name.equals("")) {
			return "-";
		}
		return sequenceNumber + " : " + name;
	}

	@Override
	public int hashCode() {
		return sequenceNumber.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MMessage) {
			if (name().equals(((MMessage) obj).name())) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return name();
	}

}
