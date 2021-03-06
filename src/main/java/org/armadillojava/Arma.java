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

import java.util.Arrays;
import java.util.Comparator;

import org.netlib.util.doubleW;
import org.netlib.util.intW;

import com.github.fommil.netlib.BLAS;
import com.github.fommil.netlib.LAPACK;

/**
 * Provides stand-alone linear algebra methods.
 * <p>
 * The interfaces are similar to the Armadillo C++ API by Conrad Sanderson et al., NICTA, Australia.
 * <p>
 * If not stated otherwise (marked as non-canonical), the provided interfaces are identical to Armadillo C++ (e.g. same
 * ordering of arguments, accepted values, ...).
 * 
 * @author Sebastian Niemann
 * 
 * @see <a href="http://arma.sourceforge.net/">Armadillo C++ Algebra Library</a>
 */
public class Arma {

  /**
   * Returns a matrix with the specified number of rows and columns and all elements along the main diagonal set to 1
   * and all others to 0.
   * 
   * @param n_rows The number of rows
   * @param n_cols The number of columns
   * 
   * @return TODO
   * 
   * @throws NegativeArraySizeException The specified number of rows ({@code n_rows}) must be positive.
   * @throws NegativeArraySizeException The specified number of columns ({@code n_cols}) must be positive.
   */
  public static Mat eye(final int n_rows, final int n_cols) throws NegativeArraySizeException {
    /*
     * The parameters "n_rows" and "n_cols" are validated within Mat(int, int, Fill).
     */
    return new Mat(n_rows, n_cols, Fill.EYE);
  }

  /**
   * Returns a vector/matrix with 100 elements linear increasing from the specified value to start with to the specified
   * value to end with (both included).
   * <p>
   * If a non-vector type is to be returned, it will be in the shape of a column vector.
   * 
   * @param <T> TODO
   * @param return_type The type of vector/matrix to be returned
   * @param start The first value
   * @param end The last value
   * 
   * @return TODO
   * 
   * @throws RuntimeException The specified value to start with ({@code start}) must be less than or equal the specified
   *           value to end with ({@code end}).
   */
  public static <T extends AbstractMat> T linspace(final Class<T> return_type, final double start, final double end) throws RuntimeException {
    /*
     * The parameters "start" and "end" are validated within linspace(Class<T>, double, double, int).
     */
    return linspace(return_type, start, end, 100);
  }

  /**
   * Returns a vector/matrix with the specified number of elements linear increasing from the specified value to start
   * with to the specified value to end with (both included).
   * <p>
   * If a matrix type is specified as return type, it will be in the shape of a column vector.
   * 
   * @param <T> TODO
   * @param return_type The type of vector/matrix to be returned
   * @param start The value to start with
   * @param end The value to end with
   * @param N The number of elements
   * 
   * @return TODO
   * 
   * @throws RuntimeException TODO
   * @throws NegativeArraySizeException The specified number of elements ({@code N}) must be positive.
   */
  public static <T extends AbstractMat> T linspace(final Class<T> return_type, final double start, final double end, final int N) throws RuntimeException, NegativeArraySizeException {
    /*
     * The parameter "N" is validated within set_size(int).
     */
    T result;

    try {
      result = return_type.newInstance();
    } catch(InstantiationException | IllegalAccessException e) {
      throw new RuntimeException("Internal Exception. I would greatly appreciate an e-mail containing some information about this problem.");
    }

    result.set_size(N);

    if (N > 0) {
      double stepLength = (end - start) / (result.n_elem - 1);
      for (int n = 0; n < result.n_elem - 1; n++) {
        /*
         * While increasing the value step by step per stepLength will be faster, it will also reduce the precision.
         */
        result._data[n] = start + stepLength * n;
      }
      /*
       * This way, the first and last value will be exactly the same as provided, without any additional loss of
       * precision.
       */
      result._data[result.n_elem - 1] = end;
    }

    return result;
  }

  /**
   * Returns a vector of ones with the specified number of elements.
   * 
   * @param <T> TODO
   * @param return_type The type of vector to be returned
   * @param n_elem The number of elements
   * 
   * @return TODO
   * 
   * @throws NegativeArraySizeException The specified number of elements ({@code n_elem}) must be positive.
   */
  public static <T extends AbstractVector> T ones(final Class<T> return_type, final int n_elem) throws NegativeArraySizeException {
    /*
     * The parameter "n_elem" is validated within set_size(int).
     */

    T result;

    try {
      result = return_type.newInstance();
    } catch(InstantiationException | IllegalAccessException e) {
      throw new RuntimeException("Internal Exception. I would greatly appreciate an e-mail containing some information about this problem.");
    }

    result.set_size(n_elem);
    result.fill(1);

    return result;
  }

  /**
   * Returns a matrix of ones with the specified number of rows and columns.
   * 
   * @param n_rows The number of rows
   * @param n_cols The number of columns
   * 
   * @return TODO
   * 
   * @throws NegativeArraySizeException The specified number of rows ({@code n_rows}) must be positive.
   * @throws NegativeArraySizeException The specified number of columns ({@code n_cols}) must be positive.
   */
  public static Mat ones(final int n_rows, final int n_cols) throws NegativeArraySizeException {
    /*
     * The parameters "n_rows" and "n_cols" are validated within Mat(int, int, Fill).
     */

    return new Mat(n_rows, n_cols, Fill.ONES);
  }

  /**
   * Returns a vector with the specified number of elements and all values drawn from the discrete uniform distribution
   * [0, Integer.MAX_VALUE - 1].
   * 
   * @param <T> TODO
   * @param return_type The type of vector to be returned
   * @param n_elem The number of elements
   * 
   * @return TODO
   * 
   * @throws NegativeArraySizeException The specified number of elements ({@code n_elem}) must be positive.
   */
  public static <T extends AbstractVector> T randi(final Class<T> return_type, final int n_elem) throws NegativeArraySizeException {
    /*
     * The parameter "n_elem" is validated within randi(Class<T>, int, DistrParam).
     */

    return randi(return_type, n_elem, new DistrParam(0, Integer.MAX_VALUE - 1));
  }

  /**
   * Returns a vector with the specified number of elements and values drawn from the discrete uniform distribution [
   * {@code distr_param._a}, {@code distr_param._b}].
   * 
   * @param <T> TODO
   * @param return_type The type of vector to be returned
   * @param n_elem The number of elements
   * @param distr_param The distribution parameter
   * 
   * @return TODO
   * 
   * @throws NegativeArraySizeException The specified number of elements ({@code n_elem}) must be positive.
   * @throws RuntimeException The first end point ({@code distr_param._a}) of the interval must be less than or equal the last end point
   *           ({@code distr_param._b}).
   * @throws RuntimeException The difference between the first end point ({@code distr_param._a}) and the last end point
   *           ({@code distr_param._b}) can be at most Integer.MAX_VALUE - 1.
   */
  public static <T extends AbstractVector> T randi(final Class<T> return_type, final int n_elem, final DistrParam distr_param) throws NegativeArraySizeException, RuntimeException {
    /*
     * The parameter "n_elem" is validated within set_size(int).
     */

    if (distr_param._b < distr_param._a) {
      throw new RuntimeException("The first end point (" + distr_param._a + ") of the interval must be less than or equal the last end point (" + distr_param._a + ").");
    }

    if (distr_param._b - distr_param._a > Integer.MAX_VALUE - 1) {
      throw new RuntimeException("The difference between the first end point (" + distr_param._a + ") and the last end point (" + distr_param._b + ") can be at most Integer.MAX_VALUE - 1.");
    }

    T result;

    try {
      result = return_type.newInstance();
    } catch(InstantiationException | IllegalAccessException e) {
      throw new RuntimeException("Internal Exception. I would greatly appreciate an e-mail containing some information about this problem.");
    }

    result.set_size(n_elem);

    for (int n = 0; n < result.n_elem; n++) {
      result._data[n] = distr_param._a + RNG._rng.nextInt(distr_param._b - distr_param._a + 1);
    }

    return result;
  }

  /**
   * Returns a matrix with the specified number of rows and columns and values drawn from the discrete uniform
   * distribution [0, Integer.MAX_VALUE - 1].
   * 
   * @param n_rows The number of rows
   * @param n_cols The number of columns
   * 
   * @return TODO
   * 
   * @throws NegativeArraySizeException The specified number of rows ({@code n_rows}) must be positive.
   * @throws NegativeArraySizeException The specified number of columns ({@code n_cols}) must be positive.
   */
  public static Mat randi(final int n_rows, final int n_cols) throws NegativeArraySizeException {
    /*
     * The parameters "n_rows" and "n_cols" are validated within randi(Class<T>, int, int, DistrParam).
     */

    return randi(n_rows, n_cols, new DistrParam(0, Integer.MAX_VALUE - 1));
  }

  /**
   * Returns a matrix with the specified number of rows and columns and values drawn from the discrete uniform
   * distribution [{@code distr_param._a}, {@code distr_param._b}].
   * 
   * @param n_rows The number of rows
   * @param n_cols The number of columns
   * @param distr_param The distribution parameter
   * 
   * @return TODO
   * 
   * @throws NegativeArraySizeException The specified number of rows ({@code n_rows}) must be positive.
   * @throws NegativeArraySizeException The specified number of columns ({@code n_cols}) must be positive.
   * @throws RuntimeException The first end point ({@code distr_param._a}) of the interval must be less than or equal the last end point
   *           ({@code distr_param._b}).
   * @throws RuntimeException The difference between the first end point ({@code distr_param._a}) and the last end point
   *           ({@code distr_param._b}) can be at most Integer.MAX_VALUE - 1.
   */
  public static Mat randi(final int n_rows, final int n_cols, final DistrParam distr_param) throws NegativeArraySizeException, RuntimeException {
    /*
     * The parameters "n_rows" and "n_cols" are validated within Mat(int, int).
     */

    if (distr_param._b < distr_param._a) {
      throw new RuntimeException("The first end point (" + distr_param._a + ") of the interval must be less than or equal the last end point (" + distr_param._a + ").");
    }

    if (distr_param._b - distr_param._a > Integer.MAX_VALUE - 1) {
      throw new RuntimeException("The difference between the first end point (" + distr_param._a + ") and the last end point (" + distr_param._b + ") can be at most Integer.MAX_VALUE - 1.");
    }

    Mat result = new Mat(n_rows, n_cols);

    for (int n = 0; n < result.n_elem; n++) {
      result._data[n] = distr_param._a + RNG._rng.nextInt(distr_param._b - distr_param._a + 1);
    }

    return result;
  }

  /**
   * Returns a vector with the specified number of elements and values drawn from the standard uniform
   * distribution on the left-closed and right-open interval [0,1).
   * <p>
   * <b>Non-canonical:</b> Drawn from [0,1) instead of the closed interval [0,1].
   * 
   * @param <T> TODO
   * @param return_type The type of vector to be returned
   * @param n_elem The number of elements
   * 
   * @return TODO
   * 
   * @throws NegativeArraySizeException The specified number of elements ({@code n_elem}) must be positive.
   */
  public static <T extends AbstractVector> T randu(final Class<T> return_type, final int n_elem) throws NegativeArraySizeException {
    /*
     * The parameter "n_elem" is validated within set_size(int).
     */

    T result;

    try {
      result = return_type.newInstance();
    } catch(InstantiationException | IllegalAccessException e) {
      throw new RuntimeException("Internal Exception. I would greatly appreciate an e-mail containing some information about this problem.");
    }

    result.set_size(n_elem);
    result.randu();

    return result;
  }

  /**
   * Returns a matrix with the specified number of rows and columns and values drawn from the standard uniform
   * distribution on the left-closed and right-open interval [0,1).
   * <p>
   * <b>Non-canonical:</b> Drawn from [0,1) instead of the closed interval [0,1].
   * 
   * @param n_rows The number of rows
   * @param n_cols The number of columns
   * 
   * @return TODO
   * 
   * @throws NegativeArraySizeException The specified number of rows ({@code n_rows}) must be positive.
   * @throws NegativeArraySizeException The specified number of columns ({@code n_cols}) must be positive.
   */
  public static Mat randu(final int n_rows, final int n_cols) throws NegativeArraySizeException {
    /*
     * The parameters "n_rows" and "n_cols" are validated within Mat(int, int, Fill).
     */

    return new Mat(n_rows, n_cols, Fill.RANDU);
  }

  /**
   * Returns a vector with the specified number of elements and values drawn from the standard normal
   * distribution with mean 0.0 and standard deviation 1.0.
   * 
   * @param <T> TODO
   * @param return_type The type of vector to be returned
   * @param n_elem The number of elements
   * 
   * @return TODO
   * 
   * @throws NegativeArraySizeException The specified number of elements ({@code n_elem}) must be positive.
   */
  public static <T extends AbstractVector> T randn(final Class<T> return_type, final int n_elem) throws NegativeArraySizeException {
    /*
     * The parameter "n_elem" is validated within set_size(int).
     */

    T result;

    try {
      result = return_type.newInstance();
    } catch(InstantiationException | IllegalAccessException e) {
      throw new RuntimeException("Internal Exception. I would greatly appreciate an e-mail containing some information about this problem.");
    }

    result.set_size(n_elem);
    result.randn();

    return result;
  }

  /**
   * Returns a matrix with the specified number of rows and columns and values drawn from the standard normal
   * distribution with mean 0.0 and standard deviation 1.0.
   * 
   * @param n_rows The number of rows
   * @param n_cols The number of columns
   * 
   * @return TODO
   * 
   * @throws NegativeArraySizeException The specified number of rows ({@code n_rows}) must be positive.
   * @throws NegativeArraySizeException The specified number of columns ({@code n_cols}) must be positive.
   */
  public static Mat randn(final int n_rows, final int n_cols) throws NegativeArraySizeException {
    /*
     * The parameters "n_rows" and "n_cols" are validated within Mat(int, int).
     */

    return new Mat(n_rows, n_cols, Fill.RANDN);
  }

  /**
   * Creates a matrix by replicating the matrix by the specified times per row and column.
   * 
   * @param A The matrix
   * @param num_copies_per_row The number of copies per row
   * @param num_copies_per_col The number of copies per column
   * 
   * @return TODO
   * 
   * @throws NegativeArraySizeException The specified number of copies per row ({@code num_copies_per_row}) must be
   *           positive.
   * @throws NegativeArraySizeException The specified number of copies per column ({@code num_copies_per_col}) must be
   *           positive.
   */
  public static Mat repmat(final AbstractMat A, final int num_copies_per_row, final int num_copies_per_col) throws NegativeArraySizeException {
    if (num_copies_per_row < 0) {
      throw new NegativeArraySizeException("The specified number of copies per row (" + num_copies_per_row + ") must be positive.");
    }

    if (num_copies_per_col < 0) {
      throw new NegativeArraySizeException("The specified number of copies per column (" + num_copies_per_col + ") must be positive.");
    }

    Mat result = new Mat(A.n_rows * num_copies_per_row, A.n_cols * num_copies_per_col);

    if (!A.is_empty()) {
      /*
       * First, copy "A.n_cols" columns alongside the rows ...
       */
      for (int i = 0; i < num_copies_per_row; i++) {
        new ViewSubMat(result, i * A.n_rows, 0, A.n_rows, A.n_cols).inPlace(Op.EQUAL, A);
      }

      /*
       * ... Secondly, copy "A.n_rows * num_copies_per_row" rows alongside the columns.
       * Copying alongside the columns is way faster and should therefore handle the largest amount of data to be
       * copied.
       */
      for (int j = 0; j < num_copies_per_col; j++) {
        System.arraycopy(result._data, 0, result._data, j * result.n_rows * A.n_cols, result.n_rows * A.n_cols);
      }
    }

    return result;
  }

  /**
   * Returns a toeplitz matrix with the provided vector as the first column of the matrix.
   * 
   * @param A The vector
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code A.n_rows}, {@code A.n_cols})-matrix must have at least one element.
   * @throws RuntimeException The provided ({@code A.n_rows}, {@code A.n_cols})-matrix must be equivalent in shape to a
   *           vector.
   */
  public static Mat toeplitz(final AbstractVector A) throws RuntimeException {
    if (A.is_empty()) {
      throw new RuntimeException("The provided (" + A.n_rows + ", " + A.n_cols + ")-matrix must have at least one element.");
    }

    if (!A.is_vec()) {
      throw new RuntimeException("The provided (" + A.n_rows + ", " + A.n_cols + ")-matrix must be equivalent in shape to a vector.");
    }

    Mat result = new Mat(A.n_elem, A.n_elem);

    new ViewDiag(result, 0).fill(A._data[0]);
    for (int n = 1; n < A.n_elem; n++) {
      double value = A._data[n];

      new ViewDiag(result, n).fill(value);
      new ViewDiag(result, -n).fill(value);
    }

    return result;
  }

  /**
   * Returns a toeplitz matrix with the first provided vector as the first column and the second one as the first row of
   * the matrix.
   * 
   * @param A The first vector
   * @param B The second vector
   * 
   * @return TODO
   * 
   * @throws RuntimeException The first provided ({@code A.n_rows}, {@code A.n_cols})-matrix must have at least one
   *           element.
   * @throws RuntimeException The second provided ({@code B.n_rows}, {@code B.n_cols})-matrix must have at least one
   *           element.
   * @throws RuntimeException The first provided ({@code A.n_rows}, {@code A.n_cols})-matrix must be equivalent in shape
   *           to a vector.
   * @throws RuntimeException The second provided ({@code B.n_rows}, {@code B.n_cols})-matrix must be equivalent in
   *           shape to a vector.
   */
  public static Mat toeplitz(final AbstractVector A, final AbstractVector B) throws RuntimeException {
    if (A.is_empty()) {
      throw new RuntimeException("The first provided (" + A.n_rows + ", " + A.n_cols + ")-matrix must have at least one element.");
    }

    if (B.is_empty()) {
      throw new RuntimeException("The second provided (" + B.n_rows + ", " + B.n_cols + ")-matrix must have at least one element.");
    }

    if (!A.is_vec()) {
      throw new RuntimeException("The first provided (" + A.n_rows + ", " + A.n_cols + ")-matrix must be equivalent in shape to a vector.");
    }

    if (!B.is_vec()) {
      throw new RuntimeException("The second provided (" + B.n_rows + ", " + B.n_cols + ")-matrix must be equivalent in shape to a vector.");
    }

    Mat result = new Mat(A.n_elem, B.n_elem);

    new ViewDiag(result, 0).fill(A._data[0]);
    for (int n = 1; n < A.n_elem; n++) {
      new ViewDiag(result, -n).fill(A._data[n]);
    }

    for (int n = 1; n < B.n_elem; n++) {
      new ViewDiag(result, n).fill(B._data[n]);
    }

    return result;
  }

  /**
   * Returns a circulant toeplitz matrix with the provided vector as the first column of the matrix.
   * 
   * @param A The vector
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code A.n_rows}, {@code A.n_cols})-matrix must have at least one element.
   * @throws RuntimeException The provided ({@code A.n_rows}, {@code A.n_cols})-matrix must be equivalent in shape to a
   *           vector.
   */
  public static Mat circ_toeplitz(final AbstractVector A) throws RuntimeException {
    if (A.is_empty()) {
      throw new RuntimeException("The provided (" + A.n_rows + ", " + A.n_cols + ")-matrix must have at least one element.");
    }

    if (!A.is_vec()) {
      throw new RuntimeException("The provided (" + A.n_rows + ", " + A.n_cols + ")-matrix must be equivalent in shape to a vector.");
    }

    Mat result = new Mat(A.n_elem, A.n_elem);

    new ViewDiag(result, 0).fill(A._data[0]);
    if (A.is_rowvec()) {
      for (int n = 1; n < A.n_elem; n++) {
        double value = A._data[n];

        new ViewDiag(result, n).fill(value);
        new ViewDiag(result, n - A.n_elem).fill(value);
      }
    } else {
      for (int n = 1; n < A.n_elem; n++) {
        double value = A._data[n];

        new ViewDiag(result, A.n_elem - n).fill(value);
        new ViewDiag(result, -n).fill(value);
      }
    }

    return result;
  }

  /**
   * Returns a null vector with the specified number of elements.
   * 
   * @param <T> TODO
   * @param return_type The type of vector to be returned
   * @param n_elem The number of elements
   * 
   * @return TODO
   * 
   * @throws NegativeArraySizeException The specified number of elements ({@code n_elem}) must be positive.
   */
  public static <T extends AbstractVector> T zeros(final Class<T> return_type, final int n_elem) throws NegativeArraySizeException {
    /*
     * The parameter "n_elem" is validated within set_size(int).
     */

    T result;

    try {
      result = return_type.newInstance();
    } catch(InstantiationException | IllegalAccessException e) {
      throw new RuntimeException("Internal Exception. I would greatly appreciate an e-mail containing some information about this problem.");
    }

    /*
     * All uninitialised matrices are already equal to a zero matrix.
     */
    result.set_size(n_elem);

    return result;
  }

  /**
   * Returns a zero matrix with the specified number of rows and columns.
   * 
   * @param n_rows The number of rows
   * @param n_cols The number of columns
   * 
   * @return TODO
   * 
   * @throws NegativeArraySizeException The specified number of rows ({@code n_rows}) must be positive.
   * @throws NegativeArraySizeException The specified number of columns ({@code n_cols}) must be positive.
   */
  public static Mat zeros(final int n_rows, final int n_cols) throws NegativeArraySizeException {
    /*
     * The parameters "n_rows" and "n_cols" are validated within Mat(int, int).
     * All uninitialised matrices are already equal to a zero matrix.
     */

    return new Mat(n_rows, n_cols);
  }

  /**
   * TODO
   * 
   * @param result TODO
   * @param X TODO
   */
  protected static void abs(final double[] result, final double[] X) {
    for (int n = 0; n < X.length; n++) {
      result[n] = Math.abs(X[n]);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed absolute values of the provided one.
   * 
   * @param X The column vector
   * 
   * @return TODO
   */
  public static Col abs(final Col X) {
    Col result = new Col(X.n_elem);
    abs(result._data, X._data);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed absolute values of the provided one.
   * 
   * @param X The row vector
   * 
   * @return TODO
   */
  public static Row abs(final Row X) {
    Row result = new Row(X.n_elem);
    abs(result._data, X._data);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed absolute values of the provided one.
   * 
   * @param X The matrix
   * 
   * @return TODO
   */
  public static Mat abs(final Mat X) {
    Mat result = new Mat(X.n_rows, X.n_cols);
    abs(result._data, X._data);
    return result;
  }

  /**
   * TODO
   * 
   * @param result TODO
   * @param X TODO
   */
  protected static void eps(final double[] result, final double[] X) {
    for (int n = 0; n < X.length; n++) {
      result[n] = Math.ulp(X[n]);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed positive distances between each value and next larger
   * representable of the provided one.
   * 
   * @param X The column vector
   * 
   * @return TODO
   */
  public static Col eps(final Col X) {
    Col result = new Col(X.n_elem);
    eps(result._data, X._data);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed positive distances between each value and next larger
   * representable of the provided one.
   * 
   * @param X The row vector
   * 
   * @return TODO
   */
  public static Row eps(final Row X) {
    Row result = new Row(X.n_elem);
    eps(result._data, X._data);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed positive distances between each value and next larger
   * representable of the provided one.
   * 
   * @param X The matrix
   * 
   * @return TODO
   */
  public static Mat eps(final Mat X) {
    Mat result = new Mat(X.n_rows, X.n_cols);
    eps(result._data, X._data);
    return result;
  }

  /**
   * TODO
   * 
   * @param result TODO
   * @param A TODO
   */
  protected static void exp(final double[] result, final double[] A) {
    for (int n = 0; n < A.length; n++) {
      result[n] = Math.exp(A[n]);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed base-e exponential values of the provided one.
   * 
   * @param A The column vector
   * 
   * @return TODO
   */
  public static Col exp(final Col A) {
    Col result = new Col(A.n_elem);
    exp(result._data, A._data);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed base-e exponential values of the provided one.
   * 
   * @param A The row vector
   * 
   * @return TODO
   */
  public static Row exp(final Row A) {
    Row result = new Row(A.n_elem);
    exp(result._data, A._data);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed base-e exponential values of the provided one.
   * 
   * @param A The matrix
   * 
   * @return TODO
   */
  public static Mat exp(final Mat A) {
    Mat result = new Mat(A.n_rows, A.n_cols);
    exp(result._data, A._data);
    return result;
  }

  /**
   * TODO
   * 
   * @param result TODO
   * @param A TODO
   */
  protected static void exp2(final double[] result, final double[] A) {
    for (int n = 0; n < A.length; n++) {
      result[n] = Math.pow(2, A[n]);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed base-2 exponential values of the provided one.
   * 
   * @param A The column vector
   * 
   * @return TODO
   */
  public static Col exp2(final Col A) {
    Col result = new Col(A.n_elem);
    exp2(result._data, A._data);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed base-2 exponential values of the provided one.
   * 
   * @param A The row vector
   * 
   * @return TODO
   */
  public static Row exp2(final Row A) {
    Row result = new Row(A.n_elem);
    exp2(result._data, A._data);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed base-2 exponential values of the provided one.
   * 
   * @param A The matrix
   * 
   * @return TODO
   */
  public static Mat exp2(final Mat A) {
    Mat result = new Mat(A.n_rows, A.n_cols);
    exp2(result._data, A._data);
    return result;
  }

  /**
   * TODO
   * 
   * @param result TODO
   * @param A TODO
   */
  protected static void exp10(final double[] result, final double[] A) {
    for (int n = 0; n < A.length; n++) {
      result[n] = Math.pow(10, A[n]);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed base-10 exponential values of the provided one.
   * 
   * @param A The column vector
   * 
   * @return TODO
   */
  public static Col exp10(final Col A) {
    Col result = new Col(A.n_elem);
    exp10(result._data, A._data);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed base-10 exponential values of the provided one.
   * 
   * @param A The row vector
   * 
   * @return TODO
   */
  public static Row exp10(final Row A) {
    Row result = new Row(A.n_elem);
    exp10(result._data, A._data);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed base-10 exponential values of the provided one.
   * 
   * @param A The matrix
   * 
   * @return TODO
   */
  public static Mat exp10(final Mat A) {
    Mat result = new Mat(A.n_rows, A.n_cols);
    exp10(result._data, A._data);
    return result;
  }

  /**
   * TODO
   * 
   * @param result TODO
   * @param A TODO
   */
  protected static void trunc_exp(final double[] result, final double[] A) {
    for (int n = 0; n < A.length; n++) {
      double exp = Math.exp(A[n]);

      if (!Double.isInfinite(exp)) {
        result[n] = exp;
      } else {
        result[n] = Double.MAX_VALUE;
      }
    }
  }

  /**
   * Returns a column vector made of the element-wise computed base-e exponential values of the provided one.
   * <p>
   * Elements that would return a value of infinity are truncated to the largest representable number.
   * 
   * @param A The column vector
   * 
   * @return TODO
   */
  public static Col trunc_exp(final Col A) {
    Col result = new Col(A.n_elem);
    trunc_exp(result._data, A._data);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed base-e exponential values of the provided one.
   * <p>
   * Elements that would return a value of infinity are truncated to the largest representable number.
   * 
   * @param A The row vector
   * 
   * @return TODO
   */
  public static Row trunc_exp(final Row A) {
    Row result = new Row(A.n_elem);
    trunc_exp(result._data, A._data);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed base-e exponential values of the provided one.
   * <p>
   * Elements that would return a value of infinity are truncated to the largest representable number.
   * 
   * @param A The matrix
   * 
   * @return TODO
   */
  public static Mat trunc_exp(final Mat A) {
    Mat result = new Mat(A.n_rows, A.n_cols);
    trunc_exp(result._data, A._data);
    return result;
  }

  /**
   * TODO
   * 
   * @param result TODO
   * @param A TODO
   */
  protected static void log(final double[] result, final double[] A) {
    for (int n = 0; n < A.length; n++) {
      double value = A[n];

      if (value < 0) {
        result[n] = Datum.nan;
      } else {
        result[n] = Math.log(A[n]);
      }
    }
  }

  /**
   * Returns a column vector made of the element-wise computed base-e logarithmic values of the provided one.
   * 
   * @param A The column vector
   * 
   * @return TODO
   */
  public static Col log(final Col A) {
    Col result = new Col(A.n_elem);
    log(result._data, A._data);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed base-e logarithmic values of the provided one.
   * 
   * @param A The row vector
   * 
   * @return TODO
   */
  public static Row log(final Row A) {
    Row result = new Row(A.n_elem);
    log(result._data, A._data);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed base-e logarithmic values of the provided one.
   * 
   * @param A The matrix
   * 
   * @return TODO
   */
  public static Mat log(final Mat A) {
    Mat result = new Mat(A.n_rows, A.n_cols);
    log(result._data, A._data);
    return result;
  }

  /**
   * TODO
   * 
   * @param result TODO
   * @param A TODO
   */
  protected static void log2(final double[] result, final double[] A) {
    for (int n = 0; n < A.length; n++) {
      double value = A[n];

      if (value < 0) {
        result[n] = Datum.nan;
      } else {
        result[n] = Math.log(A[n]) / Math.log(2);
      }
    }
  }

  /**
   * Returns a column vector made of the element-wise computed base-2 logarithmic values of the provided one.
   * 
   * @param A The column vector
   * 
   * @return TODO
   */
  public static Col log2(final Col A) {
    Col result = new Col(A.n_elem);
    log2(result._data, A._data);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed base-2 logarithmic values of the provided one.
   * 
   * @param A The row vector
   * 
   * @return TODO
   */
  public static Row log2(final Row A) {
    Row result = new Row(A.n_elem);
    log2(result._data, A._data);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed base-2 logarithmic values of the provided one.
   * 
   * @param A The matrix
   * 
   * @return TODO
   */
  public static Mat log2(final Mat A) {
    Mat result = new Mat(A.n_rows, A.n_cols);
    log2(result._data, A._data);
    return result;
  }

  /**
   * TODO
   * 
   * @param result TODO
   * @param A TODO
   */
  protected static void log10(final double[] result, final double[] A) {
    for (int n = 0; n < A.length; n++) {
      double value = A[n];

      if (value < 0) {
        result[n] = Datum.nan;
      } else {
        result[n] = Math.log10(A[n]);
      }
    }
  }

  /**
   * Returns a column vector made of the element-wise computed base-10 logarithmic values of the provided one.
   * 
   * @param A The column vector
   * 
   * @return TODO
   */
  public static Col log10(final Col A) {
    Col result = new Col(A.n_elem);
    log10(result._data, A._data);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed base-10 logarithmic values of the provided one.
   * 
   * @param A The row vector
   * 
   * @return TODO
   */
  public static Row log10(final Row A) {
    Row result = new Row(A.n_elem);
    log10(result._data, A._data);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed base-10 logarithmic values of the provided one.
   * 
   * @param A The matrix
   * 
   * @return TODO
   */
  public static Mat log10(final Mat A) {
    Mat result = new Mat(A.n_rows, A.n_cols);
    log10(result._data, A._data);
    return result;
  }

  /**
   * TODO
   * 
   * @param result TODO
   * @param A TODO
   */
  protected static void trunc_log(final double[] result, final double[] A) {
    for (int n = 0; n < A.length; n++) {
      double value = A[n];

      if (value <= 0) {
        value = Double.MIN_NORMAL;
      } else if (Double.isInfinite(value)) {
        value = Double.MAX_VALUE;
      }

      result[n] = Math.log(value);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed base-e logarithmic values of the provided one.
   * <p>
   * Elements that would return a value of infinity are truncated to the base-e logarithmic of the largest representable
   * number and values that would return a complex number are truncated to the base-e logarithmic of the smallest
   * representable number.
   * 
   * @param A The column vector
   * 
   * @return TODO
   */
  public static Col trunc_log(final Col A) {
    Col result = new Col(A.n_elem);
    trunc_log(result._data, A._data);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed base-e logarithmic values of the provided one.
   * <p>
   * Elements that would return a value of infinity are truncated to the base-e logarithmic of the largest representable
   * number and values that would return a complex number are truncated to the base-e logarithmic of the smallest
   * representable number.
   * 
   * @param A The row vector
   * 
   * @return TODO
   */
  public static Row trunc_log(final Row A) {
    Row result = new Row(A.n_elem);
    trunc_log(result._data, A._data);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed base-e logarithmic values of the provided one.
   * <p>
   * Elements that would return a value of infinity are truncated to the base-e logarithmic of the largest representable
   * number and values that would return a complex number are truncated to the base-e logarithmic of the smallest
   * representable number.
   * 
   * @param A The matrix
   * 
   * @return TODO
   */
  public static Mat trunc_log(final Mat A) {
    Mat result = new Mat(A.n_rows, A.n_cols);
    trunc_log(result._data, A._data);
    return result;
  }

  /**
   * TODO
   * 
   * @param result TODO
   * @param A TODO
   * @param p TODO
   */
  protected static void pow(final double[] result, final double[] A, final double p) {
    for (int n = 0; n < A.length; n++) {
      result[n] = Math.pow(A[n], p);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed raise to the specified power of the provided one.
   * 
   * @param A The column vector
   * @param p The power
   * 
   * @return TODO
   * 
   * @throws RuntimeException The power ({@code p}) must be positive.
   */
  public static Col pow(final Col A, final double p) throws RuntimeException {
    if (p < 0) {
      throw new RuntimeException("The power (" + p + ") must be positive");
    }

    Col result = new Col(A.n_elem);
    pow(result._data, A._data, p);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed raise to the specified power of the provided one.
   * 
   * @param A The row vector
   * @param p The power
   * 
   * @return TODO
   * 
   * @throws RuntimeException The power ({@code p}) must be positive.
   */
  public static Row pow(final Row A, final double p) throws RuntimeException {
    if (p < 0) {
      throw new RuntimeException("The power (" + p + ") must be positive");
    }

    Row result = new Row(A.n_elem);
    pow(result._data, A._data, p);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed raise to the specified power of the provided one.
   * 
   * @param A The matrix
   * @param p The power
   * 
   * @return TODO
   * 
   * @throws RuntimeException The power ({@code p}) must be positive.
   */
  public static Mat pow(final Mat A, final double p) throws RuntimeException {
    if (p < 0) {
      throw new RuntimeException("The power (" + p + ") must be positive");
    }

    Mat result = new Mat(A.n_rows, A.n_cols);
    pow(result._data, A._data, p);
    return result;
  }

  /**
   * TODO
   * 
   * @param result TODO
   * @param A TODO
   */
  protected static void sqrt(final double[] result, final double[] A) {
    for (int n = 0; n < A.length; n++) {
      double value = A[n];

      if (value < 0) {
        result[n] = Datum.nan;
      } else {
        result[n] = Math.sqrt(A[n]);
      }
    }
  }

  /**
   * Returns a column vector made of the element-wise computed square root of the provided one.
   * 
   * @param A The column vector
   * 
   * @return TODO
   */
  public static Col sqrt(final Col A) {
    Col result = new Col(A.n_elem);
    sqrt(result._data, A._data);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed square root of the provided one.
   * 
   * @param A The row vector
   * 
   * @return TODO
   */
  public static Row sqrt(final Row A) {
    Row result = new Row(A.n_elem);
    sqrt(result._data, A._data);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed square root of the provided one.
   * 
   * @param A The matrix
   * 
   * @return TODO
   */
  public static Mat sqrt(final Mat A) {
    Mat result = new Mat(A.n_rows, A.n_cols);
    sqrt(result._data, A._data);
    return result;
  }

  /**
   * TODO
   * 
   * @param result TODO
   * @param A TODO
   */
  protected static void square(final double[] result, final double[] A) {
    for (int n = 0; n < A.length; n++) {
      result[n] = Math.pow(A[n], 2);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed squared values of the provided one.
   * 
   * @param A The column vector
   * 
   * @return TODO
   */
  public static Col square(final Col A) {
    Col result = new Col(A.n_elem);
    square(result._data, A._data);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed squared values of the provided one.
   * 
   * @param A The row vector
   * 
   * @return TODO
   */
  public static Row square(final Row A) {
    Row result = new Row(A.n_elem);
    square(result._data, A._data);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed squared values of the provided one.
   * 
   * @param A The matrix
   * 
   * @return TODO
   */
  public static Mat square(final Mat A) {
    Mat result = new Mat(A.n_rows, A.n_cols);
    square(result._data, A._data);
    return result;
  }

  /**
   * TODO
   * 
   * @param result TODO
   * @param A TODO
   */
  protected static void floor(final double[] result, final double[] A) {
    for (int n = 0; n < A.length; n++) {
      result[n] = Math.floor(A[n]);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed floored values of the provided one.
   * 
   * @param A The column vector
   * 
   * @return TODO
   */
  public static Col floor(final Col A) {
    Col result = new Col(A.n_elem);
    floor(result._data, A._data);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed floored values of the provided one.
   * 
   * @param A The row vector
   * 
   * @return TODO
   */
  public static Row floor(final Row A) {
    Row result = new Row(A.n_elem);
    floor(result._data, A._data);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed floored values of the provided one.
   * 
   * @param A The matrix
   * 
   * @return TODO
   */
  public static Mat floor(final Mat A) {
    Mat result = new Mat(A.n_rows, A.n_cols);
    floor(result._data, A._data);
    return result;
  }

  /**
   * TODO
   * 
   * @param result TODO
   * @param A TODO
   */
  protected static void ceil(final double[] result, final double[] A) {
    for (int n = 0; n < A.length; n++) {
      result[n] = Math.ceil(A[n]);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed rounded up values of the provided one.
   * 
   * @param A The column vector
   * 
   * @return TODO
   */
  public static Col ceil(final Col A) {
    Col result = new Col(A.n_elem);
    ceil(result._data, A._data);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed rounded up values of the provided one.
   * 
   * @param A The row vector
   * 
   * @return TODO
   */
  public static Row ceil(final Row A) {
    Row result = new Row(A.n_elem);
    ceil(result._data, A._data);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed rounded up values of the provided one.
   * 
   * @param A The matrix
   * 
   * @return TODO
   */
  public static Mat ceil(final Mat A) {
    Mat result = new Mat(A.n_rows, A.n_cols);
    ceil(result._data, A._data);
    return result;
  }

  /**
   * TODO
   * 
   * @param result TODO
   * @param A TODO
   */
  protected static void round(final double[] result, final double[] A) {
    for (int n = 0; n < A.length; n++) {
      double value = A[n];

      if (Double.isInfinite(value)) {
        result[n] = value;
      } else {
        result[n] = Math.round(A[n]);
      }
    }
  }

  /**
   * Returns a column vector made of the element-wise computed rounded values of the provided one.
   * 
   * @param A The column vector
   * 
   * @return TODO
   */
  public static Col round(final Col A) {
    Col result = new Col(A.n_elem);
    round(result._data, A._data);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed rounded values of the provided one.
   * 
   * @param A The row vector
   * 
   * @return TODO
   */
  public static Row round(final Row A) {
    Row result = new Row(A.n_elem);
    round(result._data, A._data);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed rounded values of the provided one.
   * 
   * @param A The matrix
   * 
   * @return TODO
   */
  public static Mat round(final Mat A) {
    Mat result = new Mat(A.n_rows, A.n_cols);
    round(result._data, A._data);
    return result;
  }

  /**
   * TODO
   * 
   * @param result TODO
   * @param A TODO
   */
  protected static void sign(final double[] result, final double[] A) {
    for (int n = 0; n < A.length; n++) {
      result[n] = Math.signum(A[n]);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed sign values of the provided one.
   * <p>
   * The sign value is -1 for negative, 0 for zero and 1 for non-negative values.
   * 
   * @param A The column vector
   * 
   * @return TODO
   */
  public static Col sign(final Col A) {
    Col result = new Col(A.n_elem);
    sign(result._data, A._data);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed sign values of the provided one.
   * <p>
   * The sign value is -1 for negative, 0 for zero and 1 for non-negative values.
   * 
   * @param A The row vector
   * 
   * @return TODO
   */
  public static Row sign(final Row A) {
    Row result = new Row(A.n_elem);
    sign(result._data, A._data);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed sign values of the provided one.
   * <p>
   * The sign value is -1 for negative, 0 for zero and 1 for non-negative values.
   * 
   * @param A The matrix
   * 
   * @return TODO
   */
  public static Mat sign(final Mat A) {
    Mat result = new Mat(A.n_rows, A.n_cols);
    sign(result._data, A._data);
    return result;
  }

  /**
   * TODO
   * 
   * @param result TODO
   * @param X TODO
   */
  protected static void sin(final double[] result, final double[] X) {
    for (int n = 0; n < X.length; n++) {
      result[n] = Math.sin(X[n]);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed sine of the provided one.
   * 
   * @param X The column vector
   * 
   * @return TODO
   */
  public static Col sin(final Col X) {
    Col result = new Col(X.n_elem);
    sin(result._data, X._data);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed sine of the provided one.
   * 
   * @param X The row vector
   * 
   * @return TODO
   */
  public static Row sin(final Row X) {
    Row result = new Row(X.n_elem);
    sin(result._data, X._data);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed sine of the provided one.
   * 
   * @param X The matrix
   * 
   * @return TODO
   */
  public static Mat sin(final Mat X) {
    Mat result = new Mat(X.n_rows, X.n_cols);
    sin(result._data, X._data);
    return result;
  }

  /**
   * TODO
   * 
   * @param result TODO
   * @param X TODO
   */
  protected static void asin(final double[] result, final double[] X) {
    for (int n = 0; n < X.length; n++) {
      double value = X[n];

      if (value < -1 || value > 1) {
        result[n] = Datum.nan;
      } else {
        result[n] = Math.asin(X[n]);
      }
    }
  }

  /**
   * Returns a column vector made of the element-wise computed inverse sine of the provided one.
   * 
   * @param X The column vector
   * 
   * @return TODO
   */
  public static Col asin(final Col X) {
    Col result = new Col(X.n_elem);
    asin(result._data, X._data);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed inverse sine of the provided one.
   * 
   * @param X The row vector
   * 
   * @return TODO
   */
  public static Row asin(final Row X) {
    Row result = new Row(X.n_elem);
    asin(result._data, X._data);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed inverse sine of the provided one.
   * 
   * @param X The matrix
   * 
   * @return TODO
   */
  public static Mat asin(final Mat X) {
    Mat result = new Mat(X.n_rows, X.n_cols);
    asin(result._data, X._data);
    return result;
  }

  /**
   * TODO
   * 
   * @param result TODO
   * @param X TODO
   */
  protected static void sinh(final double[] result, final double[] X) {
    for (int n = 0; n < X.length; n++) {
      result[n] = Math.sinh(X[n]);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed hyperbolic sine of the provided one.
   * 
   * @param X The column vector
   * 
   * @return TODO
   */
  public static Col sinh(final Col X) {
    Col result = new Col(X.n_elem);
    sinh(result._data, X._data);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed hyperbolic sine of the provided one.
   * 
   * @param X The row vector
   * 
   * @return TODO
   */
  public static Row sinh(final Row X) {
    Row result = new Row(X.n_elem);
    sinh(result._data, X._data);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed hyperbolic sine of the provided one.
   * 
   * @param X The matrix
   * 
   * @return TODO
   */
  public static Mat sinh(final Mat X) {
    Mat result = new Mat(X.n_rows, X.n_cols);
    sinh(result._data, X._data);
    return result;
  }

  /**
   * TODO
   * 
   * @param result TODO
   * @param X TODO
   */
  protected static void asinh(final double[] result, final double[] X) {
    for (int n = 0; n < X.length; n++) {
      double value = X[n];

      if (Double.isInfinite(value)) {
        result[n] = value;
      } else {
        result[n] = Math.log(value + Math.sqrt(Math.pow(value, 2) + 1));
      }
    }
  }

  /**
   * Returns a column vector made of the element-wise computed inverse hyperbolic sine of the provided one.
   * 
   * @param X The column vector
   * 
   * @return TODO
   */
  public static Col asinh(final Col X) {
    Col result = new Col(X.n_elem);
    asinh(result._data, X._data);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed inverse hyperbolic sine of the provided one.
   * 
   * @param X The row vector
   * 
   * @return TODO
   */
  public static Row asinh(final Row X) {
    Row result = new Row(X.n_elem);
    asinh(result._data, X._data);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed inverse hyperbolic sine of the provided one.
   * 
   * @param X The matrix
   * 
   * @return TODO
   */
  public static Mat asinh(final Mat X) {
    Mat result = new Mat(X.n_rows, X.n_cols);
    asinh(result._data, X._data);
    return result;
  }

  /**
   * TODO
   * 
   * @param result TODO
   * @param X TODO
   */
  protected static void cos(final double[] result, final double[] X) {
    for (int n = 0; n < X.length; n++) {
      result[n] = Math.cos(X[n]);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed cosine of the provided one.
   * 
   * @param X The column vector
   * 
   * @return TODO
   */
  public static Col cos(final Col X) {
    Col result = new Col(X.n_elem);
    cos(result._data, X._data);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed cosine of the provided one.
   * 
   * @param X The row vector
   * 
   * @return TODO
   */
  public static Row cos(final Row X) {
    Row result = new Row(X.n_elem);
    cos(result._data, X._data);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed cosine of the provided one.
   * 
   * @param X The matrix
   * 
   * @return TODO
   */
  public static Mat cos(final Mat X) {
    Mat result = new Mat(X.n_rows, X.n_cols);
    cos(result._data, X._data);
    return result;
  }

  /**
   * TODO
   * 
   * @param result TODO
   * @param X TODO
   */
  protected static void acos(final double[] result, final double[] X) {
    for (int n = 0; n < X.length; n++) {
      double value = X[n];

      if (value < -1 || value > 1) {
        result[n] = Datum.nan;
      } else {
        result[n] = Math.acos(X[n]);
      }
    }
  }

  /**
   * Returns a column vector made of the element-wise computed inverse cosine of the provided one.
   * 
   * @param X The column vector
   * 
   * @return TODO
   */
  public static Col acos(final Col X) {
    Col result = new Col(X.n_elem);
    acos(result._data, X._data);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed inverse cosine of the provided one.
   * 
   * @param X The row vector
   * 
   * @return TODO
   */
  public static Row acos(final Row X) {
    Row result = new Row(X.n_elem);
    acos(result._data, X._data);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed inverse cosine of the provided one.
   * 
   * @param X The matrix
   * 
   * @return TODO
   */
  public static Mat acos(final Mat X) {
    Mat result = new Mat(X.n_rows, X.n_cols);
    acos(result._data, X._data);
    return result;
  }

  /**
   * TODO
   * 
   * @param result TODO
   * @param X TODO
   */
  protected static void cosh(final double[] result, final double[] X) {
    for (int n = 0; n < X.length; n++) {
      result[n] = Math.cosh(X[n]);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed hyperbolic cosine of the provided one.
   * 
   * @param X The column vector
   * 
   * @return TODO
   */
  public static Col cosh(final Col X) {
    Col result = new Col(X.n_elem);
    cosh(result._data, X._data);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed hyperbolic cosine of the provided one.
   * 
   * @param X The row vector
   * 
   * @return TODO
   */
  public static Row cosh(final Row X) {
    Row result = new Row(X.n_elem);
    cosh(result._data, X._data);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed hyperbolic cosine of the provided one.
   * 
   * @param X The matrix
   * 
   * @return TODO
   */
  public static Mat cosh(final Mat X) {
    Mat result = new Mat(X.n_rows, X.n_cols);
    cosh(result._data, X._data);
    return result;
  }

  /**
   * TODO
   * 
   * @param result TODO
   * @param X TODO
   */
  protected static void acosh(final double[] result, final double[] X) {
    for (int n = 0; n < X.length; n++) {
      double value = X[n];

      if (value < 1) {
        result[n] = Datum.nan;
      } else {
        result[n] = Math.log(value + Math.sqrt(Math.pow(value, 2) - 1));
      }
    }
  }

  /**
   * Returns a column vector made of the element-wise computed inverse hyperbolic cosine of the provided one.
   * 
   * @param X The column vector
   * 
   * @return TODO
   */
  public static Col acosh(final Col X) {
    Col result = new Col(X.n_elem);
    acosh(result._data, X._data);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed inverse hyperbolic cosine of the provided one.
   * 
   * @param X The row vector
   * 
   * @return TODO
   */
  public static Row acosh(final Row X) {
    Row result = new Row(X.n_elem);
    acosh(result._data, X._data);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed inverse hyperbolic cosine of the provided one.
   * 
   * @param X The matrix
   * 
   * @return TODO
   */
  public static Mat acosh(final Mat X) {
    Mat result = new Mat(X.n_rows, X.n_cols);
    acosh(result._data, X._data);
    return result;
  }

  /**
   * TODO
   * 
   * @param result TODO
   * @param X TODO
   */
  protected static void tan(final double[] result, final double[] X) {
    for (int n = 0; n < X.length; n++) {
      result[n] = Math.tan(X[n]);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed tangent of the provided one.
   * 
   * @param X The column vector
   * 
   * @return TODO
   */
  public static Col tan(final Col X) {
    Col result = new Col(X.n_elem);
    tan(result._data, X._data);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed tangent of the provided one.
   * 
   * @param X The row vector
   * 
   * @return TODO
   */
  public static Row tan(final Row X) {
    Row result = new Row(X.n_elem);
    tan(result._data, X._data);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed tangent of the provided one.
   * 
   * @param X The matrix
   * 
   * @return TODO
   */
  public static Mat tan(final Mat X) {
    Mat result = new Mat(X.n_rows, X.n_cols);
    tan(result._data, X._data);
    return result;
  }

  /**
   * TODO
   * 
   * @param result TODO
   * @param X TODO
   */
  protected static void atan(final double[] result, final double[] X) {
    for (int n = 0; n < X.length; n++) {
      result[n] = Math.atan(X[n]);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed inverse tangent of the provided one.
   * 
   * @param X The column vector
   * 
   * @return TODO
   */
  public static Col atan(final Col X) {
    Col result = new Col(X.n_elem);
    atan(result._data, X._data);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed inverse tangent of the provided one.
   * 
   * @param X The row vector
   * 
   * @return TODO
   */
  public static Row atan(final Row X) {
    Row result = new Row(X.n_elem);
    atan(result._data, X._data);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed inverse tangent of the provided one.
   * 
   * @param X The matrix
   * 
   * @return TODO
   */
  public static Mat atan(final Mat X) {
    Mat result = new Mat(X.n_rows, X.n_cols);
    atan(result._data, X._data);
    return result;
  }

  /**
   * TODO
   * 
   * @param result TODO
   * @param X TODO
   */
  protected static void tanh(final double[] result, final double[] X) {
    for (int n = 0; n < X.length; n++) {
      result[n] = Math.tanh(X[n]);
    }
  }

  /**
   * Returns a column vector made of the element-wise computed hyperbolic tangent of the provided one.
   * 
   * @param X The column vector
   * 
   * @return TODO
   */
  public static Col tanh(final Col X) {
    Col result = new Col(X.n_elem);
    tanh(result._data, X._data);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed hyperbolic tangent of the provided one.
   * 
   * @param X The row vector
   * 
   * @return TODO
   */
  public static Row tanh(final Row X) {
    Row result = new Row(X.n_elem);
    tanh(result._data, X._data);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed hyperbolic tangent of the provided one.
   * 
   * @param X The matrix
   * 
   * @return TODO
   */
  public static Mat tanh(final Mat X) {
    Mat result = new Mat(X.n_rows, X.n_cols);
    tanh(result._data, X._data);
    return result;
  }

  /**
   * TODO
   * 
   * @param result TODO
   * @param X TODO
   */
  protected static void atanh(final double[] result, final double[] X) {
    for (int n = 0; n < X.length; n++) {
      double value = X[n];

      if (value < -1 || value > 1) {
        result[n] = Datum.nan;
      } else {
        result[n] = 0.5 * Math.log((1 + value) / (1 - value));
      }
    }
  }

  /**
   * Returns a column vector made of the element-wise computed inverse hyperbolic tangent of the provided one.
   * 
   * @param X The column vector
   * 
   * @return TODO
   */
  public static Col atanh(final Col X) {
    Col result = new Col(X.n_elem);
    atanh(result._data, X._data);
    return result;
  }

  /**
   * Returns a row vector made of the element-wise computed inverse hyperbolic tangent of the provided one.
   * 
   * @param X The row vector
   * 
   * @return TODO
   */
  public static Row atanh(final Row X) {
    Row result = new Row(X.n_elem);
    atanh(result._data, X._data);
    return result;
  }

  /**
   * Returns a matrix made of the element-wise computed inverse hyperbolic tangent of the provided one.
   * 
   * @param X The matrix
   * 
   * @return TODO
   */
  public static Mat atanh(final Mat X) {
    Mat result = new Mat(X.n_rows, X.n_cols);
    atanh(result._data, X._data);
    return result;
  }

  /**
   * Returns the sum of all elements of the provided matrix.
   * 
   * @param X The matrix
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   */
  public static double accu(final AbstractMat X) throws RuntimeException {
    if (X.is_empty()) {
      throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must have at least one element.");
    }

    double sum = X._data[0];

    for (int n = 1; n < X.n_elem; n++) {
      sum += X._data[n];
    }

    return sum;
  }

  /**
   * Returns the value of the provided (1,1)-matrix as a scalar.
   * 
   * @param X The matrix
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have exactly one element.
   */
  public static double as_scalar(final AbstractMat X) throws RuntimeException {
    if (X.n_elem != 1) {
      throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must have exactly one element.");
    }

    return X._data[0];
  }

  /**
   * Returns the spectral condition number of the provided matrix.
   * 
   * @param A the matrix
   * 
   * @return TODO
   * 
   * @throws RuntimeException TODO
   */
  public static double cond(final Mat A) throws RuntimeException {
    /*
     * The parameter "A" is validated within svd(AbstractMat).
     */

    Col singularValues = new Col();
    if (!svd(singularValues, A)) {
      return Datum.inf;
    }
    return singularValues._data[0] / singularValues._data[singularValues.n_elem - 1];
  }

  /**
   * Returns the determinant of the provided matrix.
   * 
   * @param A The matrix
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code A.n_rows}, {@code A.n_cols})-matrix must have at least one element.
   * @throws RuntimeException The provided ({@code A.n_rows}, {@code A.n_cols})-matrix must be square.
   */
  public static double det(final Mat A) throws RuntimeException {
    if (A.empty()) {
      throw new RuntimeException("The provided (" + A.n_rows + ", " + A.n_cols + ")-matrix must have at least one element.");
    }

    if (!A.is_square()) {
      throw new RuntimeException("The provided (" + A.n_rows + ", " + A.n_cols + ")-matrix must be square.");
    }

    double[] temp = Arrays.copyOf(A._data, A.n_elem);
    int[] pivotIndices = new int[Math.min(A.n_rows, A.n_cols)];
    intW info = new intW(0);

    LAPACK.getInstance().dgetrf(A.n_rows, A.n_cols, temp, A.n_rows, pivotIndices, info);
    if (info.val != 0) {
      return 0;
    }

    double determinant = 1;
    boolean positiveSign = true;

    for (int i = 0; i < A.n_rows; i++) {
      determinant *= temp[i + i * A.n_rows];

      if (i != pivotIndices[i] - 1) {
        positiveSign = !positiveSign;
      }
    }

    return (positiveSign ? determinant : -determinant);
  }

  /**
   * Returns the dot product of the provided vectors.
   * 
   * @param A The first matrix
   * @param B The second matrix
   * 
   * @return TODO
   * 
   * @throws RuntimeException Both provided matrices must have the same number of elements ({@code A.n_elem},
   *           {@code B.n_elem}).
   */
  public static double dot(final AbstractVector A, final AbstractVector B) throws RuntimeException {
    if (A.n_elem != B.n_elem) {
      throw new RuntimeException("Both provided vectors must have the same number of elements (" + A.n_elem + ", " + B.n_elem + ").");
    }

    return BLAS.getInstance().ddot(A.n_elem, A._data, 1, B._data, 1);
  }

  /**
   * Returns the normalised dot product of the provided vectors.
   * 
   * @param A The first matrix
   * @param B The second matrix
   * 
   * @return TODO
   * 
   * @throws RuntimeException Both provided matrices must have the same number of elements ({@code A.n_elem},
   *           {@code B.n_elem}).
   */
  public static double norm_dot(final AbstractVector A, final AbstractVector B) throws RuntimeException {
    /*
     * The parameters "A" and "B" are validated within dot(AbstractMat, AbstractMat).
     */

    return dot(A, B) / Math.sqrt(dot(A, A) * dot(B, B));
  }

  /**
   * Calculates the base-e logarithmic determinant of the provided matrix and stores the value in {@code val} and sign
   * in {@code sign}.
   * <p>
   * <b>Note:</b> Unfortunately, the storage variables must be of the mutable type double[], respectively int[].
   * 
   * @param val The storage of the value
   * @param sign The storage of the sign
   * @param A The matrix
   * 
   * @throws RuntimeException The provided ({@code A.n_rows}, {@code A.n_cols})-matrix must have at least one element.
   * @throws RuntimeException The provided ({@code A.n_rows}, {@code A.n_cols})-matrix must be square.
   * @throws RuntimeException The factorisation could not be completed. The provided matrix appears to be singular.
   */
  public static void log_det(final double[] val, final int[] sign, final Mat A) throws RuntimeException {
    if (A.empty()) {
      throw new RuntimeException("The provided (" + A.n_rows + ", " + A.n_cols + ")-matrix must have at least one element.");
    }

    if (!A.is_square()) {
      throw new RuntimeException("The provided (" + A.n_rows + ", " + A.n_cols + ")-matrix must be square.");
    }

    /*
     * The following computation is based on how its done in Armadillo C++ 4.100.0
     */
    double[] temp = Arrays.copyOf(A._data, A.n_elem);
    int[] pivotIndices = new int[Math.min(A.n_rows, A.n_cols)];
    intW info = new intW(0);

    LAPACK.getInstance().dgetrf(A.n_rows, A.n_cols, temp, A.n_rows, pivotIndices, info);
    if (info.val != 0) {
      val[0] = Double.NEGATIVE_INFINITY;
      sign[0] = 1;
      return;
    }

    double determinant = Math.log(temp[0]);
    boolean positiveSign = (0 != pivotIndices[0] - 1 ^ temp[0] < 0) ? false : true;

    for (int i = 1; i < A.n_rows; i++) {
      double value = temp[i + i * A.n_rows];

      determinant += Math.log(Math.abs(value));

      if (i != pivotIndices[i] - 1 ^ value < 0) {
        positiveSign = !positiveSign;
      }
    }

    val[0] = determinant;
    sign[0] = (positiveSign) ? 1 : -1;
  }

  /**
   * Returns the specified norm of the provided vector.
   * 
   * @param X The vector
   * @param p The norm
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-vector must have at least one element.
   * @throws IllegalArgumentException The norm indicator ({@code p}) must be strict greater than 0.
   */
  public static double norm(final AbstractVector X, final int p) throws RuntimeException, IllegalArgumentException {
    if (X.empty()) {
      throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-vector must have at least one element.");
    }

    double norm = 0;

    if (p < 0) {
      throw new IllegalArgumentException("The norm indicator (" + p + ") must be strict greater than 0.");
    }

    switch (p) {
      case 1:
        for (int n = 0; n < X.n_elem; n++) {
          norm += Math.abs(X._data[n]);
        }
        break;
      case 2:
        for (int n = 0; n < X.n_elem; n++) {
          norm += Math.pow(X._data[n], 2);
        }
        norm = Math.sqrt(norm);
        break;
      default:
        for (int n = 0; n < X.n_elem; n++) {
          norm += Math.pow(Math.abs(X._data[n]), p);
        }
        norm = Math.pow(norm, 1.0 / p);
        break;
    }

    return norm;
  }

  /**
   * Returns the specified norm of the provided matrix.
   * <p>
   * If the provided matrix is not equivalent in shape to a vector, an induced matrix norm is computed.
   * 
   * @param X The matrix
   * @param p The norm
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   * @throws IllegalArgumentException For matrices in vector shape, the norm indicator ({@code p}) must be strict
   *           greater than 0.
   * @throws IllegalArgumentException For matrices in non-vector shape, the norm indicator ({@code p}) must be one of 1
   *           or 2.
   */
  public static double norm(final Mat X, final int p) throws RuntimeException, IllegalArgumentException {
    if (X.empty()) {
      throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must have at least one element.");
    }

    double norm = 0;

    if (X.is_vec()) {
      if (p < 0) {
        throw new IllegalArgumentException("For matrices in vector shape, the norm indicator (" + p + ") must be strict greater than 0.");
      }

      switch (p) {
        case 1:
          for (int n = 0; n < X.n_elem; n++) {
            norm += Math.abs(X._data[n]);
          }
          break;
        case 2:
          for (int n = 0; n < X.n_elem; n++) {
            norm += Math.pow(X._data[n], 2);
          }
          norm = Math.sqrt(norm);
          break;
        default:
          for (int n = 0; n < X.n_elem; n++) {
            norm += Math.pow(Math.abs(X._data[n]), p);
          }
          norm = Math.pow(norm, 1.0 / p);
          break;
      }

    } else {
      switch (p) {
        case 1:
          norm = Double.NEGATIVE_INFINITY;
          for (int j = 0; j < X.n_cols; j++) {
            int columnIndex = j * X.n_rows;
            double sum = 0;
            for (int i = 0; i < X.n_rows; i++) {
              sum += Math.abs(X._data[i + columnIndex]);
            }

            norm = Math.max(norm, sum);
          }
          break;
        case 2:
          Col singularValues = new Col();
          svd(singularValues, X);
          norm = singularValues._data[0];
          break;
        default:
          throw new IllegalArgumentException("For matrices in non-vector shape, the norm indicator (" + p + ") must be one of 1 or 2.");
      }
    }

    return norm;
  }

  /**
   * Returns the specified norm of the provided matrix.
   * <p>
   * If the provided matrix is not equivalent in shape to a vector, an induced matrix norm is computed.
   * 
   * @param X The matrix
   * @param p The norm
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   * @throws IllegalArgumentException For vectors, the norm indicator ({@code p}) must be one of '-inf', 'inf' or 'fro'.
   * @throws IllegalArgumentException For non-vectors, the norm indicator ({@code p}) must be one of 'inf' or 'fro'.
   */
  public static double norm(final AbstractMat X, final String p) throws RuntimeException, IllegalArgumentException {
    if (X.empty()) {
      throw new RuntimeException("The provided matrix must have at least one element.");
    }

    double norm;

    if (X.is_vec()) {
      switch (p) {
        case "-inf":
          norm = Math.abs(X._data[0]);
          for (int n = 1; n < X.n_elem; n++) {
            norm = Math.min(norm, Math.abs(X._data[n]));
          }
          break;
        case "inf":
          norm = Math.abs(X._data[0]);
          for (int n = 1; n < X.n_elem; n++) {
            norm = Math.max(norm, Math.abs(X._data[n]));
          }
          break;
        case "fro":
          norm = 0;
          for (int n = 0; n < X.n_elem; n++) {
            norm += Math.pow(X._data[n], 2);
          }
          norm = Math.sqrt(norm);
          break;
        default:
          throw new IllegalArgumentException("For vectors, the norm indicator (" + p + ") must be one of '-inf', 'inf' or 'fro'.");
      }

    } else {
      switch (p) {
        case "inf":
          norm = Double.NEGATIVE_INFINITY;
          for (int i = 0; i < X.n_rows; i++) {

            double sum = 0;
            for (int j = 0; j < X.n_cols; j++) {
              sum += Math.abs(X._data[i + j * X.n_rows]);
            }

            norm = Math.max(norm, sum);
          }
          break;
        case "fro":
          norm = 0;
          for (int n = 0; n < X.n_elem; n++) {
            norm += Math.pow(X._data[n], 2);
          }
          norm = Math.sqrt(norm);
          break;
        default:
          throw new IllegalArgumentException("For non-vectors, the norm indicator (" + p + ") must be one of 'inf' or 'fro'.");
      }
    }

    return norm;
  }

  /**
   * Returns the rank of the provided matrix based on singular value decomposition.
   * <p>
   * All singular values smaller than {@code Math.max(X.n_rows, X.n_cols) * Math.ulp(norm(X, 2))} are treated as zero.
   * 
   * @param X The matrix
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   */
  public static int rank(final Mat X) throws RuntimeException {
    /*
     * The parameter "X" is validated within rank(AbstractMat, double).
     */

    return rank(X, Math.max(X.n_rows, X.n_cols) * Math.ulp(norm(X, 2)));
  }

  /**
   * Returns the rank of the provided matrix based on singular value decomposition.
   * <p>
   * All singular values smaller than the specified tolerance are treated as zero.
   * 
   * @param X The matrix
   * @param tolerance The tolerance
   * 
   * @return TODO
   * 
   * @throws RuntimeException The specified tolerance must be positive.
   * @throws RuntimeException The calculation could not be completed. The provided matrix appears to be singular.
   */
  public static int rank(final Mat X, double tolerance) throws RuntimeException {
    /*
     * The parameter "X" is validated within svd(AbstractMat).
     */

    if (tolerance < 0) {
      throw new RuntimeException("The specified tolerance must be positive.");
    }
    
    Col singularValues = new Col();
    if (!svd(singularValues, X)) {
      throw new RuntimeException("The calculation could not be completed. The provided matrix appears to be singular.");
    }

    if (tolerance == 0) {
      tolerance = Math.max(X.n_rows, X.n_cols) * Math.ulp(singularValues.max());
    }

    int rank = 0;
    for (int n = 0; n < singularValues.n_elem; n++) {
      if (singularValues._data[n] > tolerance + Datum.eps) {
        rank++;
      }
    }

    return rank;
  }

  /**
   * Returns the trace of the provided square matrix.
   * 
   * @param X The matrix
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must be square.
   */
  public static double trace(final Mat X) throws RuntimeException {
    if (X.is_empty()) {
      throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must have at least one element.");
    }

    if (!X.is_square()) {
      throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must be square.");
    }

    double trace = X._data[0];
    for (int i = 1; i < X.n_rows; i++) {
      trace += X._data[i + i * X.n_rows];
    }

    return trace;
  }

  /**
   * Returns a deep copy of the main diagonal of the provided matrix as a column vector.
   * 
   * @param A The matrix
   * 
   * @return TODO
   */
  public static Col diagvec(final Mat A) {
    return diagvec(A, 0);
  }

  /**
   * Returns a deep copy of the {@code k}th diagonal of the provided matrix as a column vector.
   * <ul>
   * <li>For {@code k} = 0, its the main diagonal.
   * <li>For {@code k} &gt; 0, its the {@code k}th super-diagonal.
   * <li>For {@code k} &lt; 0, its the {@code k}th sub-diagonal.
   * </ul>
   * 
   * @param A The matrix
   * @param k The diagonal position
   * 
   * @return TODO
   * 
   * @throws IndexOutOfBoundsException The diagonal index ({@code k}) is out of bounds.
   */
  public static Col diagvec(final Mat A, int k) throws IndexOutOfBoundsException {
    /*
     * The parameter "k" is validated within A.diag(int).
     */

    return A.diag(k);
  }

  /**
   * Returns the smallest value within the provided vector.
   * 
   * @param V The vector
   * 
   * @return TODO
   * 
   * @throws RuntimeException The ({@code V.n_rows}, {@code V.n_cols})-matrix must have at least one element.
   */
  public static double min(final AbstractVector V) throws RuntimeException {
    /*
     * The parameter "V" is validated within V.min().
     */

    return V.min();
  }

  /**
   * Returns the smallest value for each column of the provided sub view.
   * 
   * @param V The sub view
   * 
   * @return TODO
   */
  protected static double min(final AbstractView V) {

    V.iteratorReset();
    double minimum = V._data[V.iteratorNext()];
    while (V.iteratorHasNext()) {
      minimum = Math.min(minimum, V._data[V.iteratorNext()]);
    }

    return minimum;
  }

  /**
   * Returns the smallest value for each column of the provided matrix.
   * 
   * @param <T> TODO
   * @param return_type The type of vector to be returned
   * @param X The matrix
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   */
  public static <T extends AbstractVector> T min(final Class<T> return_type, final Mat X) throws RuntimeException {
    /*
     * The parameter "V" is validated within min(Class<T>, Mat, int).
     */

    return min(return_type, X, 0);
  }

  /**
   * Returns the smallest value for each column ({@code dim} = 0) or row ({@code dim} = 1) of the provided matrix.
   * 
   * @param <T> TODO
   * @param return_type The type of vector to be returned
   * @param X The matrix
   * @param dim The dimension
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   * @throws IllegalArgumentException The specified dimension ({@code dim}) must either be 0 or 1.
   */
  public static <T extends AbstractVector> T min(final Class<T> return_type, final Mat X, final int dim) throws RuntimeException, IllegalArgumentException {
    if (X.is_empty()) {
      throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must have at least one element.");
    }

    T result;

    try {
      result = return_type.newInstance();
    } catch(InstantiationException | IllegalAccessException e) {
      throw new RuntimeException("Internal Exception. I would greatly appreciate an e-mail containing some information about this problem.");
    }

    switch (dim) {
      case 0:
        result.set_size(X.n_cols);

        for (int j = 0; j < X.n_cols; j++) {
          result._data[j] = min(new ViewSubCol(X, j));
        }
        break;
      case 1:
        result.set_size(X.n_rows);

        for (int i = 0; i < X.n_rows; i++) {
          result._data[i] = min(new ViewSubRow(X, i));
        }
        break;
      default:
        throw new IllegalArgumentException("The specified dimension (" + dim + ") must either be 0 or 1.");
    }

    return result;
  }

  /**
   * Returns the element-wise smallest values between the provided matrices.
   * 
   * @param A The first matrix
   * @param B The second matrix
   * 
   * @return TODO
   * 
   * @throws RuntimeException Both provided matrices ({@code A.n_rows}, {@code A.n_cols} and {@code B.n_rows},
   *           {@code B.n_cols}) must have the same shape.
   */
  public static Mat min(final AbstractMat A, final AbstractMat B) throws RuntimeException {
    if (A.n_rows != B.n_rows || A.n_cols != B.n_cols) {
      throw new RuntimeException("Both provided matrices (" + A.n_rows + ", " + A.n_cols + " and " + B.n_rows + ", " + B.n_cols + ") must have the same shape.");
    }

    Mat result = new Mat(A.n_rows, A.n_cols);

    for (int n = 0; n < A.n_elem; n++) {
      result._data[n] = Math.min(A._data[n], B._data[n]);
    }

    return result;
  }

  /**
   * Returns the largest value within the provided vector.
   * 
   * @param V The vector
   * 
   * @return TODO
   * 
   * @throws RuntimeException The ({@code V.n_rows}, {@code V.n_cols})-matrix must have at least one element.
   */
  public static double max(final AbstractVector V) throws RuntimeException {
    /*
     * The parameter "V" is validated within V.max().
     */

    return V.max();
  }

  /**
   * TODO
   * 
   * @param V TODO
   * 
   * @return TODO
   */
  protected static double max(final AbstractView V) {

    V.iteratorReset();
    double maximum = V._data[V.iteratorNext()];
    while (V.iteratorHasNext()) {
      maximum = Math.max(maximum, V._data[V.iteratorNext()]);
    }

    return maximum;
  }

  /**
   * Returns the largest value for each column of the provided matrix.
   * 
   * @param <T> TODO
   * @param return_type The type of vector/matrix to be returned
   * @param X The matrix
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   */
  public static <T extends AbstractVector> T max(final Class<T> return_type, final Mat X) throws RuntimeException {
    /*
     * The parameter "V" is validated within max(Class<T>, Mat, int).
     */

    return max(return_type, X, 0);
  }

  /**
   * Returns the largest value for each column ({@code dim} = 0) or row ({@code dim} = 1) of the provided matrix.
   * 
   * @param <T> TODO
   * @param return_type The type of vector to be returned
   * @param X The matrix
   * @param dim The dimension
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   * @throws IllegalArgumentException The specified dimension ({@code dim}) must either be 0 or 1.
   */
  public static <T extends AbstractVector> T max(final Class<T> return_type, final Mat X, final int dim) throws RuntimeException {
    if (X.is_empty()) {
      throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must have at least one element.");
    }

    T result;

    try {
      result = return_type.newInstance();
    } catch(InstantiationException | IllegalAccessException e) {
      throw new RuntimeException("Internal Exception. I would greatly appreciate an e-mail containing some information about this problem.");
    }

    switch (dim) {
      case 0:
        result.set_size(X.n_cols);

        for (int j = 0; j < X.n_cols; j++) {
          result._data[j] = max(new ViewSubCol(X, j));
        }
        break;
      case 1:
        result.set_size(X.n_rows);

        for (int i = 0; i < X.n_rows; i++) {
          result._data[i] = max(new ViewSubRow(X, i));
        }
        break;
      default:
        throw new IllegalArgumentException("The specified dimension (" + dim + ") must either be 0 or 1.");
    }

    return result;
  }

  /**
   * Returns the element-wise largest values between the provided matrices.
   * 
   * @param A The first matrix
   * @param B The second matrix
   * 
   * @return TODO
   * 
   * @throws RuntimeException Both provided matrices ({@code A.n_rows}, {@code A.n_cols} and {@code B.n_rows},
   *           {@code B.n_cols}) must have the same shape.
   */
  public static Mat max(final AbstractMat A, final AbstractMat B) throws RuntimeException {
    if (A.n_rows != B.n_rows || A.n_cols != B.n_cols) {
      throw new RuntimeException("Both provided matrices (" + A.n_rows + ", " + A.n_cols + " and " + B.n_rows + ", " + B.n_cols + ") must have the same shape.");
    }

    Mat result = new Mat(A.n_rows, A.n_cols);

    for (int n = 0; n < A.n_elem; n++) {
      result._data[n] = Math.max(A._data[n], B._data[n]);
    }

    return result;
  }

  /**
   * Returns the product of all elements of the provided vector.
   * 
   * @param V The vector
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code V.n_rows}, {@code V.n_cols})-vector must have at least one element.
   */
  public static double prod(final AbstractVector V) throws RuntimeException {
    if (V.is_empty()) {
      throw new RuntimeException("The provided (" + V.n_rows + ", " + V.n_cols + ")-vector must have at least one element.");
    }

    double prod = V._data[0];

    for (int n = 1; n < V.n_elem; n++) {
      prod *= V._data[n];
    }

    return prod;
  }

  /**
   * TODO
   * 
   * @param V TODO
   * 
   * @return TODO
   */
  protected static double prod(final AbstractView V) {
    double product = 1;

    V.iteratorReset();
    while (V.iteratorHasNext()) {
      product *= V._data[V.iteratorNext()];
    }

    return product;
  }

  /**
   * Returns the product of all elements per column of the provided matrix.
   * 
   * @param <T> TODO
   * @param return_type The type of vector/matrix to be returned
   * @param X The matrix
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   */
  public static <T extends AbstractVector> T prod(final Class<T> return_type, final Mat X) throws RuntimeException {
    /*
     * The parameter "X" is validated within prod(Class<T>, Mat, int).
     */

    return prod(return_type, X, 0);
  }

  /**
   * Returns the product of all elements per column ({@code dim} = 0) or row ({@code dim} = 1) of the provided matrix.
   * 
   * @param <T> TODO
   * @param return_type The type of vector to be returned
   * @param X The matrix
   * @param dim The dimension
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   * @throws IllegalArgumentException The specified dimension ({@code dim}) must either be 0 or 1.
   */
  public static <T extends AbstractVector> T prod(final Class<T> return_type, final Mat X, final int dim) throws RuntimeException, IllegalArgumentException {
    if (X.is_empty()) {
      throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must have at least one element.");
    }

    T result;

    try {
      result = return_type.newInstance();
    } catch(InstantiationException | IllegalAccessException e) {
      throw new RuntimeException("Internal Exception. I would greatly appreciate an e-mail containing some information about this problem.");
    }

    switch (dim) {
      case 0:
        result.set_size(X.n_cols);

        for (int j = 0; j < X.n_cols; j++) {
          result._data[j] = prod(new ViewSubCol(X, j));
        }
        break;
      case 1:
        result.set_size(X.n_rows);

        for (int i = 0; i < X.n_rows; i++) {
          result._data[i] = prod(new ViewSubRow(X, i));
        }
        break;
      default:
        throw new IllegalArgumentException("The specified dimension (" + dim + ") must either be 0 or 1.");
    }

    return result;
  }

  /**
   * Returns the sum of all elements of the provided vector.
   * 
   * @param V The vector
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code V.n_rows}, {@code V.n_cols})-matrix must have at least one element.
   */
  public static double sum(final AbstractVector V) throws RuntimeException {
    /*
     * The parameter "V" is validated within accu(AbstractMat).
     */

    return accu(V);
  }

  /**
   * TODO
   * 
   * @param V TODO
   * 
   * @return TODO
   */
  protected static double sum(final AbstractView V) {
    double sum = 0;

    V.iteratorReset();
    while (V.iteratorHasNext()) {
      sum += V._data[V.iteratorNext()];
    }

    return sum;
  }

  /**
   * Returns the sum of all elements per column of the provided matrix.
   * 
   * @param <T> TODO
   * @param return_type The type of vector/matrix to be returned
   * @param X The matrix
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   */
  public static <T extends AbstractVector> T sum(final Class<T> return_type, final Mat X) throws RuntimeException {
    /*
     * The parameter "X" is validated within sum(Class<T>, Mat, int).
     */

    return sum(return_type, X, 0);
  }

  /**
   * Returns the sum of all elements per column ({@code dim} = 0) or row ({@code dim} = 1) of the provided matrix.
   * 
   * @param <T> TODO
   * @param return_type The type of vector to be returned
   * @param X The matrix
   * @param dim The dimension
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   * @throws IllegalArgumentException The specified dimension ({@code dim}) must either be 0 or 1.
   */
  public static <T extends AbstractVector> T sum(final Class<T> return_type, final Mat X, final int dim) throws RuntimeException {
    if (X.is_empty()) {
      throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must have at least one element.");
    }

    T result;

    try {
      result = return_type.newInstance();
    } catch(InstantiationException | IllegalAccessException e) {
      throw new RuntimeException("Internal Exception. I would greatly appreciate an e-mail containing some information about this problem.");
    }

    switch (dim) {
      case 0:
        result.set_size(X.n_cols);

        for (int j = 0; j < X.n_cols; j++) {
          result._data[j] = sum(new ViewSubCol(X, j));
        }
        break;
      case 1:
        result.set_size(X.n_rows);

        for (int i = 0; i < X.n_rows; i++) {
          result._data[i] = sum(new ViewSubRow(X, i));
        }
        break;
      default:
        throw new IllegalArgumentException("The specified dimension (" + dim + ") must either be 0 or 1.");
    }

    return result;
  }

  /**
   * Returns the mean of the provided vector.
   * 
   * @param V The vector
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code V.n_rows}, {@code V.n_cols})-matrix must have at least one element.
   */
  public static double mean(final AbstractVector V) throws RuntimeException {
    /*
     * The parameter "V" is validated within sum(AbstractVector).
     */

    return sum(V) / V.n_elem;
  }

  /**
   * TODO
   * 
   * @param V TODO
   * 
   * @return TODO
   */
  protected static double mean(final AbstractView V) {
    return sum(V) / V.n_elem;
  }

  /**
   * Returns the mean for each column of the provided matrix.
   * 
   * @param <T> TODO
   * @param return_type The type of vector to be returned
   * @param X The matrix
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   */
  public static <T extends AbstractVector> T mean(final Class<T> return_type, final Mat X) throws RuntimeException {
    /*
     * The parameter "X" is validated within mean(Class<T>, Mat, int).
     */

    return mean(return_type, X, 0);
  }

  /**
   * Returns the mean for each column ({@code dim} = 0) or row ({@code dim} = 1) of the provided matrix.
   * 
   * @param <T> TODO
   * @param return_type The type of vector to be returned
   * @param X The matrix
   * @param dim The dimension
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   * @throws IllegalArgumentException The specified dimension ({@code dim}) must either be 0 or 1.
   */
  public static <T extends AbstractVector> T mean(final Class<T> return_type, final Mat X, final int dim) throws RuntimeException, IllegalArgumentException {
    if (X.is_empty()) {
      throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must have at least one element.");
    }

    T result;

    try {
      result = return_type.newInstance();
    } catch(InstantiationException | IllegalAccessException e) {
      throw new RuntimeException("Internal Exception. I would greatly appreciate an e-mail containing some information about this problem.");
    }

    switch (dim) {
      case 0:
        result.set_size(X.n_cols);

        for (int j = 0; j < X.n_cols; j++) {
          result._data[j] = mean(new ViewSubCol(X, j));
        }
        break;
      case 1:
        result.set_size(X.n_rows);

        for (int i = 0; i < X.n_rows; i++) {
          result._data[i] = mean(new ViewSubRow(X, i));
        }
        break;
      default:
        throw new IllegalArgumentException("The specified dimension (" + dim + ") must either be 0 or 1.");
    }

    return result;
  }

  /**
   * Returns the median of the provided vector.
   * 
   * @param V The vector
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   */
  public static double median(final AbstractVector V) throws RuntimeException {
    /*
     * The current algorithm runs in O(n log n), however using a selection algorithm could solve the problem in O(n).
     * See ICS 161: Design and Analysis of Algorithms, Lecture notes for January 30, 1996:
     * http://www.ics.uci.edu/~eppstein/161/960130.html
     */
    if (V.is_empty()) {
      throw new RuntimeException("The provided (" + V.n_rows + ", " + V.n_cols + ")-vector must have at least one element.");
    }

    double[] temp = Arrays.copyOf(V._data, V.n_elem);
    Arrays.sort(temp);

    double median;
    if (V.n_elem % 2 == 0) {
      int middle = V.n_elem / 2;
      median = (temp[middle - 1] + temp[middle]) / 2;
    } else {
      median = temp[V.n_elem / 2];
    }

    return median;
  }

  /**
   * Returns the median for each column of the provided matrix.
   * 
   * @param <T> TODO
   * @param return_type The type of vector to be returned
   * @param X The matrix
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   */
  public static <T extends AbstractVector> T median(final Class<T> return_type, final Mat X) throws RuntimeException {
    /*
     * The parameter "X" is validated within median(Class<T>, Mat, int).
     */

    return median(return_type, X, 0);
  }

  /**
   * Returns the median for each column ({@code dim} = 0) or row ({@code dim} = 1) of the provided matrix.
   * 
   * @param <T> TODO
   * @param return_type The type of vector to be returned
   * @param X The matrix
   * @param dim The dimension
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   * @throws IllegalArgumentException The specified dimension ({@code dim}) must either be 0 or 1.
   */
  public static <T extends AbstractVector> T median(final Class<T> return_type, final Mat X, final int dim) throws RuntimeException, IllegalArgumentException {
    if (X.is_empty()) {
      throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must have at least one element.");
    }

    T result;

    try {
      result = return_type.newInstance();
    } catch(InstantiationException | IllegalAccessException e) {
      throw new RuntimeException("Internal Exception. I would greatly appreciate an e-mail containing some information about this problem.");
    }

    switch (dim) {
      case 0:
        result.set_size(X.n_cols);

        for (int j = 0; j < X.n_cols; j++) {
          /*
           * Creates a deep copy of each column, since sorting of shallow sub views is not yet implemented.
           */
          result._data[j] = median(X.col(j));
        }
        break;
      case 1:
        result.set_size(X.n_rows);

        for (int i = 0; i < X.n_rows; i++) {
          /*
           * Creates a deep copy of each row, since sorting of shallow sub views is not yet implemented.
           */
          result._data[i] = median(X.row(i));
        }
        break;
      default:
        throw new IllegalArgumentException("The specified dimension (" + dim + ") must either be 0 or 1.");
    }

    return result;
  }

  /**
   * Returns the standard deviation of the provided vector normalised by {@code V.n_elem -1}.
   * 
   * @param V The vector
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code V.n_rows}, {@code V.n_cols})-matrix must have at least one element.
   */
  public static double stddev(final AbstractVector V) throws RuntimeException {
    /*
     * The parameter "V" is validated within stddev(AbstractVector, int).
     */

    return stddev(V, 0);
  }

  /**
   * Returns the standard deviation of the provided vector normalised by {@code V.n_elem -1} ({@code norm_type} = 0) or
   * {@code V.n_elem} ({@code norm_type} = 1).
   * 
   * @param V The vector
   * @param norm_type The normalisation
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code V.n_rows}, {@code V.n_cols})-matrix must have at least one element.
   * @throws IllegalArgumentException The specified normalisation ({@code norm_type}) must either be 0 or 1.
   */
  public static double stddev(final AbstractVector V, final int norm_type) throws RuntimeException {
    /*
     * The parameter "V" is validated within var(AbstractVector, int).
     */

    return Math.sqrt(var(V, norm_type));
  }

  /**
   * TODO
   * 
   * @param V TODO
   * @param norm_type TODO
   * 
   * @return TODO
   */
  protected static double stddev(final AbstractView V, final int norm_type) {
    /*
     * The standard deviation of just one element is 0.
     */
    if (V.n_elem < 2) {
      return 0;
    }

    double mean = mean(V);

    if (Double.isNaN(mean)) {
      return Datum.inf;
    }

    double variance = 0;

    V.iteratorReset();
    while (V.iteratorHasNext()) {
      variance += Math.pow(V._data[V.iteratorNext()] - mean, 2);
    }

    variance /= (norm_type == 0) ? ((V.n_elem > 1) ? V.n_elem - 1 : 1) : V.n_elem;
    return Math.sqrt(variance);
  }

  /**
   * Returns the standard deviation for each column of the provided matrix normalised by {@code V.n_elem -1}.
   * 
   * @param <T> TODO
   * @param return_type The type of vector to be returned
   * @param X The matrix
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   */
  public static <T extends AbstractVector> T stddev(final Class<T> return_type, final Mat X) throws RuntimeException {
    /*
     * The parameter "X" is validated within stddev(Class<T>, Mat, int).
     */

    return stddev(return_type, X, 0);
  }

  /**
   * Returns the standard deviation for each column of the provided matrix normalised by {@code V.n_elem -1} (
   * {@code norm_type} = 0) or {@code V.n_elem} ({@code norm_type} = 1).
   * 
   * @param <T> TODO
   * @param return_type The type of vector to be returned
   * @param X The matrix
   * @param norm_type The normalisation
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   * @throws IllegalArgumentException The specified normalisation ({@code norm_type}) must either be 0 or 1.
   */
  public static <T extends AbstractVector> T stddev(final Class<T> return_type, final Mat X, final int norm_type) throws RuntimeException {
    /*
     * The parameter "X" is validated within stddev(Class<T>, Mat, int, int).
     */

    return stddev(return_type, X, norm_type, 0);
  }

  /**
   * Returns the standard deviation for each column ({@code dim} = 0) or row ({@code dim} = 1) of the provided matrix
   * normalised by {@code V.n_elem -1} ( {@code norm_type} = 0) or {@code V.n_elem} ({@code norm_type} = 1).
   * 
   * @param <T> TODO
   * @param return_type The type of vector to be returned
   * @param X The matrix
   * @param norm_type The normalisation
   * @param dim The dimension
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   * @throws IllegalArgumentException The specified dimension ({@code dim}) must either be 0 or 1.
   * @throws IllegalArgumentException The specified normalisation ({@code norm_type}) must either be 0 or 1.
   */
  public static <T extends AbstractVector> T stddev(final Class<T> return_type, Mat X, final int norm_type, final int dim) throws RuntimeException, IllegalArgumentException {
    if (X.is_empty()) {
      throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must have at least one element.");
    }

    T result;

    try {
      result = return_type.newInstance();
    } catch(InstantiationException | IllegalAccessException e) {
      throw new RuntimeException("Internal Exception. I would greatly appreciate an e-mail containing some information about this problem.");
    }

    switch (norm_type) {
      case 0:
      case 1:
        switch (dim) {
          case 0:
          case 1:
            result.set_size(X.n_cols);

            for (int j = 0; j < X.n_cols; j++) {
              result._data[j] = stddev(new ViewSubCol(X, j), norm_type);
            }
            break;
          default:
            throw new IllegalArgumentException("The specified dimension (" + dim + ") must either be 0 or 1.");
        }
        break;
      default:
        throw new IllegalArgumentException("The specified normalisation (" + norm_type + ") must either be 0 or 1.");
    }

    return result;
  }

  /**
   * Returns the variance of the provided vector normalised by {@code V.n_elem -1}.
   * 
   * @param V The vector
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code V.n_rows}, {@code V.n_cols})-matrix must have at least one element.
   */
  public static double var(final AbstractVector V) throws RuntimeException {
    /*
     * The parameter "V" is validated within var(AbstractVector, int).
     */

    return var(V, 0);
  }

  /**
   * Returns the variance of the provided vector normalised by {@code V.n_elem -1} ({@code norm_type} = 0) or
   * {@code V.n_elem} ({@code norm_type} = 1).
   * 
   * @param V The vector
   * @param norm_type The normalisation
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code V.n_rows}, {@code V.n_cols})-matrix must have at least one element.
   * @throws IllegalArgumentException The specified normalisation ({@code norm_type}) must either be 0 or 1.
   */
  public static double var(final AbstractVector V, final int norm_type) throws RuntimeException {
    /*
     * The parameter "V" is validated within mean(AbstractVector).
     */

    double mean = mean(V);

    if (Double.isNaN(mean)) {
      return Datum.inf;
    }

    double variance = 0;
    for (int n = 0; n < V.n_elem; n++) {
      variance += Math.pow(V._data[n] - mean, 2);
    }

    switch (norm_type) {
      case 0:
        if (V.n_elem > 1) {
          variance /= (V.n_elem - 1);
        }
        break;
      case 1:
        variance /= V.n_elem;
        break;
      default:
        throw new IllegalArgumentException("The specified normalisation (" + norm_type + ") must either be 0 or 1.");
    }

    return variance;
  }

  /**
   * TODO
   * 
   * @param V TODO
   * @param norm_type TODO
   * 
   * @return TODO
   */
  protected static double var(final AbstractView V, final int norm_type) {
    double mean = mean(V);
    double variance = 0;

    if (Double.isNaN(mean)) {
      return Datum.inf;
    }

    V.iteratorReset();
    while (V.iteratorHasNext()) {
      variance += Math.pow(V._data[V.iteratorNext()] - mean, 2);
    }

    variance /= (norm_type == 0) ? ((V.n_elem > 1) ? V.n_elem - 1 : 1) : V.n_elem;
    return variance;
  }

  /**
   * Returns the variance for each column of the provided matrix normalised by {@code V.n_elem -1}.
   * 
   * @param <T> TODO
   * @param return_type The type of vector to be returned
   * @param X The matrix
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   */
  public static <T extends AbstractVector> T var(final Class<T> return_type, final Mat X) throws RuntimeException {
    /*
     * The parameter "X" is validated within var(Class<T>, Mat, int).
     */

    return var(return_type, X, 0);
  }

  /**
   * Returns the variance for each column of the provided matrix normalised by {@code V.n_elem -1} ( {@code norm_type} =
   * 0) or {@code V.n_elem} ({@code norm_type} = 1).
   * 
   * @param <T> TODO
   * @param return_type The type of vector to be returned
   * @param X The matrix
   * @param norm_type The normalisation
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   * @throws IllegalArgumentException The specified normalisation ({@code norm_type}) must either be 0 or 1.
   */
  public static <T extends AbstractVector> T var(final Class<T> return_type, final Mat X, final int norm_type) throws RuntimeException {
    /*
     * The parameter "X" is validated within var(Class<T>, Mat, int, int).
     */

    return var(return_type, X, norm_type, 0);
  }

  /**
   * Returns the variance for each column ({@code dim} = 0) or row ({@code dim} = 1) of the provided matrix normalised
   * by {@code V.n_elem -1} ( {@code norm_type} = 0) or {@code V.n_elem} ({@code norm_type} = 1).
   * 
   * @param <T> TODO
   * @param return_type The type of vector to be returned
   * @param X The matrix
   * @param norm_type The normalisation
   * @param dim The dimension
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   * @throws IllegalArgumentException The specified dimension ({@code dim}) must either be 0 or 1.
   * @throws IllegalArgumentException The specified normalisation ({@code norm_type}) must either be 0 or 1.
   */
  public static <T extends AbstractVector> T var(final Class<T> return_type, final Mat X, final int norm_type, final int dim) throws RuntimeException, IllegalArgumentException {
    if (X.is_empty()) {
      throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must have at least one element.");
    }

    T result;

    try {
      result = return_type.newInstance();
    } catch(InstantiationException | IllegalAccessException e) {
      throw new RuntimeException("Internal Exception. I would greatly appreciate an e-mail containing some information about this problem.");
    }

    switch (norm_type) {
      case 0:
      case 1:
        switch (dim) {
          case 0:
          case 1:
            result.set_size(X.n_cols);

            for (int j = 0; j < X.n_cols; j++) {
              result._data[j] = var(new ViewSubCol(X, j), norm_type);
            }
            break;
          default:
            throw new IllegalArgumentException("The specified dimension (" + dim + ") must either be 0 or 1.");
        }
        break;
      default:
        throw new IllegalArgumentException("The specified normalisation (" + norm_type + ") must either be 0 or 1.");
    }

    return result;
  }

  /**
   * Returns {@code true} if all elements of the provided vector are non-zero.
   * 
   * @param V The vector
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code V.n_rows}, {@code V.n_cols})-vector must have at least one element.
   */
  public static boolean all(final AbstractVector V) throws RuntimeException {
    if (V.is_empty()) {
      throw new RuntimeException("The provided (" + V.n_rows + ", " + V.n_cols + ")-vector must have at least one element.");
    }

    for (int n = 0; n < V.n_elem; n++) {
      if (V._data[n] == 0) {
        return false;
      }
    }

    return true;
  }

  /**
   * TODO
   * 
   * @param V TODO
   * 
   * @return TODO
   */
  protected static boolean all(final AbstractView V) {
    V.iteratorReset();
    while (V.iteratorHasNext()) {
      if (V._data[V.iteratorNext()] == 0) {
        return false;
      }
    }

    return true;
  }

  /**
   * Returns a 1 for each column of the provided matrix where all elements are non-zero and a 0 otherwise.
   * 
   * @param <T> TODO
   * @param return_type The type of vector to be returned
   * @param X The matrix
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   */
  public static <T extends AbstractVector> T all(final Class<T> return_type, final Mat X) throws RuntimeException {
    /*
     * The parameter "X" is validated within all(Class<T>, Mat, int).
     */

    return all(return_type, X, 0);
  }

  /**
   * Returns a 1 for each column ({@code dim} = 0) or row ({@code dim} = 1) of the provided matrix where all elements
   * are non-zero and a 0 otherwise.
   * 
   * @param <T> TODO
   * @param return_type The type of vector to be returned
   * @param X The matrix
   * @param dim The dimension
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   * @throws IllegalArgumentException The specified dimension ({@code dim}) must either be 0 or 1.
   */
  public static <T extends AbstractVector> T all(final Class<T> return_type, final Mat X, final int dim) throws RuntimeException, IllegalArgumentException {
    if (X.is_empty()) {
      throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must have at least one element.");
    }

    T result;

    try {
      result = return_type.newInstance();
    } catch(InstantiationException | IllegalAccessException e) {
      throw new RuntimeException("Internal Exception. I would greatly appreciate an e-mail containing some information about this problem.");
    }

    switch (dim) {
      case 0:
        /*
         * All uninitialised matrices are already equal to a zero matrix.
         */
        result.set_size(X.n_cols);

        for (int j = 0; j < X.n_cols; j++) {
          if (all(new ViewSubCol(X, j))) {
            result._data[j] = 1;
          }
        }
        break;
      case 1:
        /*
         * All uninitialised matrices are already equal to a zero matrix.
         */
        result.set_size(X.n_rows);

        for (int i = 0; i < X.n_rows; i++) {
          if (all(new ViewSubRow(X, i))) {
            result._data[i] = 1;
          }
        }
        break;
      default:
        throw new IllegalArgumentException("The specified dimension (" + dim + ") must either be 0 or 1.");
    }

    return result;
  }

  /**
   * Returns {@code true} if any element of the provided vector is non-zero.
   * 
   * @param V The vector
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code V.n_rows}, {@code V.n_cols})-vector must have at least one element.
   */
  public static boolean any(final AbstractVector V) throws RuntimeException {
    if (V.is_empty()) {
      throw new RuntimeException("The provided (" + V.n_rows + ", " + V.n_cols + ")-vector must have at least one element.");
    }

    for (int n = 0; n < V.n_elem; n++) {
      if (V._data[n] != 0) {
        return true;
      }
    }

    return false;
  }

  /**
   * TODO
   * 
   * @param V TODO
   * 
   * @return TODO
   */
  protected static boolean any(final AbstractView V) {
    V.iteratorReset();
    while (V.iteratorHasNext()) {
      if (V._data[V.iteratorNext()] != 0) {
        return true;
      }
    }

    return false;
  }

  /**
   * Returns a 1 for each column of the provided matrix where any element is non-zero and a 0 otherwise.
   * 
   * @param <T> TODO
   * @param return_type The type of vector to be returned
   * @param X The matrix
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   */
  public static <T extends AbstractVector> T any(final Class<T> return_type, final Mat X) throws RuntimeException {
    /*
     * The parameter "X" is validated within any(Class<T>, Mat, int).
     */

    return any(return_type, X, 0);

  }

  /**
   * Returns a 1 for each column ({@code dim} = 0) or row ({@code dim} = 1) of the provided matrix where any element
   * is non-zero and a 0 otherwise.
   * 
   * @param <T> TODO
   * @param return_type The type of vector to be returned
   * @param X The matrix
   * @param dim The dimension
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   * @throws IllegalArgumentException The specified dimension ({@code dim}) must either be 0 or 1.
   */
  public static <T extends AbstractVector> T any(final Class<T> return_type, final Mat X, final int dim) throws RuntimeException, IllegalArgumentException {
    if (X.is_empty()) {
      throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must have at least one element.");
    }

    T result;

    try {
      result = return_type.newInstance();
    } catch(InstantiationException | IllegalAccessException e) {
      throw new RuntimeException("Internal Exception. I would greatly appreciate an e-mail containing some information about this problem.");
    }

    switch (dim) {
      case 0:
        /*
         * All uninitialised matrices are already equal to a zero matrix.
         */
        result.set_size(X.n_cols);

        for (int j = 0; j < X.n_cols; j++) {
          if (any(new ViewSubCol(X, j))) {
            result._data[j] = 1;
          }
        }
        break;
      case 1:
        /*
         * All uninitialised matrices are already equal to a zero matrix.
         */
        result.set_size(X.n_rows);

        for (int i = 0; i < X.n_rows; i++) {
          if (any(new ViewSubRow(X, i))) {
            result._data[i] = 1;
          }
        }
        break;
      default:
        throw new IllegalArgumentException("The specified dimension (" + dim + ") must either be 0 or 1.");
    }

    return result;
  }

  /**
   * TODO
   * 
   * @param result TODO
   * @param A TODO
   * @param B TODO
   */
  protected static void conv(final double[] result, final double[] A, final double[] B) {
    for (int n = 0; n < result.length; n++) {
      int min = Math.max(0, n - B.length + 1);
      int max = Math.min(A.length, n + 1);

      for (int nn = min; nn < max; nn++)
      {
        result[n] += A[nn] * B[n - nn];
      }
    }
  }

  /**
   * Returns the convolution between the first and second provided vector.
   * 
   * @param A The first vector
   * @param B The second vector
   * 
   * @return TODO
   * 
   * @throws RuntimeException The first provided ({@code A.n_rows}, {@code A.n_cols})-vector must have at least one
   *           element.
   * @throws RuntimeException The second provided ({@code B.n_rows}, {@code B.n_cols})-vector must have at least one
   *           element.
   */
  public static Mat conv(final AbstractVector A, final AbstractVector B) throws RuntimeException {
    if (A.empty()) {
      throw new RuntimeException("The first provided (" + A.n_rows + ", " + A.n_cols + ")-vector must have at least one element.");
    }

    if (B.empty()) {
      throw new RuntimeException("The second provided (" + B.n_rows + ", " + B.n_cols + ")-vector must have at least one element.");
    }

    Mat result;
    if (A.is_colvec()) {
      result = new Mat(A.n_elem + B.n_elem - 1, 1);
    } else {
      result = new Mat(1, A.n_elem + B.n_elem - 1);
    }

    conv(result._data, A._data, B._data);
    return result;
  }

  /**
   * Returns the correlation between the first and second provided vector normalised by {@code X.n_elem} - 1.
   * 
   * @param X The first vector
   * @param Y The second vector
   * 
   * @return TODO
   * 
   * @throws RuntimeException Both provided matrices must have the same number of elements ({@code X.n_elem},
   *           {@code Y.n_elem}).
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   * @throws RuntimeException The provided ({@code Y.n_rows}, {@code Y.n_cols})-matrix must have at least one element.
   */
  public static double cor(final AbstractVector X, final AbstractVector Y) throws RuntimeException {
    /*
     * The parameters "X" and "Y" are validated within cor(AbstractVector, AbstractVector, int).
     */

    return cor(X, Y, 0);
  }

  /**
   * Returns the correlation between the first and second provided vector normalised by {@code X.n_elem -1} (
   * {@code norm_type} = 0) or {@code X.n_elem} ({@code norm_type} = 1).
   * 
   * @param X The first vector
   * @param Y The second vector
   * @param norm_type The normalisation
   * 
   * @return TODO
   * 
   * @throws RuntimeException Both provided matrices must have the same number of elements ({@code X.n_elem},
   *           {@code Y.n_elem}).
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   * @throws RuntimeException The provided ({@code Y.n_rows}, {@code Y.n_cols})-matrix must have at least one element.
   * @throws IllegalArgumentException The specified normalisation ({@code norm_type}) must either be 0 or 1.
   */
  public static double cor(final AbstractVector X, final AbstractVector Y, final int norm_type) throws RuntimeException, IllegalArgumentException {
    /*
     * The parameters "X", "Y" and "norm_type" are validated within cov(AbstractVector, AbstractVector, int).
     */

    return cov(X, Y, norm_type) / (stddev(X) * stddev(Y));
  }

  /**
   * Returns the autocorrelation of the provided vector normalised by {@code X.n_elem} - 1.
   * <p>
   * <b>Note:</b> The result will always be 1.
   * 
   * @param X The vector
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   */
  public static double cor(final AbstractVector X) throws RuntimeException {
    /*
     * The autocorrelation of a vector is always 1.
     */
    return 1;
  }

  /**
   * Returns the autocorrelation of the provided vector normalised by {@code X.n_elem -1} ({@code norm_type} = 0) or
   * {@code X.n_elem} ({@code norm_type} = 1).
   * <p>
   * <b>Note:</b> The result will always be 1.
   * 
   * @param X The vector
   * @param norm_type The normalisation
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   * @throws IllegalArgumentException The specified normalisation ({@code norm_type}) must either be 0 or 1.
   */
  public static double cor(final AbstractVector X, final int norm_type) throws RuntimeException, IllegalArgumentException {
    /*
     * The autocorrelation of a vector is always 1.
     */
    return 1;
  }

  /**
   * Returns the correlation between the first and second provided matrix, where the ({@code i}, {@code j})-th entry is
   * the correlation between the {@code i}th column of the first matrix and the {@code j}th column of the second matrix
   * normalised by {@code X.n_elem -1}.
   * <p>
   * <b>Note:</b> The method behaves like {@link #cov(AbstractVector, AbstractVector)} if the provided matrices are in
   * the shape of a vector.
   * 
   * @param X The first matrix
   * @param Y The second matrix
   * 
   * @return TODO
   * 
   * @throws RuntimeException Both provided matrices ({@code X.n_rows}, {@code X.n_cols} and {@code Y.n_rows},
   *           {@code Y.n_cols}) must have the same shape.
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   * @throws RuntimeException The provided ({@code Y.n_rows}, {@code Y.n_cols})-matrix must have at least one element.
   */
  public static Mat cor(final Mat X, final Mat Y) throws RuntimeException {
    /*
     * The parameters "X" and "Y" are validated within cor(AbstractVector, AbstractVector, int).
     */

    return cor(X, Y, 0);
  }

  /**
   * Returns the correlation between the first and second provided matrix, where the ({@code i}, {@code j})-th entry is
   * the correlation between the {@code i}th column of the first matrix and the {@code j}th column of the second matrix
   * normalised by {@code X.n_elem -1} ( {@code norm_type} = 0) or {@code X.n_elem} ({@code norm_type} = 1).
   * <p>
   * <b>Note:</b> The method behaves like {@link #cov(AbstractVector, AbstractVector, int)} if the provided matrices are
   * in the shape of a vector.
   * 
   * @param X The first matrix
   * @param Y The second matrix
   * @param norm_type The normalisation
   * 
   * @return TODO
   * 
   * @throws RuntimeException Both provided matrices ({@code X.n_rows}, {@code X.n_cols} and {@code Y.n_rows},
   *           {@code Y.n_cols}) must have the same shape.
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   * @throws RuntimeException The provided ({@code Y.n_rows}, {@code Y.n_cols})-matrix must have at least one element.
   */
  public static Mat cor(final Mat X, final Mat Y, final int norm_type) throws RuntimeException {
    /*
     * The parameters "X", "Y" and "norm_type" are validated within cor(AbstractVector, AbstractVector, int), if
     * the provided matrices are in the shape of a vector, and otherwise within sum(Class<T>, Mat) (excluding
     * "norm_type" in this case).
     */

    if (X.n_rows != Y.n_rows || X.n_cols != Y.n_cols) {
      throw new RuntimeException("Both provided matrices (" + X.n_rows + ", " + X.n_cols + " and " + Y.n_rows + ", " + Y.n_cols + ") must have the same shape.");
    }

    Mat result;
    if (X.is_rowvec()) {
      result = new Mat(new double[]{cor(new Row(X), new Row(Y), norm_type)});
    } else if (X.is_colvec()) {
      result = new Mat(new double[]{cor(new Col(X), new Col(Y), norm_type)});
    } else {
      result = trans(X).times(Y);
      result.inPlace(Op.MINUS, trans(sum(Row.class, X)).times(sum(Row.class, Y)).divide(X.n_rows));

      switch (norm_type) {
        case 0:
          if (X.n_rows > 1) {
            result.inPlace(Op.DIVIDE, X.n_rows - 1);
          }
          break;
        case 1:
          result.inPlace(Op.DIVIDE, X.n_rows);
          break;
        default:
          throw new IllegalArgumentException("The specified normalisation (" + norm_type + ") must either be 0 or 1.");
      }

      result.inPlace(Op.ELEMDIVIDE, trans(stddev(Row.class, X)).times(stddev(Row.class, Y)));
    }

    return result;
  }

  /**
   * Returns the autocorrelation of the provided matrix, where the ({@code i}, {@code j})-th entry is
   * the correlation between the {@code i}th column and {@code j}th column normalised by {@code X.n_elem -1}.
   * <p>
   * <b>Note:</b> The method behaves like {@link #cov(AbstractVector)} if the provided matrix is in the shape of a
   * vector.
   * 
   * @param X The matrix
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   */
  public static Mat cor(final Mat X) throws RuntimeException {
    /*
     * The parameter "X" is validated within cor(Mat, int).
     */

    return cor(X, 0);
  }

  /**
   * Returns the autocorrelation of the provided matrix, where the ({@code i}, {@code j})-th entry is
   * the correlation between the {@code i}th column and {@code j}th column normalised by {@code X.n_elem -1} (
   * {@code norm_type} = 0) or {@code X.n_elem} ({@code norm_type} = 1).
   * <p>
   * <b>Note:</b> The method behaves like {@link #cov(AbstractVector, int)} if the provided matrix is in the shape of a
   * vector.
   * 
   * @param X The matrix
   * @param norm_type The normalisation
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   */
  public static Mat cor(final Mat X, final int norm_type) throws RuntimeException {
    /*
     * The parameters "X" and "norm_type" are validated within cor(AbstractVector, int), if the provided matrices are in
     * the shape of a vector, and otherwise within sum(Class<T>, Mat) (excluding "norm_type" in this case).
     */

    if (X.is_rowvec()) {
      return new Mat(new double[]{cor(new Row(X), norm_type)});
    } else if (X.is_colvec()) {
      return new Mat(new double[]{cor(new Col(X), norm_type)});
    }

    Row sum = sum(Row.class, X);
    Row stddev = stddev(Row.class, X);

    Mat result = trans(X).times(X);
    result.inPlace(Op.MINUS, trans(sum).times(sum).divide(X.n_rows));

    switch (norm_type) {
      case 0:
        if (X.n_rows > 1) {
          result.inPlace(Op.DIVIDE, X.n_rows - 1);
        }
        break;
      case 1:
        result.inPlace(Op.DIVIDE, X.n_rows);
        break;
      default:
        throw new IllegalArgumentException("The specified normalisation (" + norm_type + ") must either be 0 or 1.");
    }

    result.inPlace(Op.ELEMDIVIDE, trans(stddev).times(stddev));

    return result;
  }

  /**
   * Returns the covariance between the first and second provided vector normalised by {@code X.n_elem} - 1.
   * 
   * @param X The first vector
   * @param Y The second vector
   * 
   * @return TODO
   * 
   * @throws RuntimeException Both provided matrices must have the same number of elements ({@code X.n_elem},
   *           {@code Y.n_elem}).
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   * @throws RuntimeException The provided ({@code Y.n_rows}, {@code Y.n_cols})-matrix must have at least one element.
   */
  public static double cov(final AbstractVector X, final AbstractVector Y) throws RuntimeException {
    /*
     * The parameters "X" and "Y" are validated within cov(AbstractVector, AbstractVector, int).
     */

    return cov(X, Y, 0);
  }

  /**
   * Returns the covariance between the first and second provided vector normalised by {@code X.n_elem -1} (
   * {@code norm_type} = 0) or {@code X.n_elem} ({@code norm_type} = 1).
   * 
   * @param X The first vector
   * @param Y The second vector
   * @param norm_type The normalisation
   * 
   * @return TODO
   * 
   * @throws RuntimeException Both provided matrices must have the same number of elements ({@code X.n_elem},
   *           {@code Y.n_elem}).
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   * @throws RuntimeException The provided ({@code Y.n_rows}, {@code Y.n_cols})-matrix must have at least one element.
   * @throws IllegalArgumentException The specified normalisation ({@code norm_type}) must either be 0 or 1.
   */
  public static double cov(final AbstractVector X, final AbstractVector Y, final int norm_type) throws RuntimeException, IllegalArgumentException {
    /*
     * The parameters "X" and "Y" are validated within mean(AbstractVector).
     */

    if (X.n_elem != Y.n_elem) {
      throw new RuntimeException("Both provided matrices must have the same number of elements (" + X.n_elem + ", " + Y.n_elem + ").");
    }

    // TODO at least one

    double X_accu = X._data[0];
    double Y_accu = Y._data[0];
    double XY_accu = X_accu * Y_accu;
    for (int n = 1; n < X.n_elem; n++) {
      double valueX = X._data[n];
      double valueY = Y._data[n];

      X_accu += valueX;
      Y_accu += valueY;
      XY_accu += valueX * valueY;
    }

    double covariance = XY_accu - (X_accu * Y_accu) / X.n_elem;

    switch (norm_type) {
      case 0:
        if (X.n_elem > 1) {
          covariance /= (X.n_elem - 1);
        }
        break;
      case 1:
        covariance /= X.n_elem;
        break;
      default:
        throw new IllegalArgumentException("The specified normalisation (" + norm_type + ") must either be 0 or 1.");
    }

    return covariance;
  }

  /**
   * Returns the covariance of the provided vector with itself normalised by {@code X.n_elem} - 1.
   * <p>
   * <b>Note:</b> This is better known as the variance of provided vector.
   * 
   * @param X The vector
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   * 
   * @see #var(AbstractVector)
   */
  public static double cov(final AbstractVector X) throws RuntimeException {
    /*
     * The parameter "X" is validated within cov(AbstractVector, int).
     */

    return cov(X, 0);
  }

  /**
   * Returns the covariance of the provided vector with itself normalised by {@code X.n_elem -1} ({@code norm_type} = 0)
   * or {@code X.n_elem} ({@code norm_type} = 1).
   * <p>
   * <b>Note:</b> This is better known as the variance of provided vector.
   * 
   * @param X The vector
   * @param norm_type TODO
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   * @throws IllegalArgumentException The specified normalisation ({@code norm_type}) must either be 0 or 1.
   * 
   * @see #var(AbstractVector, int)
   */
  public static double cov(final AbstractVector X, final int norm_type) throws RuntimeException, IllegalArgumentException {
    /*
     * The parameter "X" is validated within var(AbstractVector, int).
     */

    return var(X, norm_type);
  }

  /**
   * Returns the covariance between the first and second provided matrix, where the ({@code i}, {@code j})-th entry is
   * the covariance between the {@code i}th column of the first matrix and the {@code j}th column of the second matrix
   * normalised by {@code X.n_elem -1}.
   * <p>
   * <b>Note:</b> The method behaves like {@link #cov(AbstractVector, AbstractVector)} if the provided matrices are in
   * the shape of a vector.
   * 
   * @param X The first matrix
   * @param Y The second matrix
   * 
   * @return TODO
   * 
   * @throws RuntimeException Both provided matrices ({@code X.n_rows}, {@code X.n_cols} and {@code Y.n_rows},
   *           {@code Y.n_cols}) must have the same shape.
   * @throws RuntimeException The first provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one
   *           element.
   * @throws RuntimeException The second provided ({@code Y.n_rows}, {@code Y.n_cols})-matrix must have at least one
   *           element.
   */
  public static Mat cov(final Mat X, final Mat Y) {
    /*
     * The parameters "X" and "Y" are validated within cov(Mat, Mat, int).
     */

    return cov(X, Y, 0);
  }

  /**
   * Returns the covariance between the first and second provided matrix, where the ({@code i}, {@code j})-th entry is
   * the covariance between the {@code i}th column of the first matrix and the {@code j}th column of the second matrix
   * normalised by {@code X.n_elem -1} ({@code norm_type} = 0) or {@code X.n_elem} ({@code norm_type} = 1).
   * <p>
   * <b>Note:</b> The method behaves like {@link #cov(AbstractVector, AbstractVector, int)} if the provided matrices are
   * in the shape of a vector.
   * 
   * @param X The first matrix
   * @param Y The second matrix
   * @param norm_type The normalisation
   * 
   * @return TODO
   * 
   * @throws RuntimeException Both provided matrices ({@code X.n_rows}, {@code X.n_cols} and {@code Y.n_rows},
   *           {@code Y.n_cols}) must have the same shape.
   * @throws RuntimeException The first provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one
   *           element.
   * @throws RuntimeException The second provided ({@code Y.n_rows}, {@code Y.n_cols})-matrix must have at least one
   *           element.
   * @throws IllegalArgumentException The specified normalisation ({@code norm_type}) must either be 0 or 1.
   */
  public static Mat cov(final Mat X, final Mat Y, final int norm_type) throws RuntimeException, IllegalArgumentException {
    /*
     * The parameters "X" and "norm_type" are validated within cov(AbstractVector, int), if the provided matrices are in
     * the shape of a vector, and otherwise within sum(Class<T>, Mat) (excluding "norm_type" in this case).
     */

    if (X.n_rows != Y.n_rows || X.n_cols != Y.n_cols) {
      throw new RuntimeException("Both provided matrices (" + X.n_rows + ", " + X.n_cols + " and " + Y.n_rows + ", " + Y.n_cols + ") must have the same shape.");
    }

    if (X.is_rowvec()) {
      return new Mat(new double[]{cov(new Row(X), new Row(Y), norm_type)});
    } else if (X.is_colvec()) {
      return new Mat(new double[]{cov(new Col(X), new Col(Y), norm_type)});
    }

    Mat result = trans(X).times(Y);
    result.inPlace(Op.MINUS, trans(sum(Row.class, X)).times(sum(Row.class, Y)).divide(X.n_rows));

    switch (norm_type) {
      case 0:
        if (X.n_rows > 1) {
          result.inPlace(Op.DIVIDE, X.n_rows - 1);
        }
        break;
      case 1:
        result.inPlace(Op.DIVIDE, X.n_rows);
        break;
      default:
        throw new IllegalArgumentException("The specified normalisation (" + norm_type + ") must either be 0 or 1.");
    }

    return result;
  }

  /**
   * Returns the covariance of the provided matrix, where the ({@code i}, {@code j})-th entry is the correlation between
   * the {@code i}th column and {@code j}th column normalised by {@code X.n_elem -1}.
   * <p>
   * <b>Note:</b> The method behaves like {@link #cov(AbstractVector)} if the provided matrix is in the shape of a
   * vector.
   * 
   * @param X The matrix
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   */
  public static Mat cov(final Mat X) throws RuntimeException {
    /*
     * The parameter "X" is validated within cov(Mat, int).
     */

    return cov(X, 0);
  }

  /**
   * Returns the covariance of the provided matrix, where the ({@code i}, {@code j})-th entry is the correlation between
   * the {@code i}th column and {@code j}th column normalised by {@code X.n_elem -1} ({@code norm_type} = 0) or
   * {@code X.n_elem} ({@code norm_type} = 1).
   * <p>
   * <b>Note:</b> The method behaves like {@link #cov(AbstractVector, int)} if the provided matrix is in the shape of a
   * vector.
   * 
   * @param X The matrix
   * @param norm_type The normalisation
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   * @throws IllegalArgumentException The specified normalisation ({@code norm_type}) must either be 0 or 1.
   */
  public static Mat cov(final Mat X, final int norm_type) throws RuntimeException, IllegalArgumentException {
    /*
     * The parameters "X" and "norm_type" are validated within cov(AbstractVector, AbstractVector, int), if the provided
     * matrices are in the shape of a vector, and otherwise within sum(Class<T>, Mat) (excluding "norm_type" in this
     * case).
     */

    if (X.is_rowvec()) {
      return new Mat(new double[]{cov(new Row(X), norm_type)});
    } else if (X.is_colvec()) {
      return new Mat(new double[]{cov(new Col(X), norm_type)});
    }

    Row sum = sum(Row.class, X);

    Mat result = trans(X).times(X);
    result.inPlace(Op.MINUS, trans(sum).times(sum).divide(X.n_rows));

    switch (norm_type) {
      case 0:
        if (X.n_rows > 1) {
          result.inPlace(Op.DIVIDE, X.n_rows - 1);
        }
        break;
      case 1:
        result.inPlace(Op.DIVIDE, X.n_rows);
        break;
      default:
        throw new IllegalArgumentException("The specified normalisation (" + norm_type + ") must either be 0 or 1.");
    }

    return result;
  }

  /**
   * TODO
   * 
   * @param result TODO
   * @param A TODO
   * @param B TODO
   */
  protected static void cross(final double[] result, final double[] A, final double[] B) {
    result[0] = A[1] * B[2] - A[2] * B[1];
    result[1] = A[2] * B[0] - A[0] * B[2];
    result[2] = A[0] * B[1] - A[1] * B[0];
  }

  /**
   * Returns the cross product between the provided vectors.
   * 
   * @param A The first vector
   * @param B The second vector
   * 
   * @return TODO
   * 
   * @throws RuntimeException The first provided ({@code A.n_rows}, {@code A.n_cols})-matrix must be equivalent in shape
   *           to a 3-dimensional vector.
   * @throws RuntimeException The first provided ({@code B.n_rows}, {@code B.n_cols})-matrix must be equivalent in shape
   *           to a 3-dimensional vector.
   */
  public static Col cross(final Col A, final AbstractMat B) throws RuntimeException {
    if (A.n_elem != 3) {
      throw new RuntimeException("The first provided (" + A.n_rows + ", " + A.n_cols + ")-matrix must be equivalent in shape to a 3-dimensional vector.");
    }

    if (!B.is_vec() || B.n_elem != 3) {
      throw new RuntimeException("The second provided (" + B.n_rows + ", " + B.n_cols + ")-matrix must be equivalent in shape to a 3-dimensional vector.");
    }

    Col result = new Col(3);

    cross(result._data, A._data, B._data);
    return result;
  }

  /**
   * Returns the cross product between the provided vectors.
   * 
   * @param A The first vector
   * @param B The second vector
   * 
   * @return TODO
   * 
   * @throws RuntimeException The first provided ({@code A.n_rows}, {@code A.n_cols})-matrix must be equivalent in shape
   *           to a 3-dimensional vector.
   * @throws RuntimeException The first provided ({@code B.n_rows}, {@code B.n_cols})-matrix must be equivalent in shape
   *           to a 3-dimensional vector.
   */
  public static Row cross(final Row A, final AbstractMat B) throws RuntimeException {
    if (A.n_elem != 3) {
      throw new RuntimeException("The first provided (" + A.n_rows + ", " + A.n_cols + ")-matrix must be equivalent in shape to a 3-dimensional vector.");
    }

    if (!B.is_vec() || B.n_elem != 3) {
      throw new RuntimeException("The second provided (" + B.n_rows + ", " + B.n_cols + ")-matrix must be equivalent in shape to a 3-dimensional vector.");
    }

    Row result = new Row(3);

    cross(result._data, A._data, B._data);
    return result;
  }

  /**
   * Returns the cross product between the provided vectors.
   * 
   * @param A The first vector
   * @param B The second vector
   * 
   * @return TODO
   * 
   * @throws RuntimeException The first provided ({@code A.n_rows}, {@code A.n_cols})-matrix must be equivalent in shape
   *           to a 3-dimensional vector.
   * @throws RuntimeException The first provided ({@code B.n_rows}, {@code B.n_cols})-matrix must be equivalent in shape
   *           to a 3-dimensional vector.
   */
  public static Mat cross(final Mat A, final AbstractMat B) throws RuntimeException {
    if (!A.is_vec() || A.n_elem != 3) {
      throw new RuntimeException("The first provided (" + A.n_rows + ", " + A.n_cols + ")-matrix must be equivalent in shape to a 3-dimensional vector.");
    }

    if (!B.is_vec() || B.n_elem != 3) {
      throw new RuntimeException("The second provided (" + B.n_rows + ", " + B.n_cols + ")-matrix must be equivalent in shape to a 3-dimensional vector.");
    }

    Mat result = new Mat();
    result.copy_size(A);

    cross(result._data, A._data, B._data);
    return result;
  }

  /**
   * TODO
   * 
   * @param result TODO
   * @param V TODO
   */
  protected static void cumsum(final double[] result, final double[] V) {
    result[0] = V[0];
    for (int n = 1; n < result.length; n++) {
      result[n] = result[n - 1] + V[n];
    }
  }

  /**
   * Returns the cumulative sum of the provided vector.
   * 
   * @param V The vector
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code V.n_rows}, {@code V.n_cols})-vector must have at least one element.
   */
  public static Col cumsum(final Col V) throws RuntimeException {
    if (V.is_empty()) {
      throw new RuntimeException("The provided (" + V.n_rows + ", " + V.n_cols + ")-vector must have at least one element.");
    }

    Col result = new Col(V.n_elem);
    cumsum(result._data, V._data);
    return result;
  }

  /**
   * Returns the cumulative sum of the provided vector.
   * 
   * @param V The vector
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code V.n_rows}, {@code V.n_cols})-vector must have at least one element.
   */
  public static Row cumsum(final Row V) throws RuntimeException {
    if (V.is_empty()) {
      throw new RuntimeException("The provided (" + V.n_rows + ", " + V.n_cols + ")-vector must have at least one element.");
    }

    Row result = new Row(V.n_elem);
    cumsum(result._data, V._data);
    return result;
  }

  /**
   * TODO
   * 
   * @param result TODO
   * @param V TODO
   */
  protected static void cumsum(final AbstractView result, final AbstractView V) {
    V.iteratorReset();
    result.iteratorReset();

    int previousPosition = result.iteratorNext();

    result._data[previousPosition] = V._data[V.iteratorNext()];
    for (int n = 1; n < result.n_elem; n++) {
      int currentPosition = result.iteratorNext();
      result._data[currentPosition] = result._data[previousPosition] + V._data[V.iteratorNext()];
      previousPosition = currentPosition;
    }
  }

  /**
   * Returns the cumulative sum for each column of the provided matrix.
   * 
   * @param X The matrix
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   * @throws IllegalArgumentException The specified dimension ({@code dim}) must either be 0 or 1.
   */
  public static Mat cumsum(final Mat X) throws RuntimeException {
    /*
     * The parameter "X" is validated within cumsum(Mat, int).
     */

    return cumsum(X, 0);
  }

  /**
   * Returns the cumulative sum for each column ({@code dim} = 0) or row ({@code dim} = 1) of the provided matrix.
   * 
   * @param X The matrix
   * @param dim The dimension
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   * @throws IllegalArgumentException The specified dimension ({@code dim}) must either be 0 or 1.
   */
  public static Mat cumsum(final Mat X, final int dim) throws RuntimeException, IllegalArgumentException {
    if (X.is_empty()) {
      throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must have at least one element.");
    }

    Mat result = new Mat(X.n_rows, X.n_cols);

    switch (dim) {
      case 0:
        for (int j = 0; j < X.n_cols; j++) {
          cumsum(new ViewSubCol(result, j), new ViewSubCol(X, j));
        }
        break;
      case 1:
        for (int i = 0; i < X.n_rows; i++) {
          cumsum(new ViewSubRow(result, i), new ViewSubRow(X, i));
        }
        break;
      default:
        throw new IllegalArgumentException("The specified dimension (" + dim + ") must either be 0 or 1.");
    }

    return result;
  }

  /**
   * Returns a diagonal matrix with the provided vector set as the main diagonal.
   * 
   * @param X The vector
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-vector must have at least one element.
   */
  public static Mat diagmat(final AbstractVector X) throws RuntimeException {
    if (X.is_empty()) {
      throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-vector must have at least one element.");
    }

    Mat result = new Mat(X.n_elem, X.n_elem);
    new ViewDiag(result, 0).inPlace(Op.EQUAL, X);
    return result;
  }

  /**
   * Returns a diagonal matrix with all elements of the provided square matrix besides the main diagonal set to zero.
   * 
   * @param X The matrix
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must be square.
   */
  public static Mat diagmat(final Mat X) throws RuntimeException {
    if (X.empty()) {
      throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must have at least one element.");
    }

    if (!X.is_square()) {
      throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must be square.");
    }

    /*
     * All uninitialised matrices are already equal to a zero matrix.
     */
    Mat result = new Mat(X.n_rows, X.n_cols);
    new ViewDiag(result, 0).inPlace(Op.EQUAL, new ViewDiag(X, 0));
    return result;
  }

  /**
   * Returns a column vector containing the all positions of non-zero elements within the provided matrix.
   * 
   * @param X The matrix
   * 
   * @return TODO
   */
  public static Col find(final AbstractMat X) {
    /*
     * The parameter "X" is validated within find(AbstractMat, int).
     */

    return find(X, 0);
  }

  /**
   * Returns a column vector containing the all ({@code k} = 0) or at most {@code k} ({@code k} &gt; 0) positions of the
   * first non-zero elements within the provided matrix.
   * 
   * @param X The matrix
   * @param k The number of positions
   * 
   * @return TODO
   * 
   * @throws NegativeArraySizeException The specified number of positions ({@code k}) must be positive.
   */
  public static Col find(final AbstractMat X, final int k) throws NegativeArraySizeException {
    /*
     * The parameters "X" and "k" are validated within cumsum(AbstractMat, int, String).
     */

    return find(X, k, "first");
  }

  /**
   * Returns a column vector containing the all ({@code k} = 0) or at most {@code k} ({@code k} &gt; 0) positions of the
   * first ({@code s} = 'first') or last ({@code s} = 'last') non-zero elements within the provided matrix.
   * 
   * @param X The matrix
   * @param k The number of positions
   * @param s The search direction
   * 
   * @return TODO
   * 
   * @throws NegativeArraySizeException The specified number of positions ({@code k}) must be positive.
   * @throws IllegalArgumentException The norm sorting order ({@code s}) must be one of 'first' or 'last'.
   */
  public static Col find(final AbstractMat X, final int k, final String s) throws NegativeArraySizeException, IllegalArgumentException {
    if (k < 0) {
      throw new NegativeArraySizeException("The specified number of positions (" + k + ") must be positive.");
    }

    double[] temp = new double[X.n_elem];

    int limit;
    if (k > 0) {
      limit = k;
    } else {
      limit = X.n_elem;
    }

    int index = 0;
    switch (s) {
      case "first":
        for (int n = 0; n < X.n_elem && index < limit; n++) {
          if (X._data[n] != 0) {
            temp[index++] = n;
          }
        }
        break;
      case "last":
        for (int n = X.n_elem - 1; n >= 0 && index < limit; n--) {
          if (X._data[n] != 0) {
            temp[index++] = n;
          }
        }
        break;
      default:
        throw new IllegalArgumentException("The search direction (" + s + ") must be one of 'first' or 'last'.");
    }

    Col result = new Col(index);
    if (index > 0) {
      System.arraycopy(temp, 0, result._data, 0, index);
    }

    if (s.equals("last") && result.n_elem > 0) {
      revert(result._data, result._data);
    }

    return result;
  }

  /**
   * Returns a deep copy of the matrix with reverse order of its columns.
   * 
   * @param X The matrix
   * 
   * @return TODO
   */
  public static Mat fliplr(final Mat X) {
    Mat result = new Mat(X.n_rows, X.n_cols);

    for (int j = 0; j < result.n_cols; j++) {
      System.arraycopy(X._data, (result.n_cols - (j + 1)) * X.n_rows, result._data, j * X.n_rows, X.n_rows);
    }

    return result;
  }

  /**
   * Returns a deep copy of the matrix with reverse order of its rows.
   * 
   * @param X The matrix
   * 
   * @return TODO
   */
  public static Mat flipud(final Mat X) {
    Mat result = new Mat(X.n_rows, X.n_cols);

    for (int i = 0; i < result.n_rows; i++) {
      new ViewSubRow(result, i).inPlace(Op.EQUAL, new ViewSubRow(X, X.n_rows - (i + 1)));
    }

    return result;
  }

  /**
   * Returns the histogramm for each column of the provided matrix by using 10 uniformly distributed bins placed in
   * regards to the range of values within the matrix.
   * 
   * @param X The matrix
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   */
  public static Mat hist(final AbstractMat X) throws RuntimeException {
    /*
     * The parameter "X" is validated within hist(Mat, int).
     */
    return hist(X, 10);
  }

  /**
   * Returns the histogramm for each column of the provided matrix by using the specified number of uniformly
   * distributed bins placed in regards to the range of values within the matrix.
   * 
   * @param X The matrix
   * @param n_bins The number of bins
   * 
   * @return TODO
   * 
   * @throws NegativeArraySizeException The specified number of bins ({@code n_bins}) must be positive.
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   */
  public static Mat hist(final AbstractMat X, final int n_bins) throws NegativeArraySizeException, RuntimeException {
    /*
     * The parameters "X" and "n_bins" are validated within hist(Mat, int, int).
     */
    return hist(X, n_bins, 0);
  }

  /**
   * Returns the histogramm for each column ({@code dim} = 0) or row ({@code dim} = 1) of the provided matrix by using
   * the specified number of uniformly distributed bins placed in regards to the range of values within the matrix.
   * <p>
   * <b>Note:</b> The specified dimension will be ignored if the provided matrix is in the shape of a vector.
   * 
   * @param X The matrix
   * @param n_bins The number of bins
   * @param dim The dimension
   * 
   * @return TODO
   * 
   * @throws NegativeArraySizeException The specified number of bins ({@code n_bins}) must be positive.
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   * @throws IllegalArgumentException The specified dimension ({@code dim}) must either be 0 or 1.
   */
  public static Mat hist(final AbstractMat X, final int n_bins, int dim) throws NegativeArraySizeException, RuntimeException, IllegalArgumentException {
    /*
     * The parameter "dim" is validated within hist(Mat, AbstractMat, int).
     */

    if (n_bins < 0) {
      throw new NegativeArraySizeException("The specified number of bins (" + n_bins + ") must be positive.");
    }

    if (X.empty()) {
      throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must have at least one element.");
    }

    double minimum = X._data[0];
    double maximum = X._data[0];
    for (int n = 1; n < X.n_elem; n++) {
      double value = X._data[n];
      minimum = Math.min(minimum, value);
      maximum = Math.max(maximum, value);
    }

    if (Double.isInfinite(minimum)) {
      minimum = -Double.MAX_VALUE;
    }

    if (Double.isInfinite(maximum)) {
      maximum = Double.MAX_VALUE;
    }

    Col centers = new Col(n_bins);
    double stepLength = (maximum - minimum) / (double) n_bins;
    for (int n = 0; n < n_bins; n++) {
      /*
       * While increasing the value step by step per stepLength will be faster, it will also reduce the precision.
       */
      centers._data[n] = minimum + stepLength * (0.5 + n);
    }

    return hist(X, centers, dim);
  }

  /**
   * TODO
   * 
   * @param result TODO
   * @param V TODO
   * @param centers TODO
   */
  protected static void hist(final double[] result, final double[] V, final double[] centers) {
    for (int n = 0; n < V.length; n++) {
      double value = V[n];

      if (is_finite(value)) {
        double previousDistance = Math.abs(value - centers[0]);
        int index = 0;

        for (int nn = 1; nn < centers.length; nn++) {
          double currentDistance = Math.abs(value - centers[nn]);

          if (currentDistance < previousDistance) {
            previousDistance = currentDistance;

            ++index;
          } else {
            break;
          }
        }

        result[index]++;
      } else {
        if (value < 0) {
          result[0]++;
        }

        if (value > 0) {
          result[result.length - 1]++;
        }
      }
    }
  }

  /**
   * Returns the histogramm for each column of the provided matrix for the provided, monotonically increasing bin
   * centers.
   * 
   * @param X The matrix
   * @param centers The bin centers
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   */
  public static Mat hist(final AbstractMat X, final AbstractMat centers) throws RuntimeException {
    return hist(X, centers, 0);
  }

  /**
   * Returns the histogramm for each column ({@code dim} = 0) or row ({@code dim} = 1) of the provided matrix for the
   * provided, monotonically increasing bin centers.
   * <p>
   * <b>Note:</b> The specified dimension will be ignored if the provided matrix is in the shape of a vector.
   * 
   * @param X The matrix
   * @param centers The bin centers
   * @param dim The dimension
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   * @throws IllegalArgumentException The specified dimension ({@code dim}) must either be 0 or 1.
   */
  public static Mat hist(final AbstractMat X, final AbstractMat centers, final int dim) throws RuntimeException, IllegalArgumentException {
    if (X.is_empty()) {
      throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must have at least one element.");
    }

    Mat result = new Mat();

    if (X.is_rowvec()) {
      result.set_size(1, centers.n_elem);
      hist(result._data, X._data, centers._data);
      return result;
    } else if (X.is_colvec()) {
      result.set_size(centers.n_elem, 1);
      hist(result._data, X._data, centers._data);
      return result;
    }
    else {
      Mat matrixX = new Mat(X);

      switch (dim) {
        case 0:
          result.set_size(centers.n_elem, X.n_cols);

          for (int j = 0; j < X.n_cols; j++) {
            new ViewSubCol(result, j).inPlace(Op.EQUAL, hist(matrixX.col(j), centers));
          }
          break;
        case 1:
          result.set_size(X.n_rows, centers.n_elem);

          for (int i = 0; i < X.n_rows; i++) {
            new ViewSubRow(result, i).inPlace(Op.EQUAL, hist(matrixX.row(i), centers));
          }
          break;
        default:
          throw new IllegalArgumentException("The specified dimension (" + dim + ") must either be 0 or 1.");
      }

      return result;
    }
  }

  /**
   * TODO
   * 
   * @param result TODO
   * @param V TODO
   * @param edges TODO
   */
  protected static void histc(final double[] result, final double[] V, final double[] edges) {
    for (int n = 0; n < V.length; n++) {
      double value = V[n];

      for (int nn = 0; nn < edges.length - 1; nn++) {
        if (edges[nn] <= value && value < edges[nn + 1]) {
          result[nn]++;
          break;
        } else if (value == edges[edges.length - 1]) {
          result[edges.length - 1]++;
          break;
        }
      }
    }
  }

  /**
   * Returns the histogramm for each column of the provided matrix for the provided, monotonically increasing bin edges.
   * 
   * @param X The matrix
   * @param edges The bin edges
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   */
  public static Mat histc(final AbstractMat X, final AbstractMat edges) throws RuntimeException {
    return histc(X, edges, 0);
  }

  /**
   * Returns the histogramm for each column ({@code dim} = 0) or row ({@code dim} = 1) of the provided matrix for the
   * provided, monotonically increasing bin edges.
   * 
   * @param X The matrix
   * @param edges The bin edges
   * @param dim The dimension
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   * @throws IllegalArgumentException The specified dimension ({@code dim}) must either be 0 or 1.
   */
  public static Mat histc(final AbstractMat X, final AbstractMat edges, final int dim) throws RuntimeException, IllegalArgumentException {
    if (X.is_empty()) {
      throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-vector must have at least one element.");
    }

    Mat result = new Mat();

    if (X.is_rowvec()) {
      result.set_size(1, edges.n_elem);
      histc(result._data, X._data, edges._data);
      return result;
    } else if (X.is_colvec()) {
      result.set_size(edges.n_elem, 1);
      histc(result._data, X._data, edges._data);
      return result;
    }
    else {
      Mat matrixX = new Mat(X);

      switch (dim) {
        case 0:
          result.set_size(edges.n_elem, X.n_cols);

          for (int j = 0; j < X.n_cols; j++) {
            new ViewSubCol(result, j).inPlace(Op.EQUAL, histc(matrixX.col(j), edges));
          }
          break;
        case 1:
          result.set_size(X.n_rows, edges.n_elem);

          for (int i = 0; i < X.n_rows; i++) {
            new ViewSubRow(result, i).inPlace(Op.EQUAL, histc(matrixX.row(i), edges));
          }
          break;
        default:
          throw new IllegalArgumentException("The specified dimension (" + dim + ") must either be 0 or 1.");
      }

      return result;
    }
  }

  /**
   * Replaces the matrix with its tranpose in-place.
   * 
   * @param X The matrix
   */
  public static void inplace_trans(final Mat X) {
    int n_rows = X.n_rows;
    int n_cols = X.n_cols;

    if (X.is_vec()) {
      X.n_rows = n_cols;
      X.n_cols = n_rows;
      X.n_elem = X.n_rows * X.n_cols;
    } else if (n_rows == n_cols) {
      X.n_rows = n_cols;
      X.n_cols = n_rows;
      X.n_elem = X.n_rows * X.n_cols;

      int n = 0;
      for (int i = 0; i < n_rows; i++) {
        for (int j = 0; j < n_cols; j++) {
          if (i < j) {
            double temp = X._data[i + j * n_rows];
            X._data[i + j * n_rows] = X._data[n];
            X._data[n] = temp;
          }

          n++;
        }
      }
    } else {
      X.inPlace(Op.EQUAL, X.t());
    }

  }

  /**
   * Returns the concatenation of the provided matrices along their rows.
   * <p>
   * The first provided matrix will be placed left and second one right.
   * 
   * @param A The first matrix
   * @param B The second matrix
   * 
   * @return TODO
   * 
   * @throws RuntimeException Both matrices must have the same number of rows ({@code A.n_rows} and {@code B.n_rows}).
   */
  public static Mat join_rows(final AbstractMat A, final AbstractMat B) throws RuntimeException {
    return join_horiz(A, B);
  }

  /**
   * Returns the concatenation of the provided matrices along their rows.
   * <p>
   * The first provided matrix will be placed left and second one right.
   * 
   * @param A The first matrix
   * @param B The second matrix
   * 
   * @return TODO
   * 
   * @throws RuntimeException Both matrices must have the same number of rows ({@code A.n_rows} and {@code B.n_rows}).
   */
  public static Mat join_horiz(final AbstractMat A, final AbstractMat B) throws RuntimeException {
    if (A.n_rows != B.n_rows) {
      throw new RuntimeException("Both matrices must have the same number of rows (" + A.n_rows + " and " + B.n_rows + ").");
    }

    Mat result = new Mat(A.n_rows, A.n_cols + B.n_cols);

    System.arraycopy(A._data, 0, result._data, 0, A.n_elem);
    System.arraycopy(B._data, 0, result._data, A.n_elem, B.n_elem);

    return result;
  }

  /**
   * Returns the concatenation of the provided matrices along their columns.
   * <p>
   * The first provided matrix will be placed above and second one below.
   * 
   * @param A The first matrix
   * @param B The second matrix
   * 
   * @return TODO
   * 
   * @throws RuntimeException Both matrices must have the same number of columns ({@code A.n_cols} and {@code B.n_cols}
   *           ).
   */
  public static Mat join_cols(final AbstractMat A, final AbstractMat B) throws RuntimeException {
    return join_vert(A, B);
  }

  /**
   * Returns the concatenation of the provided matrices along their columns.
   * <p>
   * The first provided matrix will be placed above and second one below.
   * 
   * @param A The first matrix
   * @param B The second matrix
   * 
   * @return TODO
   * 
   * @throws RuntimeException Both matrices must have the same number of columns ({@code A.n_cols} and {@code B.n_cols}
   *           ).
   */
  public static Mat join_vert(final AbstractMat A, final AbstractMat B) throws RuntimeException {
    if (A.n_cols != B.n_cols) {
      throw new RuntimeException("Both matrices must have the same number of columns (" + A.n_cols + " and " + B.n_cols + ").");
    }

    Mat result = new Mat(A.n_rows + B.n_rows, A.n_cols);

    new ViewSubRows(result, 0, A.n_rows).inPlace(Op.EQUAL, A);
    new ViewSubRows(result, A.n_rows, B.n_rows).inPlace(Op.EQUAL, B);

    return result;
  }

  /**
   * Returns the kronecker product of the provided matrices.
   * 
   * @param A The left-hand matrix
   * @param B The right-hand matrix
   * 
   * @return TODO
   */
  public static Mat kron(final AbstractMat A, final AbstractMat B) {
    Mat result = repmat(B, A.n_rows, A.n_cols);

    int n = 0;
    for (int j = 0; j < A.n_cols; j++) {
      for (int i = 0; i < A.n_rows; i++) {
        AbstractView subView = new ViewSubMat(result, i * B.n_rows, j * B.n_cols, B.n_rows, B.n_cols);

        double value = A._data[n++];
        subView.iteratorReset();
        while (subView.iteratorHasNext()) {
          result._data[subView.iteratorNext()] *= value;
        }
      }
    }

    return result;
  }

  /**
   * Resizes the provided matrix to the specified number of rows and columns and reuses existing values in a column-wise
   * manner.
   * 
   * @param mat The matrix
   * @param n_rows The number of rows
   * @param n_cols The number of columns
   * 
   * @return TODO
   * 
   * @throws NegativeArraySizeException The specified number of rows ({@code n_rows}) must be positive.
   * @throws NegativeArraySizeException The specified number of columns ({@code n_cols}) must be positive.
   */
  public static Mat reshape(final Mat mat, final int n_rows, final int n_cols) throws NegativeArraySizeException {
    /*
     * The parameters "n_rows" and "n_cols" are validated within Mat.resize(int, int).
     */

    Mat result = new Mat(mat);
    result.reshape(n_rows, n_cols);
    return result;
  }

  /**
   * Resizes the matrix to the specified number of rows and columns and preserves existing values at their current
   * position.
   * 
   * @param mat The matrix
   * @param n_rows The number of rows
   * @param n_cols The number of columns
   * 
   * @return TODO
   * 
   * @throws NegativeArraySizeException The specified number of rows ({@code n_rows}) must be positive.
   * @throws NegativeArraySizeException The specified number of columns ({@code n_cols}) must be positive.
   */
  public static Mat resize(final Mat mat, final int n_rows, final int n_cols) throws NegativeArraySizeException {
    /*
     * The parameters "n_rows" and "n_cols" are validated within Mat.resize(int, int).
     */

    Mat result = new Mat(mat);
    result.resize(n_rows, n_cols);
    return result;
  }

  /**
   * TODO
   * 
   * @param result TODO
   * @param V TODO
   */
  protected static void shuffle(final double[] result, final double[] V) {
    /*
     * Performs an inside-out version of the Fisher-Yates shuffle
     */
    result[0] = V[0];
    for (int n = 1; n < V.length; n++)
    {
      int nn = RNG._rng.nextInt(n + 1);

      if (n != nn) {
        result[n] = result[nn];
      }

      result[nn] = V[n];
    }
  }

  /**
   * TODO
   * 
   * @param result TODO
   * @param V TODO
   */
  protected static void shuffle(final int[] result, final int[] V) {
    /*
     * Performs an inside-out version of the Fisher-Yates shuffle
     */
    result[0] = V[0];
    for (int n = 1; n < V.length; n++)
    {
      int nn = RNG._rng.nextInt(n + 1);

      if (n != nn) {
        result[n] = result[nn];
      }

      result[nn] = V[n];
    }
  }

  /**
   * Returns a shuffled copy of the provided vector.
   * 
   * @param V The vector
   * 
   * @return TODO
   */
  public static Col shuffle(final Col V) {
    Col result = new Col(V.n_elem);
    shuffle(result._data, V._data);
    return result;
  }

  /**
   * Returns a shuffled copy of the provided vector.
   * 
   * @param V The vector
   * 
   * @return TODO
   */
  public static Row shuffle(final Row V) {
    Row result = new Row(V.n_elem);
    shuffle(result._data, V._data);
    return result;
  }

  /**
   * Returns a copy of the provided matrix with shuffled columns.
   * 
   * @param X The matrix
   * 
   * @return TODO
   */
  public static Mat shuffle(final Mat X) {
    return shuffle(X, 0);
  }

  /**
   * Returns a copy of the provided matrix with shuffled columns ({@code dim} = 0) or rows ({@code dim} = 1).
   * 
   * @param X The matrix
   * @param dim The dimension
   * 
   * @return TODO
   * 
   * @throws RuntimeException TODO
   * @throws IllegalArgumentException The specified dimension ({@code dim}) must either be 0 or 1.
   */
  public static Mat shuffle(final Mat X, final int dim) throws RuntimeException {
    Mat result = new Mat();
    result.copy_size(X);

    int[] indicies;
    switch (dim) {
      case 0:
        indicies = new int[X.n_rows];
        break;
      case 1:
        indicies = new int[X.n_cols];
        break;
      default:
        throw new IllegalArgumentException("The specified dimension (" + dim + ") must either be 0 or 1.");
    }

    for (int n = 0; n < indicies.length; n++) {
      indicies[n] = n;
    }

    int[] shuffeldIndicies = new int[indicies.length];
    shuffle(shuffeldIndicies, indicies);

    switch (dim) {
      case 0:
        for (int i = 0; i < X.n_rows; i++) {
          new ViewSubRow(result, i).inPlace(Op.EQUAL, new ViewSubRow(X, shuffeldIndicies[i]));
        }
        break;
      case 1:
        for (int j = 0; j < X.n_cols; j++) {
          new ViewSubCol(result, j).inPlace(Op.EQUAL, new ViewSubCol(X, shuffeldIndicies[j]));
        }
        break;
    }

    return result;
  }

  /**
   * TODO
   * 
   * @param result TODO
   * @param V TODO
   * @param sort_direction TODO
   */
  protected static void sort(final double[] result, final double[] V, final String sort_direction) {
    System.arraycopy(V, 0, result, 0, V.length);

    Arrays.sort(result);

    if (sort_direction.equals("descend")) {
      revert(result, result);
    }
  }

  /**
   * Returns a copy of the provided vector sorted in ascending order.
   * 
   * @param V The vector
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided vector must be finite.
   */
  public static Col sort(final Col V) throws RuntimeException {
    return sort(V, "ascend");
  }

  /**
   * Returns a copy of the provided vector sorted in ascending ({@code sort_direction} = 'ascend') or descending (
   * {@code sort_direction} = 'descend') order.
   * 
   * @param V The vector
   * @param sort_direction TODO
   * 
   * @return TODO
   * 
   * @throws IllegalArgumentException The specified sort direction ({@code sort_direction}) must either be 'ascend' or
   *           'descend'.
   * @throws RuntimeException The provided vector must be finite.
   */
  public static Col sort(final Col V, final String sort_direction) throws IllegalArgumentException, RuntimeException {
    if (!sort_direction.equals("ascend") && !sort_direction.equals("descend")) {
      throw new IllegalArgumentException("The specified sort direction (" + sort_direction + ") must either be 'ascend' or 'descend'.");
    }

    if (!V.is_finite()) {
      throw new RuntimeException("The provided vector must be finite.");
    }

    Col result = new Col(V.n_elem);
    sort(result._data, V._data, sort_direction);
    return result;
  }

  /**
   * Returns a copy of the provided vector sorted in ascending order.
   * 
   * @param V The vector
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided vector must be finite.
   */
  public static Row sort(final Row V) throws RuntimeException {
    return sort(V, "ascend");
  }

  /**
   * Returns a copy of the provided vector sorted in ascending ({@code sort_direction} = 'ascend') or descending (
   * {@code sort_direction} = 'descend') order.
   * 
   * @param V The vector
   * @param sort_direction TODO
   * 
   * @return TODO
   * 
   * @throws IllegalArgumentException The specified sort direction ({@code sort_direction}) must either be 'ascend' or
   *           'descend'.
   * @throws RuntimeException The provided vector must be finite.
   */
  public static Row sort(final Row V, final String sort_direction) throws IllegalArgumentException, RuntimeException {
    if (!sort_direction.equals("ascend") && !sort_direction.equals("descend")) {
      throw new IllegalArgumentException("The specified sort direction (" + sort_direction + ") must either be 'ascend' or 'descend'.");
    }

    if (!V.is_finite()) {
      throw new RuntimeException("The provided vector must be finite.");
    }

    Row result = new Row(V.n_elem);
    sort(result._data, V._data, sort_direction);
    return result;
  }

  /**
   * Returns a copy of the provided vector with each column sorted in ascending order.
   * 
   * @param X The matrix
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided matrix must be finite.
   */
  public static Mat sort(final Mat X) throws RuntimeException {
    return sort(X, "ascend");
  }

  /**
   * Returns a copy of the provided matrix with each column ({@code dim} = 0) or row ({@code dim} = 1) sorted in
   * ascending ({@code sort_direction} = 'ascend') or descending ({@code sort_direction} = 'descend') order.
   * 
   * @param X The matrix
   * @param sort_direction TODO
   * 
   * @return TODO
   * 
   * @throws IllegalArgumentException The specified sort direction ({@code sort_direction}) must either be 'ascend' or
   *           'descend'.
   * @throws RuntimeException The provided matrix must be finite.
   */
  public static Mat sort(final Mat X, final String sort_direction) throws IllegalArgumentException, RuntimeException {
    return sort(X, sort_direction, 0);
  }

  /**
   * Returns a copy of the provided matrix with each column sorted in ascending ({@code sort_direction} = 'ascend') or
   * descending ({@code sort_direction} = 'descend') order.
   * 
   * @param X The matrix
   * @param sort_direction TODO
   * @param dim TODO
   * 
   * @return TODO
   * 
   * @throws IllegalArgumentException The specified sort direction ({@code sort_direction}) must either be 'ascend' or
   *           'descend'.
   * @throws IllegalArgumentException The specified dimension ({@code dim}) must either be 0 or 1.
   * @throws RuntimeException The provided matrix must be finite.
   */
  public static Mat sort(final Mat X, final String sort_direction, final int dim) throws IllegalArgumentException, RuntimeException {
    if (!sort_direction.equals("ascend") && !sort_direction.equals("descend")) {
      throw new IllegalArgumentException("The specified sort direction (" + sort_direction + ") must either be 'ascend' or 'descend'.");
    }

    Mat result = new Mat(X.n_rows, X.n_cols);

    if (!X.is_finite()) {
      throw new RuntimeException("The provided matrix must be finite.");
    }

    switch (dim) {
      case 0:
        for (int j = 0; j < X.n_cols; j++) {
          /*
           * Creates a deep copy of each column, since sorting of shallow sub views is not yet implemented.
           */
          new ViewSubCol(result, j).inPlace(Op.EQUAL, sort(X.col(j), sort_direction));
        }
        break;
      case 1:
        for (int i = 0; i < X.n_rows; i++) {
          /*
           * Creates a deep copy of each row, since sorting of shallow sub views is not yet implemented.
           */
          new ViewSubRow(result, i).inPlace(Op.EQUAL, sort(X.row(i), sort_direction));
        }
        break;
      default:
        throw new IllegalArgumentException("The specified dimension (" + dim + ") must either be 0 or 1.");
    }

    return result;
  }

  /**
   * TODO
   * 
   * @param result TODO
   * @param V TODO
   * @param sort_direction TODO
   */
  protected static void sort_index(final double[] result, final double[] V, final String sort_direction) {
    stable_sort_index(result, V, sort_direction);
  }

  /**
   * Returns a vector of indicies that is arranged based on the ascending order of the elements of the provided
   * vector.
   * 
   * @param V The vector
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided vector must be finite.
   */
  public static Col sort_index(final Col V) throws RuntimeException {
    return stable_sort_index(V, "ascend");
  }

  /**
   * Returns a vector of indicies that is arranged based on the ascending ({@code sort_direction} = 'ascend') or
   * descending ({@code sort_direction} = 'descend') order of the elements of the provided vector.
   * 
   * @param V The vector
   * @param sort_direction TODO
   * 
   * @return TODO
   * 
   * @throws IllegalArgumentException The specified sort direction ({@code sort_direction}) must either be 'ascend' or
   *           'descend'.
   * @throws RuntimeException The provided vector must be finite.
   */
  public static Col sort_index(final Col V, final String sort_direction) throws IllegalArgumentException, RuntimeException {
    if (!sort_direction.equals("ascend") && !sort_direction.equals("descend")) {
      throw new IllegalArgumentException("The specified sort direction (" + sort_direction + ") must either be 'ascend' or 'descend'.");
    }

    if (!V.is_finite()) {
      throw new RuntimeException("The provided vector must be finite.");
    }

    Col result = new Col(V.n_elem);
    sort_index(result._data, V._data, sort_direction);
    return result;
  }

  /**
   * Returns a vector of indicies that is arranged based on the ascending order of the elements of the provided
   * vector.
   * 
   * @param V The vector
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided vector must be finite.
   */
  public static Row sort_index(final Row V) throws RuntimeException {
    return stable_sort_index(V, "ascend");
  }

  /**
   * Returns a vector of indicies that is arranged based on the ascending ({@code sort_direction} = 'ascend') or
   * descending ({@code sort_direction} = 'descend') order of the elements of the provided vector.
   * 
   * @param V The vector
   * @param sort_direction TODO
   * 
   * @return TODO
   * 
   * @throws IllegalArgumentException The specified sort direction ({@code sort_direction}) must either be 'ascend' or
   *           'descend'.
   * @throws RuntimeException The provided vector must be finite.
   */
  public static Row sort_index(final Row V, final String sort_direction) throws IllegalArgumentException, RuntimeException {
    if (!sort_direction.equals("ascend") && !sort_direction.equals("descend")) {
      throw new IllegalArgumentException("The specified sort direction (" + sort_direction + ") must either be 'ascend' or 'descend'.");
    }

    if (!V.is_finite()) {
      throw new RuntimeException("The provided vector must be finite.");
    }

    Row result = new Row(V.n_elem);
    sort_index(result._data, V._data, sort_direction);
    return result;
  }

  /**
   * TODO
   * 
   * @param result TODO
   * @param V TODO
   * @param sort_direction TODO
   */
  protected static void stable_sort_index(final double[] result, final double[] V, final String sort_direction) {
    Integer[] temp = new Integer[result.length];
    for (int n = 0; n < temp.length; n++) {
      temp[n] = n;
    }

    if (sort_direction.equals("descend")) {
      Arrays.sort(temp, new Comparator<Integer>() {
        @Override
        public int compare(final Integer a, final Integer b) {
          return Double.compare(V[b], V[a]);
        }
      });
    } else {
      Arrays.sort(temp, new Comparator<Integer>() {
        @Override
        public int compare(final Integer a, final Integer b) {
          return Double.compare(V[a], V[b]);
        }
      });
    }

    for (int n = 0; n < temp.length; n++) {
      result[n] = temp[n];
    }
  }

  /**
   * Returns a vector of indicies that is arranged based on the ascending order of the elements of the provided
   * vector and preserves the relative order of elements with equal values.
   * 
   * @param V The vector
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided vector must be finite.
   */
  public static Col stable_sort_index(final Col V) throws RuntimeException {
    return stable_sort_index(V, "ascend");
  }

  /**
   * Returns a vector of indicies that is arranged based on the ascending ({@code sort_direction} = 'ascend') or
   * descending ({@code sort_direction} = 'descend') order of the elements of the provided vector and preserves the
   * relative order of elements with equal values.
   * 
   * @param V The vector
   * @param sort_direction TODO
   * 
   * @return TODO
   * 
   * @throws IllegalArgumentException The specified sort direction ({@code sort_direction}) must either be 'ascend' or
   *           'descend'.
   * @throws RuntimeException The provided vector must be finite.
   */
  public static Col stable_sort_index(final Col V, final String sort_direction) throws IllegalArgumentException, RuntimeException {
    if (!sort_direction.equals("ascend") && !sort_direction.equals("descend")) {
      throw new IllegalArgumentException("The specified sort direction (" + sort_direction + ") must either be 'ascend' or 'descend'.");
    }

    Col result = new Col(V.n_elem);
    stable_sort_index(result._data, V._data, sort_direction);
    return result;
  }

  /**
   * Returns a vector of indicies that is arranged based on the ascending order of the elements of the provided
   * vector and preserves the relative order of elements with equal values.
   * 
   * @param V The vector
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided vector must be finite.
   */
  public static Row stable_sort_index(final Row V) throws RuntimeException {
    return stable_sort_index(V, "ascend");
  }

  /**
   * Returns a vector of indicies that is arranged based on the ascending ({@code sort_direction} = 'ascend') or
   * descending ({@code sort_direction} = 'descend') order of the elements of the provided vector and preserves the
   * relative order of elements with equal values.
   * 
   * @param V The vector
   * @param sort_direction TODO
   * 
   * @return TODO
   * 
   * @throws IllegalArgumentException The specified sort direction ({@code sort_direction}) must either be 'ascend' or
   *           'descend'.
   * @throws RuntimeException The provided vector must be finite.
   */
  public static Row stable_sort_index(final Row V, final String sort_direction) throws IllegalArgumentException, RuntimeException {
    if (!sort_direction.equals("ascend") && !sort_direction.equals("descend")) {
      throw new IllegalArgumentException("The specified sort direction (" + sort_direction + ") must either be 'ascend' or 'descend'.");
    }

    Row result = new Row(V.n_elem);
    stable_sort_index(result._data, V._data, sort_direction);
    return result;
  }

  /**
   * Returns a symmetric copy of the provided matrix by reflecting the upper triangle to the lower one.
   * 
   * @param A The matrix
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code A.n_rows}, {@code A.n_cols})-matrix must be square.
   */
  public static Mat symmatu(final Mat A) throws RuntimeException {
    if (!A.is_square()) {
      throw new RuntimeException("The provided (" + A.n_rows + ", " + A.n_cols + ")-matrix must be square.");
    }

    Mat result = new Mat();
    result.copy_size(A);

    int n = 0;
    for (int i = 0; i < A.n_rows; i++) {
      for (int j = 0; j < A.n_cols; j++) {
        double value = A._data[n];

        if (i < j) {
          result._data[n] = value;
          result._data[i + j * result.n_rows] = value;
        } else if (i == j) {
          result._data[n] = value;
        }

        n++;
      }
    }

    return result;
  }

  /**
   * Returns a symmetric copy of the provided matrix by reflecting the lower triangle to the upper one.
   * 
   * @param A The matrix
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code A.n_rows}, {@code A.n_cols})-matrix must be square.
   */
  public static Mat symmatl(final Mat A) throws RuntimeException {
    if (!A.is_square()) {
      throw new RuntimeException("The provided (" + A.n_rows + ", " + A.n_cols + ")-matrix must be square.");
    }

    Mat result = new Mat();
    result.copy_size(A);

    int n = 0;
    for (int i = 0; i < A.n_rows; i++) {
      for (int j = 0; j < A.n_cols; j++) {
        double value = A._data[n];

        if (i > j) {
          result._data[n] = value;
          result._data[i + j * result.n_rows] = value;
        } else if (i == j) {
          result._data[n] = value;
        }

        n++;
      }
    }

    return result;
  }

  /**
   * Returns the transpose of the provided matrix.
   * 
   * @param A TODO
   * 
   * @return TODO
   */
  public static Row trans(final Col A) {
    return A.t();
  }

  /**
   * Returns the transpose of the provided matrix.
   * 
   * @param A TODO
   * 
   * @return TODO
   */
  public static Col trans(final Row A) {
    return A.t();
  }

  /**
   * Returns the transpose of the provided matrix.
   * 
   * @param A TODO
   * 
   * @return TODO
   */
  public static Mat trans(final Mat A) {
    return A.t();
  }

  /**
   * Returns a upper triangular copy of the provided matrix by setting the lower triangle to zero.
   * 
   * @param A The matrix
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code A.n_rows}, {@code A.n_cols})-matrix must be square.
   */
  public static Mat trimatu(final Mat A) throws RuntimeException {
    if (!A.is_square()) {
      throw new RuntimeException("The provided (" + A.n_rows + ", " + A.n_cols + ")-matrix must be square.");
    }

    /*
     * All uninitialised matrices are already equal to a zero matrix.
     */
    Mat result = new Mat();
    result.copy_size(A);

    int n = 0;
    for (int j = 0; j < A.n_cols; j++) {
      for (int i = 0; i < A.n_rows; i++) {
        if (i <= j) {
          result._data[n] = A._data[n];
        }
        n++;
      }
    }

    return result;
  }

  /**
   * Returns a lower triangular copy of the provided matrix by setting the upper triangle to zero.
   * 
   * @param A The matrix
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code A.n_rows}, {@code A.n_cols})-matrix must be square.
   */
  public static Mat trimatl(final Mat A) throws RuntimeException {
    if (!A.is_square()) {
      throw new RuntimeException("The provided (" + A.n_rows + ", " + A.n_cols + ")-matrix must be square.");
    }

    /*
     * All uninitialised matrices are already equal to a zero matrix.
     */
    Mat result = new Mat();
    result.copy_size(A);

    int n = 0;
    for (int j = 0; j < A.n_cols; j++) {
      for (int i = 0; i < A.n_rows; i++) {
        if (i >= j) {
          result._data[n] = A._data[n];
        }
        n++;
      }
    }

    return result;
  }

  /**
   * TODO
   * 
   * @param result TODO
   * @param A TODO
   */
  protected static void unique(final AbstractMat result, final double[] A) {
    double[] sortedA = Arrays.copyOf(A, A.length);
    Arrays.sort(sortedA);

    double[] temp = new double[sortedA.length];
    double currentValue = sortedA[0];
    temp[0] = currentValue;

    int n = 1;
    for (int nn = 1; nn < sortedA.length; nn++) {
      double value = sortedA[nn];

      if (value > currentValue || Double.isInfinite(value)) {
        temp[n++] = value;
        currentValue = value;
      }
    }

    result.set_size(n);
    System.arraycopy(temp, 0, result._data, 0, result.n_elem);
  }

  /**
   * Returns a vector containing the unique values of the provided vector in ascending order.
   * 
   * @param A The vector
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code A.n_rows}, {@code A.n_cols})-column vector must have at least one
   *           element.
   */
  public static Col unique(final Col A) {
    if (A.is_empty()) {
      throw new RuntimeException("The provided (" + A.n_rows + ", " + A.n_cols + ")-column vector must have at least one element.");
    }

    Col result = new Col();
    unique(result, A._data);
    return result;
  }

  /**
   * Returns a vector containing the unique values of the provided vector in ascending order.
   * 
   * @param A The vector
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code A.n_rows}, {@code A.n_cols})-row vector must have at least one
   *           element.
   */
  public static Row unique(final Row A) {
    if (A.is_empty()) {
      throw new RuntimeException("The provided (" + A.n_rows + ", " + A.n_cols + ")-row vector must have at least one element.");
    }

    Row result = new Row();
    unique(result, A._data);
    return result;
  }

  /**
   * Returns a matrix in the shape of a column vector containing the unique values of the provided matrix in ascending
   * order.
   * <p>
   * <b>Note:</b> The method behaves like {@link #unique(Row)} if the provided matrix is in the shape of a row vector.
   * 
   * @param A The vector
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code A.n_rows}, {@code A.n_cols})-matrix must have at least one element.
   */
  public static Mat unique(final Mat A) {
    if (A.is_empty()) {
      throw new RuntimeException("The provided (" + A.n_rows + ", " + A.n_cols + ")-matrix must have at least one element.");
    }

    Mat result = new Mat();
    unique(result, A._data);

    if (A.is_rowvec()) {
      return result.t();
    }

    return result;
  }

  /**
   * Returns a vector created by column-wise concatination of the provided matrix A.
   * 
   * @param <T> TODO
   * @param return_type The type of vector to be returned
   * @param A The matrix
   * 
   * @return TODO
   */
  public static <T extends AbstractVector> T vectorise(final Class<T> return_type, final Mat A) {
    return vectorise(return_type, A, 0);
  }

  /**
   * Returns a vector created by column-wise ({@code dim} = 0) or rows-wise ({@code dim} = 1) concatination of the
   * provided matrix A.
   * 
   * @param <T> TODO
   * @param return_type The type of vector to be returned
   * @param A The matrix
   * @param dim TODO
   * 
   * @return TODO
   * 
   * @throws IllegalArgumentException The specified dimension ({@code dim}) must either be 0 or 1.
   */
  public static <T extends AbstractVector> T vectorise(final Class<T> return_type, final Mat A, final int dim) throws IllegalArgumentException {
    T result;

    try {
      result = return_type.newInstance();
    } catch(InstantiationException | IllegalAccessException e) {
      throw new RuntimeException("Internal Exception. I would greatly appreciate an e-mail containing some information about this problem.");
    }

    result.set_size(A.n_elem);

    switch (dim) {
      case 0:
        System.arraycopy(A._data, 0, result._data, 0, A.n_elem);
        break;
      case 1:
        int n = 0;
        for (int i = 0; i < A.n_rows; i++) {
          for (int j = 0; j < A.n_cols; j++) {
            result._data[n++] = A._data[i + j * A.n_rows];
          }
        }
        break;
      default:
        throw new IllegalArgumentException("The specified dimension (" + dim + ") must either be 0 or 1.");
    }

    return result;
  }

  /**
   * Performs a Cholesky decomposition of the provided symmetric and positive-definite matrix and returns the result
   * (called {@code R}), such that {@code trans(R).times(R) = X}.
   * 
   * @param X The matrix
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must be square.
   * @throws RuntimeException The decomposition could not be completed. Ensure that the provided matrix is symmetric and
   *           positive-definite.
   */
  public static Mat chol(final Mat X) throws RuntimeException {
    if (!X.is_square()) {
      throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must be square.");
    }

    Mat R = new Mat();
    if (!chol(R, X)) {
      throw new RuntimeException("The decomposition could not be completed. Ensure that the provided matrix is symmetric and positive-definite.");
    }
    return R;
  }

  /**
   * Performs a Cholesky decomposition of the provided symmetric and positive-definite matrix and stores the result in
   * {@code R}, such that {@code trans(R).times(R) = X}.
   * <p>
   * Returns {@code false} if the decomposition failed.
   * 
   * @param R The storage of the output
   * @param X The matrix
   * 
   * @return TODO
   */
  public static boolean chol(final Mat R, final Mat X) {
    if (!X.is_square()) {
      return false;
    }

    R.inPlace(Op.EQUAL, X);

    intW info = new intW(0);

    LAPACK.getInstance().dpotrf("U", X.n_rows, R._data, X.n_rows, info);
    if (info.val != 0) {
      return false;
    }

    int n = 0;
    for (int j = 0; j < R.n_cols; j++) {
      for (int i = 0; i < R.n_rows; i++) {
        if (i > j) {
          R._data[n] = 0;
        }
        n++;
      }
    }

    return true;
  }

  /**
   * Performs an Eigen decomposition of the provided symmetric matrix and returns its eigenvalues.
   * 
   * @param X The matrix
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code X.n_rows}, {@code X.n_cols})-matrix must be square.
   * @throws RuntimeException The decomposition could not be completed. Ensure that the provided matrix is symmetric.
   */
  public static Col eig_sym(final Mat X) throws RuntimeException {
    if (!X.is_square()) {
      throw new RuntimeException("The provided (" + X.n_rows + ", " + X.n_cols + ")-matrix must be square.");
    }

    Col eigval = new Col();
    if (!eig_sym(eigval, X)) {
      throw new RuntimeException("The algorithm failed to converge. Ensure that the provided matrix is symmetric.");
    }
    return eigval;
  }

  /**
   * Performs an Eigen decomposition of the provided symmetric matrix and stores the eigenvalues in {@code eigval}.
   * <p>
   * Returns {@code false} if the decomposition failed.
   * 
   * @param eigval The storage of the eigenvalues
   * @param X The matrix
   * 
   * @return TODO
   */
  public static boolean eig_sym(final AbstractVector eigval, Mat X) {
    if (!X.is_square()) {
      return false;
    }

    double temp[] = Arrays.copyOf(X._data, X.n_elem);
    eigval.set_size(X.n_rows);

    double[] work = new double[Math.max(1, 3 * X.n_rows - 1)];
    intW info = new intW(0);

    LAPACK.getInstance().dsyev("N", "U", X.n_rows, temp, Math.max(1, X.n_rows), eigval._data, work, work.length, info);
    if (info.val != 0) {
      return false;
    }

    return true;
  }

  /**
   * Performs an Eigen decomposition of the provided matrix and stores the eigenvalues in {@code eigval} and the
   * corresponding eigenvectors in {@code eigvec}.
   * <p>
   * Returns {@code false} if the decomposition failed.
   * 
   * @param eigval The storage of the eigenvalues
   * @param eigvec The storage of the eigenvectors
   * @param X The matrix
   * 
   * @return TODO
   */
  public static boolean eig_sym(final AbstractVector eigval, final Mat eigvec, final Mat X) {
    if (!X.is_square()) {
      return false;
    }

    eigvec.inPlace(Op.EQUAL, X);
    eigval.set_size(X.n_rows);

    double[] work = new double[Math.max(1, 3 * X.n_rows - 1)];
    intW info = new intW(0);

    LAPACK.getInstance().dsyev("V", "U", X.n_rows, eigvec._data, Math.max(1, X.n_rows), eigval._data, work, work.length, info);
    if (info.val != 0) {
      return false;
    }

    return true;
  }

  /**
   * Returns the inverse of the provided symmetric and positive-definite matrix.
   * <p>
   * Use {@link #inv_sympd(Mat)} instead, if the provided matrix is known to be symmetric and positive-definite.
   * 
   * @param A The matrix
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code A.n_rows}, {@code A.n_cols})-matrix must be square.
   * @throws RuntimeException The calculation could not be completed. The provided matrix appears to be singular.
   */
  public static Mat inv(final Mat A) throws RuntimeException {
    if (!A.is_square()) {
      throw new RuntimeException("The provided (" + A.n_rows + ", " + A.n_cols + ")-matrix must be square.");
    }

    Mat B = new Mat();
    if (!inv(B, A)) {
      throw new RuntimeException("The calculation could not be completed. The provided matrix appears to be singular.");
    }

    return B;
  }

  /**
   * Calculates the inverse of the provided symmetric and positive-definite matrix and stores the result in {@code B}.
   * <p>
   * Returns {@code false} if the calculation failed.
   * <p>
   * Use {@link #inv_sympd(Mat, Mat)} instead, if the provided matrix is known to be symmetric and positive-definite.
   * 
   * @param B The storage of the inserve
   * @param A The matrix
   * 
   * @return TODO
   */
  public static boolean inv(final Mat B, final Mat A) {
    try {
      B.inPlace(Op.EQUAL, A.i());
    } catch(Exception e) {
      return false;
    }

    return true;
  }

  /**
   * Returns the inverse of the provided symmetric and positive-definite matrix.
   * <p>
   * This method is faster than {@link #inv(Mat)} for matrices that are known to be symmetric and positive-definite.
   * 
   * @param A The matrix
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code A.n_rows}, {@code A.n_cols})-matrix must be square.
   * @throws RuntimeException The inverse could not be computed. Ensure that the provided matrix is symmetric,
   *           positive-definite and not singular.
   */
  public static Mat inv_sympd(final Mat A) throws RuntimeException {
    if (!A.is_square()) {
      throw new RuntimeException("The provided (" + A.n_rows + ", " + A.n_cols + ")-matrix must be square.");
    }

    Mat B = new Mat();

    if (!inv_sympd(B, A)) {
      throw new RuntimeException("The inverse could not be computed. Ensure that the provided matrix is symmetric, positive-definite and not singular.");
    }

    return B;
  }

  /**
   * Calculates the inverse of the provided symmetric and positive-definite matrix and stores the result in {@code B}.
   * <p>
   * Returns {@code false} if the calculation failed.
   * <p>
   * This method is faster than {@link #inv(Mat, Mat)} for matrices that are known to be symmetric and
   * positive-definite.
   * 
   * @param B The storage of the inserve
   * @param A The matrix
   * 
   * @return TODO
   */
  public static boolean inv_sympd(final Mat B, final Mat A) {
    if (!A.is_square()) {
      return false;
    }

    B.inPlace(Op.EQUAL, A);
    intW info = new intW(0);

    LAPACK.getInstance().dpotrf("U", A.n_rows, B._data, Math.max(1, A.n_rows), info);
    if (info.val != 0) {
      return false;
    }

    LAPACK.getInstance().dpotri("U", A.n_rows, B._data, Math.max(1, A.n_rows), info);
    if (info.val != 0) {
      return false;
    }

    int n = 0;
    for (int i = 0; i < B.n_rows; i++) {
      for (int j = 0; j < B.n_cols; j++) {
        if (i > j) {
          B._data[i + j * B.n_rows] = B._data[n];
        }
        n++;
      }
    }

    return true;
  }

  /**
   * Performs a Lower-upper decomposition (with partial pivoting) of the provided matrix and stores the lower-triangular
   * matrix in {@code L}, the upper-triangular matrix in {@code U} and the permutation matrix in {@code P}, such that
   * {@code trans(P).times(L).times(U) = X}.
   * <p>
   * Returns {@code false} if the decomposition failed.
   * 
   * @param L The storage of the lower-triangular matrix
   * @param U The storage of the upper-triangular matrix
   * @param P The storage of the permutation matrix
   * @param X The matrix
   * 
   * @return TODO
   */
  public static boolean lu(final Mat L, final Mat U, final Mat P, final Mat X) {
    U.inPlace(Op.EQUAL, X);

    int[] pivotIndices = new int[Math.min(X.n_rows, X.n_cols)];
    intW info = new intW(0);

    LAPACK.getInstance().dgetrf(X.n_rows, X.n_cols, U._data, X.n_rows, pivotIndices, info);
    if (info.val != 0) {
      return false;
    }

    L.copy_size(U);

    int n = 0;
    for (int j = 0; j < X.n_cols; j++) {
      for (int i = 0; i < X.n_rows; i++) {
        if (i < j) {
          /*
           * strict upper triangle
           */
          L._data[n] = 0;
        } else if (i == j) {
          /*
           * main diagonal
           */
          L._data[n] = 1;
        } else {
          /*
           * strict lower triangle
           */
          L._data[n] = U._data[n];
          U._data[n] = 0;
        }

        n++;
      }
    }

    int[] pivotVector = new int[X.n_rows];
    for (int nn = 0; nn < pivotVector.length; nn++) {
      pivotVector[nn] = nn;
    }

    for (int nn = 0; nn < pivotIndices.length; nn++) {
      int nnn = pivotIndices[nn] - 1;

      if (pivotVector[nn] != pivotVector[nnn]) {
        int temp = pivotVector[nn];
        pivotVector[nn] = pivotVector[nnn];
        pivotVector[nnn] = temp;
      }
    }

    P.zeros(X.n_rows, X.n_rows);
    for (int i = 0; i < P.n_rows; i++) {
      P._data[i + pivotVector[i] * P.n_rows] = 1;
    }

    if (L.n_cols > U.n_rows) {
      L.shed_cols(U.n_rows, L.n_cols - 1);
    }

    if (U.n_rows > L.n_cols) {
      U.shed_rows(L.n_cols, U.n_rows - 1);
    }

    return true;
  }

  /**
   * Performs a Lower-upper decomposition (with partial pivoting) of the provided matrix and stores the permuted
   * lower-triangular matrix in {@code L} and the permuted upper-triangular matrix in {@code U}, such that
   * {@code L.times(U) = X}.
   * <p>
   * Returns {@code false} if the decomposition failed.
   * 
   * @param L The storage of the permuted lower-triangular matrix
   * @param U The storage of the permuted upper-triangular matrix
   * @param X The matrix
   * 
   * @return TODO
   */
  public static boolean lu(final Mat L, final Mat U, final Mat X) {
    U.inPlace(Op.EQUAL, X);

    int[] pivotIndices = new int[Math.min(X.n_rows, X.n_cols)];
    intW info = new intW(0);

    LAPACK.getInstance().dgetrf(X.n_rows, X.n_cols, U._data, X.n_rows, pivotIndices, info);
    if (info.val != 0) {
      return false;
    }

    L.copy_size(U);

    int n = 0;
    for (int j = 0; j < X.n_cols; j++)
    {
      for (int i = 0; i < X.n_rows; i++)
      {
        if (i < j) {
          /*
           * strict upper triangle
           */
          L._data[n] = 0;
        } else if (i == j) {
          /*
           * main diagonal
           */
          L._data[n] = 1;
        } else {
          /*
           * strict lower triangle
           */
          L._data[n] = U._data[n];
          U._data[n] = 0;
        }
        n++;
      }
    }

    int[] pivotVector = new int[X.n_rows];
    for (int nn = 0; nn < pivotVector.length; nn++) {
      pivotVector[nn] = nn;
    }

    for (int nn = 0; nn < pivotIndices.length; nn++) {
      int nnn = pivotIndices[nn] - 1;

      if (pivotVector[nn] != pivotVector[nnn]) {
        int temp = pivotVector[nn];
        pivotVector[nn] = pivotVector[nnn];
        pivotVector[nnn] = temp;

        L.swap_rows(pivotVector[nn], pivotVector[nnn]);
      }
    }

    if (L.n_cols > U.n_rows) {
      L.shed_cols(U.n_rows, L.n_cols - 1);
    }

    if (U.n_rows > L.n_cols) {
      U.shed_rows(L.n_cols, U.n_rows - 1);
    }

    return true;
  }

  /**
   * Returns the Moore-Penrose pseudo-inverse of the provided matrix while treating any singular value below
   * {@code Math.max(A.n_rows, A.n_cols) * norm(A, 2) * Datum.eps} as zero.
   * 
   * @param A The matrix
   * 
   * @return TODO
   * 
   * @throws RuntimeException The calculation could not be completed. The provided matrix appears to be singular.
   */
  public static Mat pinv(final Mat A) throws RuntimeException {
    return pinv(A, Math.max(A.n_rows, A.n_cols) * norm(A, 2) * Datum.eps);
  }

  /**
   * Returns the Moore-Penrose pseudo-inverse of the provided matrix while treating any singular value below the
   * specified tolerance as zero.
   * 
   * @param A The matrix
   * @param tolerance The tolerance
   * 
   * @return TODO
   * 
   * @throws RuntimeException The specified tolerance must be positive.
   * @throws RuntimeException The calculation could not be completed. The provided matrix appears to be singular.
   */
  public static Mat pinv(final Mat A, final double tolerance) throws RuntimeException {
    /*
     * The parameter "tolerance" is validated within pinv(Mat, Mat, double).
     */
    
    Mat B = new Mat();
    if (!pinv(B, A, tolerance)) {
      throw new RuntimeException("The calculation could not be completed. The provided matrix appears to be singular.");
    }

    return B;
  }

  /**
   * Calculates the Moore-Penrose pseudo-inverse of the provided matrix and stores the inverse in {@code B} while
   * treating any singular value below {@code Math.max(A.n_rows, A.n_cols) * norm(A, 2) * Datum.eps} as zero.
   * <p>
   * Returns {@code false} if the calculation failed.
   * 
   * @param B The storage of the inverse
   * @param A The matrix
   * 
   * @return TODO
   */
  public static boolean pinv(final Mat B, final Mat A) {
    return pinv(B, A, Math.max(A.n_rows, A.n_cols) * norm(A, 2) * Datum.eps);
  }

  /**
   * Calculates the Moore-Penrose pseudo-inverse of the provided matrix and stores the inverse in {@code B} while
   * treating any singular value below the specified tolerance as zero.
   * <p>
   * Returns {@code false} if the calculation failed.
   * 
   * @param B The storage of the inverse
   * @param A The matrix
   * @param tolerance The tolerance
   * 
   * @return TODO
   * 
   * @throws RuntimeException The specified tolerance must be positive.
   */
  public static boolean pinv(final Mat B, final Mat A, double tolerance) throws RuntimeException {
    if (tolerance < 0) {
      throw new RuntimeException("The specified tolerance must be positive.");
    }
    
    Mat U = new Mat();
    Col s = new Col();
    Mat V = new Mat();

    boolean status;

    if (A.n_cols > A.n_rows) {
      status = svd_econ(U, s, V, A.t());
    } else {
      status = svd_econ(U, s, V, A);
    }

    if (status == false) {
      return false;
    }

    if (tolerance == 0) {
      tolerance = Math.max(A.n_rows, A.n_cols) * Math.ulp(s.max());
    }

    int count = 0;
    for (int n = 0; n < s.n_elem; n++) {
      if (s._data[n] > tolerance + Datum.eps) {
        count++;
      }
    }

    if (count > 0) {
      /*
       * Contains all singular values that are larger than the specififed tolerance.
       */
      Col singularValues = new Col(count);

      int n = 0;
      for (int nn = 0; nn < s.n_elem; nn++) {
        double value = s._data[nn];
        if (value > tolerance + Datum.eps) {
          singularValues._data[n++] = 1 / value;
        }
      }

      if (V.n_cols > count) {
        V = V.cols(0, count - 1);
      }

      if (U.n_cols > count) {
        U = U.cols(0, count - 1);
      }

      if (A.n_rows >= A.n_cols) {
        B.inPlace(Op.EQUAL, V.times(diagmat(singularValues).times(U.t())));
      } else {
        B.inPlace(Op.EQUAL, U.times(diagmat(singularValues).times(V.t())));
      }
    } else {
      B.zeros(A.n_cols, A.n_rows);
    }

    return true;
  }

  /**
   * Performs a principal component analysis of the provided matrix and returns the principal component coefficients.
   * 
   * @param X The matrix
   * 
   * @return TODO
   * 
   * @throws RuntimeException The calculation could not be completed. The provided matrix appears to be singular.
   */
  public static Mat princomp(Mat X) throws RuntimeException {
    Mat coeff = new Mat();
    if (!princomp(coeff, X)) {
      throw new RuntimeException("The calculation could not be completed. The provided matrix appears to be singular.");
    }

    return coeff;
  }

  /**
   * Performs a principal component analysis of the provided matrix and stores the principal component coefficients in
   * {@code coeff}.
   * <p>
   * Returns {@code false} if the calculation failed.
   * 
   * @param coeff The storage of the principal component coefficients
   * @param X The matrix
   * 
   * @return TODO
   */
  public static boolean princomp(final Mat coeff, final Mat X) {
    if (X.n_rows > 1) {
      Mat temp = new Mat(X);
      temp.each_row(Op.MINUS, mean(Row.class, X));

      Mat U = new Mat();
      Col s = new Col();

      if (!svd(U, s, coeff, temp)) {
        return false;
      }
    } else {
      coeff.eye(X.n_cols, X.n_cols);
    }

    return true;
  }

  /**
   * Performs a principal component analysis of the provided matrix and stores the principal component coefficients in
   * {@code coeff} and the projected data in {@code score}.
   * <p>
   * Returns {@code false} if the calculation failed.
   * 
   * @param coeff The storage of the principal component coefficients
   * @param score The storage of the projected data
   * @param X The matrix
   * 
   * @return TODO
   */
  public static boolean princomp(final Mat coeff, final Mat score, final Mat X) {
    if (X.n_rows > 1) {
      score.inPlace(Op.EQUAL, X);
      score.each_row(Op.MINUS, mean(Row.class, X));

      Mat U = new Mat();
      Col s = new Col();

      if (!svd(U, s, coeff, score)) {
        return false;
      }

      score.inPlace(Op.TIMES, coeff);

      if (X.n_rows <= X.n_cols) {
        new ViewSubCols(score, X.n_rows - 1, X.n_cols - X.n_rows + 1).fill(0);
      }
    } else {
      coeff.eye(X.n_cols, X.n_cols);

      score.copy_size(X);
      score.zeros();
    }

    return true;
  }

  /**
   * Performs a principal component analysis of the provided matrix and stores the principal component coefficients in
   * {@code coeff}, the projected data in {@code score} and the eigenvalues of the covariance matrix of the provided
   * matrix
   * in {@code latent}.
   * <p>
   * Returns {@code false} if the calculation failed.
   * 
   * @param coeff The storage of the principal component coefficients
   * @param score The storage of the projected data
   * @param latent The storage of the eigenvalues of the covariance matrix
   * @param X The matrix
   * 
   * @return TODO
   */
  public static boolean princomp(final Mat coeff, final Mat score, final Col latent, final Mat X) {
    if (X.n_rows > 1) {
      score.inPlace(Op.EQUAL, X);
      score.each_row(Op.MINUS, mean(Row.class, X));

      Mat U = new Mat();
      Col s = new Col();

      if (!svd(U, s, coeff, score)) {
        return false;
      }

      s.inPlace(Op.DIVIDE, Math.sqrt(X.n_rows - 1));

      score.inPlace(Op.TIMES, coeff);

      if (X.n_rows <= X.n_cols) {
        new ViewSubCols(score, X.n_rows - 1, X.n_cols - X.n_rows + 1).fill(0);

        Col s_temp = new Col(X.n_cols, Fill.ZEROS);
        new ViewSubRows(s_temp, 0, X.n_rows - 1).inPlace(Op.EQUAL, new ViewSubRows(s, 0, X.n_rows - 1));
        s.inPlace(Op.EQUAL, s_temp);
      }

      latent.inPlace(Op.EQUAL, square(s));
    } else {
      coeff.eye(X.n_cols, X.n_cols);

      score.copy_size(X);
      score.zeros();

      latent.set_size(X.n_cols);
      latent.zeros();
    }

    return true;
  }

  /**
   * Performs a principal component analysis of the provided matrix and stores the principal component coefficients in
   * {@code coeff}, the projected data in {@code score}, the eigenvalues of the covariance matrix of the provided matrix
   * in {@code latent} and the Hotteling's statistic for each sample in {@code tsquared}.
   * <p>
   * Returns {@code false} if the calculation failed.
   * 
   * @param coeff The storage of the principal component coefficients
   * @param score The storage of the projected data
   * @param latent The storage of the eigenvalues of the covariance matrix
   * @param tsquared The storage of the Hotteling's statistic for each sample
   * @param X The matrix
   * 
   * @return TODO
   */
  public static boolean princomp(final Mat coeff, final Mat score, final Col latent, final Col tsquared, final Mat X) {
    if (X.n_rows > 1) {
      score.inPlace(Op.EQUAL, X);
      score.each_row(Op.MINUS, mean(Row.class, X));

      Mat U = new Mat();
      Col s = new Col();

      if (!svd(U, s, coeff, score)) {
        return false;
      }

      s.inPlace(Op.DIVIDE, Math.sqrt(X.n_rows - 1));

      score.inPlace(Op.TIMES, coeff);

      Mat S;
      if (X.n_rows <= X.n_cols) {
        new ViewSubCols(score, X.n_rows - 1, X.n_cols - X.n_rows + 1).fill(0);

        Col s_temp = new Col(X.n_cols, Fill.ZEROS);
        new ViewSubRows(s_temp, 0, X.n_rows - 1).inPlace(Op.EQUAL, new ViewSubRows(s, 0, X.n_rows - 1));
        s.inPlace(Op.EQUAL, s_temp);

        new ViewSubRows(s_temp, 0, X.n_rows - 1).inPlace(Op.EQUAL, reciprocal(s_temp.rows(0, X.n_rows - 2)));

        S = score.times(diagmat(s_temp));
      } else {
        S = score.times(diagmat(reciprocal(s)));
      }

      tsquared.inPlace(Op.EQUAL, sum(Col.class, square(S), 1));
      latent.inPlace(Op.EQUAL, square(s));
    } else {
      coeff.eye(X.n_cols, X.n_cols);

      score.copy_size(X);
      score.zeros();

      latent.set_size(X.n_cols);
      latent.zeros();

      tsquared.set_size(X.n_rows);
      tsquared.zeros();
    }

    return true;
  }

  /**
   * Performs a QR decomposition of the provided matrix and stores the orthogonal matrix in {@code Q}, the right
   * -triangular matrix in {@code R}, such that {@code Q.times(U) = X}.
   * <p>
   * Returns {@code false} if the decomposition failed.
   * 
   * @param Q The storage of the orthogonal matrix
   * @param R The storage of the right-triangular matrix
   * @param X The matrix
   * 
   * @return TODO
   */
  public static boolean qr(final Mat Q, final Mat R, final Mat X) {
    if (X.empty()) {
      return false;
    }

    R.inPlace(Op.EQUAL, X);

    double[] tau = new double[Math.min(X.n_rows, X.n_cols)];
    double[] work = new double[Math.max(1, Math.max(X.n_rows, X.n_cols))];
    intW info = new intW(0);

    LAPACK.getInstance().dgeqrf(X.n_rows, X.n_cols, R._data, X.n_rows, tau, work, work.length, info);

    if (info.val != 0) {
      return false;
    }

    Q.set_size(X.n_rows, X.n_rows);
    System.arraycopy(R._data, 0, Q._data, 0, Math.min(Q.n_elem, R.n_elem));

    for (int j = 0; j < R.n_cols; j++) {
      for (int i = (j + 1); i < R.n_rows; i++) {
        R._data[i + j * R.n_rows] = 0;
      }
    }

    LAPACK.getInstance().dorgqr(X.n_rows, X.n_rows, tau.length, Q._data, X.n_rows, tau, work, work.length, info);

    return (info.val == 0);
  }

  /**
   * Performs an economical (memory friendly) QR decomposition of the provided matrix and stores the orthogonal matrix
   * in {@code Q}, the right -triangular matrix in {@code R}, such that {@code Q.times(U) = X}.
   * <p>
   * {@code R} is calculated only up to the first {@code X.n_rows} rows and {@code Q} up to the first {@code X.n_cols}
   * columns.
   * <p>
   * Returns {@code false} if the decomposition failed.
   * 
   * @param Q The storage of the orthogonal matrix
   * @param R The storage of the right-triangular matrix
   * @param X The matrix
   * 
   * @return TODO
   */
  public static boolean qr_econ(final Mat Q, final Mat R, final Mat X) {
    if (X.is_empty()) {
      return false;
    }

    if (X.n_rows <= X.n_cols) {
      return qr(Q, R, X);
    }

    Q.inPlace(Op.EQUAL, X);

    double[] tau = new double[Math.min(X.n_rows, X.n_cols)];
    double[] work = new double[Math.max(1, Math.max(X.n_rows, X.n_cols))];
    intW info = new intW(0);

    LAPACK.getInstance().dgeqrf(X.n_rows, X.n_cols, Q._data, X.n_rows, tau, work, work.length, info);

    if (info.val != 0) {
      return false;
    }

    R.set_size(X.n_cols, X.n_cols);

    for (int j = 0; j < R.n_cols; j++) {
      for (int i = 0; i <= j; ++i) {
        R._data[i + j * R.n_rows] = Q._data[i + j * Q.n_rows];
      }

      for (int i = (j + 1); i < R.n_cols; i++) {
        R._data[i + j * R.n_rows] = 0;
      }
    }

    LAPACK.getInstance().dorgqr(X.n_rows, X.n_cols, tau.length, Q._data, X.n_rows, tau, work, work.length, info);

    return (info.val == 0);
  }

  /**
   * Returns the solution of a system of linear equations {@code A.times(X) = B} with unknown {@code X}.
   * 
   * @param A The matrix A
   * @param B The matrix B
   * 
   * @return TODO
   * 
   * @throws RuntimeException Both matrices must have the same number of rows ({@code A.n_rows} and {@code B.n_rows}).
   * @throws RuntimeException No solution was found.
   */
  public static Mat solve(final Mat A, final Mat B) throws RuntimeException {
    if (A.n_rows != B.n_rows) {
      throw new RuntimeException("Both matrices must have the same number of rows (" + A.n_rows + " and " + B.n_rows + ").");
    }

    Mat X = new Mat();
    if (!solve(X, A, B)) {
      throw new RuntimeException("No solution was found.");
    }

    return X;
  }

  /**
   * Solves a system of linear equations {@code A.times(X) = B} with unknown {@code X} and stores the solution in
   * {@code X}.
   * <p>
   * Returns {@code false} if the calculation failed.
   * 
   * @param X The storage of the solution
   * @param A The matrix A
   * @param B The matrix B
   * 
   * @return TODO
   */
  public static boolean solve(final Mat X, final Mat A, final Mat B) {
    if (A.n_rows != B.n_rows) {
      throw new RuntimeException("Both matrices must have the same number of rows (" + A.n_rows + " and " + B.n_rows + ").");
    }

    if (A.empty() || B.empty()) {
      return false;
    }

    X.set_size(A.n_cols, B.n_cols);
    intW info = new intW(0);

    if (A.n_rows == A.n_cols) {
      double[] tempA = Arrays.copyOf(A._data, A.n_elem);
      System.arraycopy(B._data, 0, X._data, 0, B.n_elem);
      int[] pivotIndices = new int[A.n_rows + 2];

      LAPACK.getInstance().dgesv(A.n_rows, B.n_cols, tempA, A.n_rows, pivotIndices, X._data, A.n_rows, info);
    } else if (A.n_rows > A.n_cols) {
      double[] tempA = Arrays.copyOf(A._data, A.n_elem);
      double[] tempB = Arrays.copyOf(B._data, B.n_elem);
      double[] work = new double[3 * Math.max(1, A.n_cols + Math.max(A.n_cols, B.n_cols))];

      LAPACK.getInstance().dgels("N", A.n_rows, A.n_cols, B.n_cols, tempA, A.n_rows, tempB, A.n_rows, work, work.length, info);
      System.arraycopy(tempB, 0, X._data, 0, X.n_elem);
    } else {
      double[] tempA = Arrays.copyOf(A._data, A.n_elem);
      double[] tempB = new double[X.n_elem];
      for (int j = 0; j < B.n_cols; j++) {
        System.arraycopy(B._data, j * B.n_rows, tempB, j * X.n_rows, B.n_rows);
      }
      double[] work = new double[3 * Math.max(1, A.n_cols + Math.max(A.n_cols, B.n_cols))];

      LAPACK.getInstance().dgels("N", A.n_rows, A.n_cols, B.n_cols, tempA, A.n_rows, tempB, A.n_cols, work, work.length, info);
      System.arraycopy(tempB, 0, X._data, 0, X.n_elem);
    }

    return (info.val == 0);
  }

  /**
   * Performs a singular value decomposition of the provided matrix and returns the singular values in descending order.
   * 
   * @param X The matrix
   * 
   * @return TODO
   * 
   * @throws RuntimeException The decomposition could not be completed. The provided matrix appears to be singular.
   */
  public static Col svd(final Mat X) throws RuntimeException {
    Col s = new Col();
    if (!svd(s, X)) {
      throw new RuntimeException("The decomposition could not be completed. The provided matrix appears to be singular.");
    }
    return s;
  }

  /**
   * Performs a singular value decomposition of the provided matrix and stores the singular values in descending order
   * in {@code s}.
   * <p>
   * Returns {@code false} if the decomposition failed.
   * 
   * @param s The storage of the singular values
   * @param X The matrix
   * 
   * @return TODO
   */
  public static boolean svd(final Col s, final Mat X) {
    if (X.is_empty()) {
      return false;
    }

    double[] U = new double[1];
    s.set_size(Math.min(X.n_rows, X.n_cols));
    double[] V = new double[X.n_cols];

    double[] temp = Arrays.copyOf(X._data, X.n_elem);
    double[] work = new double[Math.max(1, Math.max(3 * Math.min(X.n_rows, X.n_cols) + Math.max(X.n_rows, X.n_cols), 5 * Math.min(X.n_rows, X.n_cols)))];
    intW info = new intW(0);

    LAPACK.getInstance().dgesvd("N", "N", X.n_rows, X.n_cols, temp, X.n_rows, s._data, U, 1, V, 1, work, work.length, info);

    return (info.val == 0);
  }

  /**
   * Performs a singular value decomposition of the provided matrix and stores the singular values in descending order
   * in {@code s}, such that {@code U.times(diagmat(s)).times(transpose(V)) = X}.
   * <p>
   * Returns {@code false} if the decomposition failed.
   * 
   * @param U The storage of U
   * @param s The storage of the singular values
   * @param V The storage of V
   * @param X The matrix
   * 
   * @return TODO
   */
  public static boolean svd(final Mat U, final Col s, final Mat V, final Mat X) {
    if (X.is_empty()) {
      return false;
    }

    U.set_size(X.n_rows, X.n_rows);
    s.set_size(Math.min(X.n_rows, X.n_cols));
    V.set_size(X.n_cols, X.n_cols);

    double[] temp = Arrays.copyOf(X._data, X.n_elem);
    double[] work = new double[Math.max(1, Math.max(3 * Math.min(X.n_rows, X.n_cols) + Math.max(X.n_rows, X.n_cols), 5 * Math.min(X.n_rows, X.n_cols)))];
    intW info = new intW(0);

    LAPACK.getInstance().dgesvd("A", "A", X.n_rows, X.n_cols, temp, X.n_rows, s._data, U._data, U.n_rows, V._data, V.n_rows, work, work.length, info);

    inplace_trans(V);

    return (info.val == 0);
  }

  /**
   * Performs an economical (memory friendly) singular value decomposition of the provided matrix and stores the
   * singular values in descending order in {@code s} and calculated both the left-singular and right-singular values.
   * <p>
   * Returns {@code false} if the decomposition failed.
   * 
   * @param U The storage of the left-singular vectors U
   * @param s The storage of the singular values
   * @param V The storage of the right-singular vectors V
   * @param X The matrix
   * 
   * @return TODO
   */
  public static boolean svd_econ(final Mat U, final Col s, final Mat V, final Mat X) {
    return svd_econ(U, s, V, X, "both");
  }

  /**
   * Performs an economical (memory friendly) singular value decomposition of the provided matrix and stores the
   * singular values in descending order in {@code s}.
   * <ul>
   * <li>For {@code side} = "left", only the left-singular vectors are calculated.
   * <li>For {@code side} = "right", only the right-singular vectors are calculated.
   * <li>For {@code side} = "both", both the left-singular and right-singular vectors are calculated.
   * </ul>
   * <p>
   * Returns {@code false} if the decomposition failed.
   * 
   * @param U The storage of the left-singular vectors U
   * @param s The storage of the singular values
   * @param V The storage of the right-singular vectors V
   * @param X The matrix
   * @param side The singular vectors to be calculated
   * 
   * @return TODO
   * 
   * @throws IllegalArgumentException The specified side ({@code side}) must be one of 'left', 'right' or 'both'.
   */
  public static boolean svd_econ(final Mat U, final Col s, final Mat V, final Mat X, final String side) throws IllegalArgumentException {
    if (X.is_empty()) {
      return false;
    }

    s.set_size(Math.min(X.n_rows, X.n_cols));

    double[] temp = Arrays.copyOf(X._data, X.n_elem);
    double[] work = new double[3 * Math.max(1, Math.max(3 * Math.min(X.n_rows, X.n_cols) + Math.max(X.n_rows, X.n_cols), 5 * Math.min(X.n_rows, X.n_cols)))];
    intW info = new intW(0);

    switch (side) {
      case "left":
        U.set_size(X.n_rows, s.n_elem);
        V.reset();

        LAPACK.getInstance().dgesvd("S", "N", X.n_rows, X.n_cols, temp, X.n_rows, s._data, U._data, U.n_rows, V._data, 1, work, work.length, info);
        break;
      case "right":
        U.reset();
        V.set_size(s.n_elem, X.n_cols);

        LAPACK.getInstance().dgesvd("N", "S", X.n_rows, X.n_cols, temp, X.n_rows, s._data, U._data, 1, V._data, V.n_rows, work, work.length, info);
        break;
      case "both":
        U.set_size(X.n_rows, s.n_elem);
        V.set_size(s.n_elem, X.n_cols);

        LAPACK.getInstance().dgesvd("S", "S", X.n_rows, X.n_cols, temp, X.n_rows, s._data, U._data, U.n_rows, V._data, V.n_rows, work, work.length, info);
        break;
      default:
        throw new IllegalArgumentException("The specified side (" + side + ") must be one of 'left', 'right' or 'both'.");
    }

    inplace_trans(V);

    return (info.val == 0);
  }

  /**
   * Returns the solution of the Sylvester equations {@code A.times(X) + X.times(B) = C} with unknown {@code X}.
   * 
   * @param A The matrix A
   * @param B The matrix B
   * @param C The matrix C
   * 
   * @return TODO
   * 
   * @throws RuntimeException The provided ({@code A.n_rows}, {@code A.n_cols})-matrix A must be square.
   * @throws RuntimeException The provided ({@code B.n_rows}, {@code B.n_cols})-matrix B must be square.
   * @throws RuntimeException The matrix C must have the same number of rows ({@code C.n_rows}) as the matrix A (
   *           {@code A.n_rows}).
   * @throws RuntimeException The matrix C must have the same number of columns ({@code C.n_cols}) as the matrix B (
   *           {@code A.n_cols}).
   * @throws RuntimeException No solution was found.
   */
  public static Mat syl(final Mat A, final Mat B, final Mat C) throws RuntimeException {
    if (!A.is_square()) {
      throw new RuntimeException("The provided (" + A.n_rows + ", " + A.n_cols + ")-matrix A must be square.");
    }

    if (!B.is_square()) {
      throw new RuntimeException("The provided (" + B.n_rows + ", " + B.n_cols + ")-matrix B must be square.");
    }

    if (C.n_rows != A.n_rows) {
      throw new RuntimeException("The matrix C must have the same number of rows (" + C.n_rows + ") as the matrix A (" + A.n_rows + ").");
    }

    if (C.n_cols != B.n_cols) {
      throw new RuntimeException("The matrix C must have the same number of columns (" + C.n_cols + ") as the matrix B (" + A.n_cols + ").");
    }

    Mat X = new Mat();
    if (!syl(X, A, B, C)) {
      throw new RuntimeException("No solution was found.");
    }
    return X;
  }

  /**
   * Solves the Sylvester equations {@code A.times(X) + X.times(B) = C} with unknown {@code X} and stores the solution
   * in {@code X}.
   * <p>
   * Returns {@code false} if the calculation failed.
   * 
   * @param X The storage of the solution
   * @param A The matrix A
   * @param B The matrix B
   * @param C The matrix C
   * 
   * @return TODO
   */
  public static boolean syl(final Mat X, final Mat A, final Mat B, final Mat C) {
    if (A.is_empty() || B.is_empty() || C.is_empty()) {
      return false;
    }

    if (!A.is_square() || !B.is_square()) {
      return false;
    }

    if (C.n_rows != A.n_rows || C.n_cols != B.n_cols) {
      return false;
    }

    Mat Z1 = new Mat();
    Mat Z2 = new Mat();
    Mat T1 = new Mat();
    Mat T2 = new Mat();

    boolean statusA = schur(Z1, T1, A);
    boolean statusB = schur(Z2, T2, B);

    if (!statusA || !statusB) {
      return false;
    }

    Mat Y = trans(Z1).times(C).times(Z2);

    doubleW scale = new doubleW(0);
    intW info = new intW(0);

    LAPACK.getInstance().dtrsyl("N", "N", 1, T1.n_rows, T2.n_cols, T1._data, T1.n_rows, T2._data, T2.n_cols, Y._data, T1.n_rows, scale, info);

    Y.inPlace(Op.DIVIDE, -scale.val);
    X.inPlace(Op.EQUAL, Z1.times(Y).times(trans(Z2)));

    return (info.val >= 0);
  }

  /**
   * Returns {@code true} if all values of the provided matrix are neither NaN nor +/-infinity.
   * 
   * @param X The matrix
   * 
   * @return TODO
   */
  public static boolean is_finite(final AbstractMat X) {
    return X.is_finite();
  }

  /**
   * Returns {@code true} if the value is neither NaN nor +/-infinity.
   * 
   * @param X The value
   * 
   * @return TODO
   */
  public static boolean is_finite(final double X) {
    return (!Double.isInfinite(X) && !Double.isNaN(X));
  }

  /**
   * Returns a copy of the provided vector with element-wise negated elements.
   * 
   * @param X The vector
   * 
   * @return TODO
   */
  public static Col negate(final Col X) {
    Col result = new Col(X.n_elem);
    for (int n = 0; n < X.n_elem; n++) {
      result._data[n] = -X._data[n];
    }
    return result;
  }

  /**
   * Returns a copy of the provided vector with element-wise negated elements.
   * 
   * @param X The vector
   * 
   * @return TODO
   */
  public static Row negate(final Row X) {
    Row result = new Row(X.n_elem);
    for (int n = 0; n < X.n_elem; n++) {
      result._data[n] = -X._data[n];
    }
    return result;
  }

  /**
   * Returns a copy of the provided matrix with element-wise negated elements.
   * 
   * @param X The matrix
   * 
   * @return TODO
   */
  public static Mat negate(final Mat X) {
    Mat result = new Mat(X.n_rows, X.n_cols);
    for (int n = 0; n < X.n_elem; n++) {
      result._data[n] = -X._data[n];
    }
    return result;
  }

  /**
   * Returns a copy of the provided vector with element-wise inversed elements.
   * 
   * @param X The vector
   * 
   * @return TODO
   */
  public static Col reciprocal(final Col X) {
    Col result = new Col(X.n_elem);
    for (int n = 0; n < X.n_elem; n++) {
      result._data[n] = 1 / X._data[n];
    }
    return result;
  }

  /**
   * Returns a copy of the provided vector with element-wise inversed elements.
   * 
   * @param X The vector
   * 
   * @return TODO
   */
  public static Row reciprocal(final Row X) {
    Row result = new Row(X.n_elem);
    for (int n = 0; n < X.n_elem; n++) {
      result._data[n] = 1 / X._data[n];
    }
    return result;
  }

  /**
   * Returns a copy of the provided matrix with element-wise inversed elements.
   * 
   * @param X The matrix
   * 
   * @return TODO
   */
  public static Mat reciprocal(final Mat X) {
    Mat result = new Mat(X.n_rows, X.n_cols);
    for (int n = 0; n < X.n_elem; n++) {
      result._data[n] = 1 / X._data[n];
    }
    return result;
  }

  /**
   * TODO
   * 
   * @param result TODO
   * @param X TODO
   */
  protected static void revert(final double[] result, final double[] X) {
    for (int n = 0; n < X.length / 2; n++) {
      double temp = X[n];
      result[n] = X[X.length - (n + 1)];
      result[X.length - (n + 1)] = temp;
    }
  }

  /**
   * TODO
   * 
   * @param Z TODO
   * @param T TODO
   * @param A TODO
   * 
   * @return TODO
   */
  protected static boolean schur(final Mat Z, final Mat T, final Mat A) {
    Z.set_size(A.n_rows, A.n_rows);
    T.inPlace(Op.EQUAL, A);

    double[] workReal = new double[A.n_rows];
    double[] workImaginary = new double[A.n_rows];
    double[] work = new double[3 * Math.max(1, 3 * A.n_rows)];
    boolean[] bwork = new boolean[A.n_rows];
    intW sdim = new intW(0);
    intW info = new intW(0);

    LAPACK.getInstance().dgees("V", "N", new Object(), A.n_rows, T._data, A.n_rows, sdim, workReal, workImaginary, Z._data, A.n_rows, work, work.length, bwork, info);

    return (info.val == 0);
  }

}
