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

/**
 * March 22th 2001 
 * @author  Joern Bohling
 */

package org.tzi.use.kodkod.assl;

import java.util.Iterator;
import java.util.List;

import org.tzi.use.gen.assl.dynamics.GConfiguration;
import org.tzi.use.gen.assl.dynamics.GEvaluationException;
import org.tzi.use.gen.assl.dynamics.IGCaller;
import org.tzi.use.gen.assl.dynamics.IGChecker;
import org.tzi.use.gen.assl.dynamics.IGCollector;
import org.tzi.use.gen.assl.statics.GProcedure;
import org.tzi.use.kodkod.assl.dynamics.GCreatorKodKod;
import org.tzi.use.uml.ocl.expr.MultiplicityViolationException;
import org.tzi.use.uml.ocl.expr.VarDecl;
import org.tzi.use.uml.ocl.value.UndefinedValue;
import org.tzi.use.uml.ocl.value.Value;
import org.tzi.use.uml.ocl.value.VarBindings;
import org.tzi.use.uml.sys.MSystemState;

/**
 * this class evaluetes a a procedure 
 * 
 * this file is based on GEvalProcedure
 * 
 * @author Juergen Widdermann
 */
public class GEvalProcedureKodKod implements IGCaller {
    private GProcedure fProcedure;
    private IGChecker fChecker;
	private AsslTranslation translation;
  
    public GEvalProcedureKodKod( GProcedure proc, AsslTranslation translation ) {
        fProcedure = proc;
        this.translation = translation;
    }

    public void eval(List<Value> paramValues,
                     MSystemState state,
                     IGCollector collector,
                     IGChecker checker,
                     long randomNr) throws GEvaluationException {
        collector.detailPrintWriter().println(new StringBuilder("evaluating `").append(fProcedure).append("'").toString());
        fChecker = checker;
        VarBindings varBindings = new VarBindings();
        Iterator<Value> valuesIt = paramValues.iterator();

        for (VarDecl parDecl : fProcedure.parameterDecls()) {	
            String varName = parDecl.name();
            Value value = valuesIt.next();
            varBindings.push(varName, value);
            collector.detailPrintWriter().println( varName + ":=" + value );
        }
        
        for (VarDecl localDecl : fProcedure.localDecls()) {
            Value value = UndefinedValue.instance;
            varBindings.push(localDecl.name(), value);
            collector.detailPrintWriter().println(localDecl.name() + ":=" + value);
        }
        
        GConfiguration conf = new GConfiguration( state,
                                                  varBindings,
                                                  randomNr );
        translation.setInstructionList(fProcedure.instructionList());
        translation.resetAsslUniverse();
        translation.readSystemState(state);
        GCreatorKodKod.setAsslTranslation(translation);
        translation.checkTryInstructions();
        GCreatorKodKod.createFor( fProcedure.instructionList() )
            .eval( conf, this, collector );       // just delegation
    }
    
    public void feedback( GConfiguration conf,
                          Value value,
                          IGCollector collector ) throws GEvaluationException {
        // value is not relevant
        collector.leaf();
        try {
            if (fChecker.check(conf.systemState(),
                               collector.basicPrintWriter())) {
                collector.setValidStateFound();
            }
        } catch (MultiplicityViolationException e) {
            collector.detailPrintWriter().println("An error occured while checking an invariant:");
            collector.detailPrintWriter().println(e.getMessage());
        }
    }

    public String toString() {
        return "GEvalProcedure";
    }

}
