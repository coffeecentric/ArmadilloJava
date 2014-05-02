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
 *   Sebastian Niemann - Lead developer
 *   Daniel Kiechle - Unit testing
 ******************************************************************************/
#include <Expected.hpp>
using armadilloJava::Expected;

#include <iostream>
using std::cout;
using std::endl;

#include <cmath>
using std::log;
using std::sqrt;
using std::pow;

#include <utility>
using std::pair;

#include <armadillo>
using arma::Mat;
using arma::Row;
using arma::uword;
using arma::abs;
using arma::eps;
using arma::exp;
using arma::exp2;
using arma::exp10;
using arma::trunc_exp;
using arma::log;
using arma::log2;
using arma::log10;
using arma::trunc_log;
using arma::sqrt;
using arma::square;
using arma::floor;
using arma::ceil;
using arma::round;
using arma::sign;
using arma::sin;
using arma::asin;
using arma::sinh;
using arma::asinh;
using arma::cos;
using arma::acos;
using arma::cosh;
using arma::acosh;
using arma::tan;
using arma::atan;
using arma::tanh;
using arma::atanh;
using arma::cumsum;
using arma::hist;
using arma::sort;
using arma::sort_index;
using arma::stable_sort_index;
using arma::trans;
using arma::unique;
using arma::toeplitz;
using arma::circ_toeplitz;
using arma::accu;
using arma::min;
using arma::max;
using arma::prod;
using arma::sum;
using arma::mean;
using arma::median;
using arma::stddev;
using arma::var;
using arma::cor;
using arma::cov;
using arma::diagmat;
using arma::is_finite;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedGenRowVec : public Expected {
    public:
      ExpectedGenRowVec() {
        cout << "Compute ExpectedGenRowVec(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
          InputClass::GenRowVec
        });

        for (vector<pair<string, void*>> input : inputs) {
          _fileSuffix = "";

          int n = 0;
          for (pair<string, void*> value : input) {
            switch (n) {
              case 0:
                _fileSuffix += value.first;
                _genRowVec = *static_cast<Row<double>*>(value.second);
                break;
            }
            ++n;
          }

          cout << "Using input: " << _fileSuffix << endl;

          expectedArmaAbs();
          expectedArmaEps();
          expectedArmaExp();
          expectedArmaExp2();
          expectedArmaExp10();
          expectedArmaTrunc_exp();
          expectedArmaLog();
          expectedArmaLog2();
          expectedArmaLog10();
          expectedArmaTrunc_log();
          expectedArmaSqrt();
          expectedArmaSquare();
          expectedArmaFloor();
          expectedArmaCeil();
          expectedArmaRound();
          expectedArmaSign();
          expectedArmaSin();
          expectedArmaAsin();
          expectedArmaSinh();
          expectedArmaAsinh();
          expectedArmaCos();
          expectedArmaAcos();
          expectedArmaCosh();
          expectedArmaAcosh();
          expectedArmaTan();
          expectedArmaAtan();
          expectedArmaTanh();
          expectedArmaAtanh();
          expectedArmaCumsum();
          expectedArmaHist();
          expectedArmaSort();
          expectedArmaSort_index();
          expectedArmaStable_sort_index();
          expectedArmaTrans();
          expectedArmaUnique();
          expectedArmaNegate();
          expectedArmaReciprocal();
          expectedArmaToeplitz();
          expectedArmaCirc_toeplitz();
          expectedArmaAccu();
          expectedArmaMin();
          expectedArmaMax();
          expectedArmaProd();
          expectedArmaSum();
          expectedArmaMean();
          expectedArmaMedian();
          expectedArmaStddev();
          expectedArmaVar();
          expectedArmaCor();
          expectedArmaCov();
          expectedArmaDiagmat();
          expectedArmaIs_finite();
        }

        cout << "done." << endl;
      }

    protected:
      Row<double> _genRowVec;

      void expectedArmaAbs() {
        cout << "- Compute expectedArmaAbs() ... ";
        save<double>("Arma.abs", abs(_genRowVec));
        cout << "done." << endl;
      }

      void expectedArmaEps() {
        cout << "- Compute expectedArmaAbs() ... ";
        save<double>("Arma.eps", eps(_genRowVec));
        cout << "done." << endl;
      }

      void expectedArmaExp() {
        cout << "- Compute expectedArmaExp() ... ";
        save<double>("Arma.exp", exp(_genRowVec));
        cout << "done." << endl;
      }

      void expectedArmaExp2() {
        cout << "- Compute expectedArmaExp2() ... ";
        save<double>("Arma.exp2", exp2(_genRowVec));
        cout << "done." << endl;
      }

      void expectedArmaExp10() {
        cout << "- Compute expectedArmaExp10() ... ";
        save<double>("Arma.exp10", exp10(_genRowVec));
        cout << "done." << endl;
      }

      void expectedArmaTrunc_exp() {
        cout << "- Compute expectedArmaTrunc_exp() ... ";
        save<double>("Arma.trunc_exp", trunc_exp(_genRowVec));
        cout << "done." << endl;
      }

      void expectedArmaLog() {
        cout << "- Compute expectedArmaLog() ... ";
        save<double>("Arma.log", log(_genRowVec));
        cout << "done." << endl;
      }

      void expectedArmaLog2() {
        cout << "- Compute expectedArmaLog2() ... ";
        save<double>("Arma.log2", log2(_genRowVec));
        cout << "done." << endl;
      }

      void expectedArmaLog10() {
        cout << "- Compute expectedArmaLog10() ... ";
        save<double>("Arma.log10", log10(_genRowVec));
        cout << "done." << endl;
      }

      void expectedArmaTrunc_log() {
        cout << "- Compute expectedArmaTrunc_log() ... ";
        save<double>("Arma.trunc_log", trunc_log(_genRowVec));
        cout << "done." << endl;
      }

      void expectedArmaSqrt() {
        cout << "- Compute expectedArmaSqrt() ... ";
        save<double>("Arma.sqrt", sqrt(_genRowVec));
        cout << "done." << endl;
      }

      void expectedArmaSquare() {
        cout << "- Compute expectedArmaSquare() ... ";
        save<double>("Arma.square", square(_genRowVec));
        cout << "done." << endl;
      }

      void expectedArmaFloor() {
        cout << "- Compute expectedArmaFloor() ... ";
        save<double>("Arma.floor", floor(_genRowVec));
        cout << "done." << endl;
      }

      void expectedArmaCeil() {
        cout << "- Compute expectedArmaCeil() ... ";
        save<double>("Arma.ceil", ceil(_genRowVec));
        cout << "done." << endl;
      }

      void expectedArmaRound() {
        cout << "- Compute expectedArmaRound() ... ";
        save<double>("Arma.round", round(_genRowVec));
        cout << "done." << endl;
      }

      void expectedArmaSign() {
        cout << "- Compute expectedArmaSign() ... ";
        save<double>("Arma.sign", sign(_genRowVec));
        cout << "done." << endl;
      }

      void expectedArmaSin() {
        cout << "- Compute expectedArmaSin() ... ";
        save<double>("Arma.sin", sin(_genRowVec));
        cout << "done." << endl;
      }

      void expectedArmaAsin() {
        cout << "- Compute expectedArmaAsin() ... ";
        save<double>("Arma.asin", asin(_genRowVec));
        cout << "done." << endl;
      }

      void expectedArmaSinh() {
        cout << "- Compute expectedArmaSinh() ... ";
        save<double>("Arma.sinh", sinh(_genRowVec));
        cout << "done." << endl;
      }

      void expectedArmaAsinh() {
        cout << "- Compute expectedArmaAsinh() ... ";
        save<double>("Arma.asinh", asinh(_genRowVec));
        cout << "done." << endl;
      }

      void expectedArmaCos() {
        cout << "- Compute expectedArmaCos() ... ";
        save<double>("Arma.cos", cos(_genRowVec));
        cout << "done." << endl;
      }

      void expectedArmaAcos() {
        cout << "- Compute expectedArmaAcos() ... ";
        save<double>("Arma.acos", acos(_genRowVec));
        cout << "done." << endl;
      }

      void expectedArmaCosh() {
        cout << "- Compute expectedArmaCosh() ... ";
        save<double>("Arma.cosh", cosh(_genRowVec));
        cout << "done." << endl;
      }

      void expectedArmaAcosh() {
        cout << "- Compute expectedArmaAcosh() ... ";

        /*
         * acosh behaves buggy on some systems, with acosh(inf) = nan instead of inf
         */
        //save<double>("Arma.acosh", acosh(_genRowVec));

        Mat<double> expected = _genRowVec;
        expected.transform([](double value) {
          return log(value + sqrt(pow(value, 2) - 1));
        });
        save<double>("Arma.acosh", expected);

        cout << "done." << endl;
      }

      void expectedArmaTan() {
        cout << "- Compute expectedArmaTan() ... ";
        save<double>("Arma.tan", tan(_genRowVec));
        cout << "done." << endl;
      }

      void expectedArmaAtan() {
        cout << "- Compute expectedArmaAtan() ... ";
        save<double>("Arma.atan", atan(_genRowVec));
        cout << "done." << endl;
      }

      void expectedArmaTanh() {
        cout << "- Compute expectedArmaTanh() ... ";
        save<double>("Arma.tanh", tanh(_genRowVec));
        cout << "done." << endl;
      }

      void expectedArmaAtanh() {
        cout << "- Compute expectedArmaAtanh() ... ";
        save<double>("Arma.atanh", atanh(_genRowVec));
        cout << "done." << endl;
      }

      void expectedArmaCumsum() {
        cout << "- Compute expectedArmaCumsum() ... ";
        save<double>("Arma.cumsum", cumsum(_genRowVec));
        cout << "done." << endl;
      }

      void expectedArmaHist() {
        cout << "- Compute expectedArmaHist() ... ";
        save<uword>("Arma.hist", hist(_genRowVec));
        cout << "done." << endl;
      }

      void expectedArmaSort() {
        if(!_genRowVec.is_finite()) {
          return;
        }

        cout << "- Compute expectedArmaSort() ... ";
        save<double>("Arma.sort", sort(_genRowVec));
        cout << "done." << endl;
      }

      void expectedArmaSort_index() {
        if(!_genRowVec.is_finite()) {
          return;
        }

        cout << "- Compute expectedArmaSort_index() ... ";
        save<uword>("Arma.sort_index", sort_index(_genRowVec));
        cout << "done." << endl;
      }

      void expectedArmaStable_sort_index() {
        if(!_genRowVec.is_finite()) {
          return;
        }

        cout << "- Compute expectedArmaStable_sort_index() ... ";
        save<uword>("Arma.stable_sort_index", stable_sort_index(_genRowVec));
        cout << "done." << endl;
      }

      void expectedArmaTrans() {
        cout << "- Compute expectedArmaTrans() ... ";
        save<double>("Arma.trans", trans(_genRowVec));
        cout << "done." << endl;
      }

      void expectedArmaUnique() {
        cout << "- Compute expectedArmaUnique() ... ";
        save<double>("Arma.unique", unique(_genRowVec));
        cout << "done." << endl;
      }

      void expectedArmaNegate() {
        cout << "- Compute expectedArmaNegate() ... ";
        save<double>("Arma.negate", -_genRowVec);
        cout << "done." << endl;
      }

      void expectedArmaReciprocal() {
        cout << "- Compute expectedArmaReciprocal() ... ";
        save<double>("Arma.reciprocal", 1/_genRowVec);
        cout << "done." << endl;
      }

      void expectedArmaToeplitz() {
        cout << "- Compute expectedArmaToeplitz() ... ";
        save<double>("Arma.toeplitz", toeplitz(_genRowVec));
        cout << "done." << endl;
      }

      void expectedArmaCirc_toeplitz() {
        cout << "- Compute expectedArmaCirc_toeplitz() ... ";
        save<double>("Arma.circ_toeplitz", circ_toeplitz(_genRowVec));
        cout << "done." << endl;
      }

      void expectedArmaAccu() {
        cout << "- Compute expectedArmaAccu() ... ";
        save<double>("Arma.accu", Mat<double>({accu(_genRowVec)}));
        cout << "done." << endl;
      }

      void expectedArmaMin() {
        cout << "- Compute expectedArmaMin() ... ";
        save<double>("Arma.min", Mat<double>({min(_genRowVec)}));
        cout << "done." << endl;
      }

      void expectedArmaMax() {
        cout << "- Compute expectedArmaMax() ... ";
        save<double>("Arma.max", Mat<double>({max(_genRowVec)}));
        cout << "done." << endl;
      }

      void expectedArmaProd() {
        cout << "- Compute expectedArmaProd() ... ";
        save<double>("Arma.prod", Mat<double>({prod(_genRowVec)}));
        cout << "done." << endl;
      }

      void expectedArmaSum() {
        cout << "- Compute expectedArmaSum() ... ";
        save<double>("Arma.sum", Mat<double>({sum(_genRowVec)}));
        cout << "done." << endl;
      }

      void expectedArmaMean() {
        cout << "- Compute expectedArmaMean() ... ";
        save<double>("Arma.mean", Mat<double>({mean(_genRowVec)}));
        cout << "done." << endl;
      }

      void expectedArmaMedian() {
        cout << "- Compute expectedArmaMedian() ... ";
        save<double>("Arma.median", Mat<double>({median(_genRowVec)}));
        cout << "done." << endl;
      }

      void expectedArmaStddev() {
        cout << "- Compute expectedArmaStddev() ... ";
        save<double>("Arma.stddev", Mat<double>({stddev(_genRowVec)}));
        cout << "done." << endl;
      }

      void expectedArmaVar() {
        cout << "- Compute expectedArmaVar() ... ";
        save<double>("Arma.var", Mat<double>({var(_genRowVec)}));
        cout << "done." << endl;
      }

      void expectedArmaCor() {
        cout << "- Compute expectedArmaCor() ... ";
        save<double>("Arma.cor", Mat<double>({cor(_genRowVec)}));
        cout << "done." << endl;
      }

      void expectedArmaCov() {
        cout << "- Compute expectedArmaCov() ... ";
        save<double>("Arma.cov", Mat<double>({cov(_genRowVec)}));
        cout << "done." << endl;
      }

      void expectedArmaDiagmat() {
        cout << "- Compute expectedArmaDiagmat() ... ";
        save<double>("Arma.diagmat", diagmat(_genRowVec));
        cout << "done." << endl;
      }

      void expectedArmaIs_finite() {
        cout << "- Compute expectedArmaIs_finite() ... ";

        if(is_finite(_genRowVec)) {
          save<double>("Arma.is_finite", Mat<double>({1.0}));
        } else {
          save<double>("Arma.is_finite", Mat<double>({0.0}));
        }

        cout << "done." << endl;
      }

  };
}
