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

package org.tzi.use.kodkod.assl.dynamics;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;

import org.tzi.use.gen.assl.dynamics.GConfiguration;
import org.tzi.use.gen.assl.dynamics.GEvalInstruction;
import org.tzi.use.gen.assl.dynamics.GEvaluationException;
import org.tzi.use.gen.assl.dynamics.IGCaller;
import org.tzi.use.gen.assl.dynamics.IGCollector;
import org.tzi.use.gen.assl.statics.GInstrInsert_Assoc_Linkends;
import org.tzi.use.gen.assl.statics.GInstruction;
import org.tzi.use.gen.assl.statics.GValueInstruction;
import org.tzi.use.kodkod.assl.AsslTranslation;
import org.tzi.use.uml.mm.MAssociation;
import org.tzi.use.uml.ocl.value.ObjectValue;
import org.tzi.use.uml.ocl.value.Value;
import org.tzi.use.uml.sys.MObject;
import org.tzi.use.uml.sys.MSystem;
import org.tzi.use.uml.sys.MSystemException;
import org.tzi.use.uml.sys.MSystemState;
import org.tzi.use.uml.sys.StatementEvaluationResult;
import org.tzi.use.uml.sys.soil.MLinkInsertionStatement;
import org.tzi.use.uml.sys.soil.MRValue;
import org.tzi.use.uml.sys.soil.MRValueExpression;
import org.tzi.use.uml.sys.soil.MStatement;

/**
 * eval insert association
 * 
 * based on {@link GEvalInstrInsert_Assoc_LinkendsKodKod}
 * 
 * @author Juergen Widdermann
 */
public class GEvalInstrInsert_Assoc_LinkendsKodKod extends GEvalInstruction
    implements IGCaller {
    private GInstrInsert_Assoc_Linkends fInstr;
    private IGCaller fCaller;
    private ListIterator<GValueInstruction> fIterator;
    private List<String> fObjectNames;
    
    public GEvalInstrInsert_Assoc_LinkendsKodKod(GInstrInsert_Assoc_Linkends instr, AsslTranslation asslTranslation ) {
        fInstr = instr;
    }
    
    public void eval(GConfiguration conf,
                     IGCaller caller,
                     IGCollector collector) throws GEvaluationException {
        collector.detailPrintWriter().println(new StringBuilder("evaluating `").append(fInstr).append("'").toString());
        fCaller = caller;
        fIterator = fInstr.linkEnds().listIterator();
        fObjectNames = new ArrayList<String>();
    
        // fIterator has a next element, because an association has at least
        // two linkends.
        GCreatorKodKod.createFor((GInstruction)fIterator.next())
            .eval(conf,this,collector);
        fIterator.previous();
    }

    public void feedback(GConfiguration conf,
                         Value value,
                         IGCollector collector ) throws GEvaluationException {
        if (value.isUndefined()) {
            collector.invalid( buildCantExecuteMessage(fInstr,
                                                       (GValueInstruction) fInstr.linkEnds()
                                                       .get(fIterator.previousIndex())) );
            return;
        }

        fObjectNames.add( ((ObjectValue) value).value().name() );

        if (fIterator.hasNext()) {
            GCreatorKodKod.createFor((GInstruction)fIterator.next())
                .eval(conf,this,collector);
            fIterator.previous();
        }
        else {
            // every parameter is evaluated, so fObjectNames is complete now
            createLink(conf, collector);
            // add KodKod Link
            //Changing Names of Vector
            HashSet<MObject> objects = new HashSet<MObject>();
            MSystemState state = conf.systemState();
            for(String objectName : fObjectNames) {
            	MObject object = state.objectByName(objectName);
            	objects.add(object);
            }
        }
    }
    
    private void createLink(GConfiguration conf,
                            IGCollector collector) throws GEvaluationException {

    	MSystemState state = conf.systemState();
        MSystem system = state.system();
        PrintWriter basicOutput = collector.basicPrintWriter();
        //PrintWriter detailOutput = collector.detailPrintWriter();
        
        MAssociation association = fInstr.association();
        List<MRValue> participants = 
        	new ArrayList<MRValue>(fObjectNames.size());
        
        for (String objectName : fObjectNames) {
        	MObject object = state.objectByName(objectName);
        	participants.add(
        			new MRValueExpression(object));	
        }
        
        MStatement statement = 
        	new MLinkInsertionStatement(association, participants, Collections.<List<MRValue>>emptyList());
        
        MStatement inverseStatement;

        basicOutput.println(statement.getShellCommand());
        try {
        	StatementEvaluationResult evaluationResult = 
        		system.evaluateStatement(statement, true, false, false);
        	evaluationResult.getStateDifference().getNewObjects();
        	inverseStatement = evaluationResult.getInverseStatement();
		} catch (MSystemException e) {
			collector.invalid(e);
			return;
		}
		
		//detailOutput.println("`" + fInstr + "' == (no value)");
		
		fCaller.feedback(conf, null, collector);
        if (collector.expectSubsequentReporting()) {
        	collector.subsequentlyPrependStatement(statement);
        }
         
        basicOutput.println("undo: " + statement.getShellCommand());
        try {
        	system.evaluateStatement(inverseStatement, true, false);
		} catch (MSystemException e) {
			collector.invalid(e);
		}
    }
    
    public String toString() {
        return "GEvalInstrInsert_Assoc_Linkends";
    }
}
