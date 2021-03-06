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
public class TestInPlaceGenMatRowIndsGenDouble extends TestClass {

  @Parameters(name = "{index}: GenMat = {0}, RowInds = {2}, GenDouble = {4}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenMat);
    inputClasses.add(InputClass.RowInds);
    inputClasses.add(InputClass.GenDouble);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genMatString;

  @Parameter(1)
  public Mat    _genMat;

  protected Mat _copyOfGenMat;

  @Parameter(2)
  public String _rowIndsString;

  @Parameter(3)
  public Col    _rowInds;

  protected Col _copyOfRowInds;

  @Parameter(4)
  public String _genRowVecString;

  @Parameter(5)
  public double    _genRowVec;

  protected double _copyOfGenRowVec;

  @Before
  public void before() {
    _fileSuffix = _genMatString + "," + _rowIndsString + "," + _genRowVecString;

    _copyOfGenMat = new Mat(_genMat);
    _copyOfRowInds = new Col(_rowInds);
    _copyOfGenRowVec = new Double(_genRowVec);
  }

  @After
  public void after() {
    _genMat.inPlace(Op.EQUAL, _copyOfGenMat);
    _rowInds.inPlace(Op.EQUAL, _copyOfRowInds);
    _genRowVec = new Double(_copyOfGenRowVec);
  }

  @Test
  public void testMatRowsPlus() throws IOException {
    for(int i = 0; i < _rowInds.n_elem; i++) {
      assumeThat((int) _rowInds.at(i), is(lessThan(_genMat.n_rows)));
    }

    _genMat.rows(_rowInds, Op.PLUS, _genRowVec);

    assertMatEquals(_genMat, load("Mat.rowsPlus"));
  }

  @Test
  public void testMatRowsMinus() throws IOException {
    for(int i = 0; i < _rowInds.n_elem; i++) {
      assumeThat((int) _rowInds.at(i), is(lessThan(_genMat.n_rows)));
    }

    _genMat.rows(_rowInds, Op.MINUS, _genRowVec);

    assertMatEquals(_genMat, load("Mat.rowsMinus"));
  }

  @Test
  public void testMatRowsTimes() throws IOException {
    for(int i = 0; i < _rowInds.n_elem; i++) {
      assumeThat((int) _rowInds.at(i), is(lessThan(_genMat.n_rows)));
    }

    _genMat.rows(_rowInds, Op.TIMES, _genRowVec);

    assertMatEquals(_genMat, load("Mat.rowsTimes"));
  }

  @Test
  public void testMatRowsDivide() throws IOException {
    for(int i = 0; i < _rowInds.n_elem; i++) {
      assumeThat((int) _rowInds.at(i), is(lessThan(_genMat.n_rows)));
    }

    _genMat.rows(_rowInds, Op.DIVIDE, _genRowVec);

    assertMatEquals(_genMat, load("Mat.rowsDivide"));
  }

}
