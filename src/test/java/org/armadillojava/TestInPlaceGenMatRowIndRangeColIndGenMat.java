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
import static org.hamcrest.Matchers.lessThan;

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
public class TestInPlaceGenMatRowIndRangeColIndGenMat extends TestClass {

  @Parameters(name = "{index}: GenMat = {0}, RowIndRange = {2}, ColInd = {4}, GenMat = {6}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenMat);
    inputClasses.add(InputClass.RowIndRange);
    inputClasses.add(InputClass.ColInd);
    inputClasses.add(InputClass.GenMat);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String    _genMatAString;

  @Parameter(1)
  public Mat       _genMatA;

  protected Mat    _copyOfGenMatA;

  @Parameter(2)
  public String    _rowIndRangeString;

  @Parameter(3)
  public Span      _rowIndRange;

  protected Span   _copyOfRowIndRange;

  @Parameter(4)
  public String    _colIndString;

  @Parameter(5)
  public int    _colInd;

  protected int _copyOfColInd;

  @Parameter(6)
  public String    _genMatBString;

  @Parameter(7)
  public Mat    _genMatB;

  protected Mat _copyOfGenMatB;

  @Before
  public void before() {
    _fileSuffix = _genMatAString + "," + _rowIndRangeString + "," + _colIndString + "," + _genMatBString;

    _copyOfGenMatA = new Mat(_genMatA);
    _copyOfRowIndRange = new Span(_rowIndRange);
    _copyOfColInd = new Integer(_colInd);
    _copyOfGenMatB = new Mat(_genMatB);
  }

  @After
  public void after() {
    _genMatA.inPlace(Op.EQUAL, _copyOfGenMatA);
    _rowIndRange = new Span(_copyOfRowIndRange);
    _colInd = new Integer(_copyOfColInd);
    _genMatB.inPlace(Op.EQUAL, _copyOfGenMatB);
  }

  @Test
  public void testMatColEqual() throws IOException {
    assumeThat(_colInd, is(lessThan(_genMatA.n_cols)));
    assumeThat(_genMatB.is_colvec(), is(true));
    if(!_rowIndRange._isEntireRange) {
      assumeThat(_rowIndRange._last, is(lessThan(_genMatA.n_rows)));
      assumeThat(_genMatB.n_rows, is(_rowIndRange._last - _rowIndRange._first + 1));
    } else {
      assumeThat(_genMatB.n_rows, is(_genMatA.n_rows));
    }

    _genMatA.col(_rowIndRange, _colInd, Op.EQUAL, _genMatB);

    assertMatEquals(_genMatA, load("Mat.colEqual"));
  }

  @Test
  public void testMatColPlus() throws IOException {
    assumeThat(_colInd, is(lessThan(_genMatA.n_cols)));
    assumeThat(_genMatB.is_colvec(), is(true));
    if(!_rowIndRange._isEntireRange) {
      assumeThat(_rowIndRange._last, is(lessThan(_genMatA.n_rows)));
      assumeThat(_genMatB.n_rows, is(_rowIndRange._last - _rowIndRange._first + 1));
    } else {
      assumeThat(_genMatB.n_rows, is(_genMatA.n_rows));
    }

    _genMatA.col(_rowIndRange, _colInd, Op.PLUS, _genMatB);

    assertMatEquals(_genMatA, load("Mat.colPlus"));
  }

  @Test
  public void testMatColMinus() throws IOException {
    assumeThat(_colInd, is(lessThan(_genMatA.n_cols)));
    assumeThat(_genMatB.is_colvec(), is(true));
    if(!_rowIndRange._isEntireRange) {
      assumeThat(_rowIndRange._last, is(lessThan(_genMatA.n_rows)));
      assumeThat(_genMatB.n_rows, is(_rowIndRange._last - _rowIndRange._first + 1));
    } else {
      assumeThat(_genMatB.n_rows, is(_genMatA.n_rows));
    }

    _genMatA.col(_rowIndRange, _colInd, Op.MINUS, _genMatB);

    assertMatEquals(_genMatA, load("Mat.colMinus"));
  }

  @Test
  public void testMatColElemTimes() throws IOException {
    assumeThat(_colInd, is(lessThan(_genMatA.n_cols)));
    assumeThat(_genMatB.is_colvec(), is(true));
    if(!_rowIndRange._isEntireRange) {
      assumeThat(_rowIndRange._last, is(lessThan(_genMatA.n_rows)));
      assumeThat(_genMatB.n_rows, is(_rowIndRange._last - _rowIndRange._first + 1));
    } else {
      assumeThat(_genMatB.n_rows, is(_genMatA.n_rows));
    }

    _genMatA.col(_rowIndRange, _colInd, Op.ELEMTIMES, _genMatB);

    assertMatEquals(_genMatA, load("Mat.colElemTimes"));
  }

  @Test
  public void testMatColElemDivide() throws IOException {
    assumeThat(_colInd, is(lessThan(_genMatA.n_cols)));
    assumeThat(_genMatB.is_colvec(), is(true));
    if(!_rowIndRange._isEntireRange) {
      assumeThat(_rowIndRange._last, is(lessThan(_genMatA.n_rows)));
      assumeThat(_genMatB.n_rows, is(_rowIndRange._last - _rowIndRange._first + 1));
    } else {
      assumeThat(_genMatB.n_rows, is(_genMatA.n_rows));
    }

    _genMatA.col(_rowIndRange, _colInd, Op.ELEMDIVIDE, _genMatB);

    assertMatEquals(_genMatA, load("Mat.colElemDivide"));
  }

}
