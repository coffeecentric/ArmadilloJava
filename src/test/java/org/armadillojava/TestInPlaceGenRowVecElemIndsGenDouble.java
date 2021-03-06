/*******************************************************************************
 * Copyright 2013-2014 Sebastian Niemann <niemann@sra.uni-hannover.de>.
 * 
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://opensource.org/licenses/MIT
 * 
 * Developers:
 * Sebastian Niemann - Lead developer
 * Daniel Kiechle - Unit testing
 ******************************************************************************/
package org.armadillojava;

import static org.armadillojava.TestUtil.assertMatEquals;
import static org.junit.Assume.assumeThat;
import static org.hamcrest.CoreMatchers.is;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class TestInPlaceGenRowVecElemIndsGenDouble extends TestClass {

  @Parameters(name = "{index}: GenRowVec = {0}, ElemInds = {2}, GenDouble = {4}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenRowVec);
    inputClasses.add(InputClass.ElemInds);
    inputClasses.add(InputClass.GenDouble);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genRowVecString;

  @Parameter(1)
  public Row    _genRowVec;

  protected Row _copyOfGenRowVec;

  @Parameter(2)
  public String _elemIndsString;

  @Parameter(3)
  public Col    _elemInds;

  protected Col _copyOfElemInds;
  
  @Parameter(4)
  public String _genDoubleString;

  @Parameter(5)
  public Double    _genDouble;

  protected Double _copyOfGenDouble;

  @Before
  public void before() {
    _fileSuffix = _genRowVecString + "," + _elemIndsString + "," +_genDoubleString;

    _copyOfGenRowVec = new Row(_genRowVec);
    _copyOfElemInds = new Col(_elemInds);
    _copyOfGenDouble = new Double(_genDouble);
  }

  @After
  public void after() {
	_genRowVec.inPlace(Op.EQUAL, _copyOfGenRowVec);
	_elemInds = new Col(_copyOfElemInds);
	_genDouble = new Double(_copyOfGenDouble);
  }

  @Test
  public void testRowElemPlus() throws IOException {
    for(int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genRowVec.in_range((int) _elemInds.at(n)), is(true));
    }

    _genRowVec.elem(_elemInds, Op.PLUS, _genDouble);
    
    assertMatEquals(_genRowVec, load("Row.elemPlus"));
  }
  
  @Test
  public void testRowElemMinus() throws IOException {
    for(int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genRowVec.in_range((int) _elemInds.at(n)), is(true));
    }

    _genRowVec.elem(_elemInds, Op.MINUS, _genDouble);
    
    assertMatEquals(_genRowVec, load("Row.elemMinus"));
  }
  
  @Test
  public void testRowElemTimes() throws IOException {
    for(int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genRowVec.in_range((int) _elemInds.at(n)), is(true));
    }

    _genRowVec.elem(_elemInds, Op.TIMES, _genDouble);
    
    assertMatEquals(_genRowVec, load("Row.elemTimes"));
  }
  
  @Test
  public void testRowElemDivide() throws IOException {
    for(int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genRowVec.in_range((int) _elemInds.at(n)), is(true));
    }

    _genRowVec.elem(_elemInds, Op.DIVIDE, _genDouble);
    
    assertMatEquals(_genRowVec, load("Row.elemDivide"));
  }
  
  @Test
  public void testRowColsPlus() throws IOException {
    for(int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genRowVec.in_range((int) _elemInds.at(n)), is(true));
    }

    _genRowVec.cols(_elemInds, Op.PLUS, _genDouble);
    
    assertMatEquals(_genRowVec, load("Row.colsPlus"));
  }
  
  @Test
  public void testRowColsMinus() throws IOException {
    for(int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genRowVec.in_range((int) _elemInds.at(n)), is(true));
    }

    _genRowVec.cols(_elemInds, Op.MINUS, _genDouble);
    
    assertMatEquals(_genRowVec, load("Row.colsMinus"));
  }

  @Test
  public void testRowColsTimes() throws IOException {
    for(int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genRowVec.in_range((int) _elemInds.at(n)), is(true));
    }

    _genRowVec.cols(_elemInds, Op.TIMES, _genDouble);
    
    assertMatEquals(_genRowVec, load("Row.colsTimes"));
  }
  
  @Test
  public void testRowColsDivide() throws IOException {
    for(int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genRowVec.in_range((int) _elemInds.at(n)), is(true));
    }

    _genRowVec.cols(_elemInds, Op.DIVIDE, _genDouble);
    
    assertMatEquals(_genRowVec, load("Row.colsDivide"));
  }
}
