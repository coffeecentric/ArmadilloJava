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
import static org.junit.Assert.assertThat;
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
public class TestGenMatSinValTol extends TestClass {

  @Parameters(name = "{index}: GenMat = {0}, SinValTol = {2}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenMat);
    inputClasses.add(InputClass.SinValTol);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String    _genMatString;

  @Parameter(1)
  public Mat       _genMat;

  protected Mat    _copyOfGenMat;

  @Parameter(2)
  public String    _sinValTolString;

  @Parameter(3)
  public double    _sinValTol;

  protected double _copyOfSinValTol;

  @Before
  public void before() {
    _fileSuffix = _genMatString + "," + _sinValTolString;

    _copyOfGenMat = new Mat(_genMat);
    _copyOfSinValTol = new Double(_sinValTol);
  }

  @After
  public void after() {
    assertMatEquals(_genMat, _copyOfGenMat, 0);
    assertThat(_sinValTol, is(_copyOfSinValTol));
  }

  @Test
  public void testArmaRank() throws IOException {
    int expected = (int) load("Arma.rank")._data[0];
    assertThat(Arma.rank(_genMat, _sinValTol), is(expected));
  }

  @Test
  public void testArmaPinvA() throws IOException {
    assertMatEquals(Arma.pinv(_genMat, _sinValTol), load("Arma.pinv"), TestUtil.globalDelta(load("Arma.pinv"), 1e-12));
  }

  @Test
  public void testArmaPinvB() throws IOException {
    Mat pinv = new Mat();

    Arma.pinv(pinv, _genMat, _sinValTol);

    assertMatEquals(pinv, load("Arma.pinv"), TestUtil.globalDelta(load("Arma.pinv"), 1e-12));
  }

}
