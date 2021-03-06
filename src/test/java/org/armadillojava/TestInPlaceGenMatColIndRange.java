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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.armadillojava.TestUtil.assertMatEquals;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assume.assumeThat;

@RunWith(Parameterized.class)
public class TestInPlaceGenMatColIndRange extends TestClass {

  @Parameters(name = "{index}: GenMat = {0}, ColIndRange = {2}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenMat);
    inputClasses.add(InputClass.ColIndRange);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String    _genMatString;

  @Parameter(1)
  public Mat       _genMat;

  protected Mat    _copyOfGenMat;

  @Parameter(2)
  public String    _colIndRangeString;

  @Parameter(3)
  public Span      _colIndRange;

  protected Span   _copyOfColIndRange;

  @Before
  public void before() {
    _fileSuffix = _genMatString + "," + _colIndRangeString;

    _copyOfGenMat = new Mat(_genMat);
    _copyOfColIndRange = new Span(_colIndRange);
  }

  @After
  public void after() {
    _genMat.inPlace(Op.EQUAL, _copyOfGenMat);
    _colIndRange = new Span(_copyOfColIndRange);
  }

  @Test
  public void testMatSwapCols() throws IOException {
    assumeThat(_colIndRange._isEntireRange, is(false));
    assumeThat(_genMat.in_range(Span.all(), _colIndRange), is(true));

    _genMat.swap_cols(_colIndRange._first, _colIndRange._last);

    assertMatEquals(_genMat, load("Mat.swap_cols"));
  }

  @Test
  public void testMatShedCols() throws IOException {
    assumeThat(_colIndRange._isEntireRange, is(false));
    assumeThat(_genMat.in_range(Span.all(), _colIndRange), is(true));

    _genMat.shed_cols(_colIndRange._first, _colIndRange._last);

    assertMatEquals(_genMat, load("Mat.shed_cols"));
  }

}
