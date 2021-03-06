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

#include <utility>
using std::pair;

#include <armadillo>
using arma::Mat;
using arma::Col;
using arma::span;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedInPlaceGenMatColIndRangeGenColVec : public Expected {
    public:
      ExpectedInPlaceGenMatColIndRangeGenColVec() {
        cout << "Compute ExpectedInPlaceGenMatColIndRangeGenColVec(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
            InputClass::GenMat,
            InputClass::ColIndRange,
            InputClass::GenColVec
          });

          for (vector<pair<string, void*>> input : inputs) {
            _fileSuffix = "";

            int n = 0;
            for (pair<string, void*> value : input) {
              switch (n) {
                case 0:
                  _fileSuffix += value.first;
                  _genMat = *static_cast<Mat<double>*>(value.second);
                  break;
                case 1:
                  _fileSuffix += "," + value.first;
                  _colIndRange = *static_cast<span*>(value.second);
                  break;
                case 2:
                  _fileSuffix += "," + value.first;
                  _genColVec = *static_cast<Col<double>*>(value.second);
                  break;
              }
              ++n;
            }

            cout << "Using input: " << _fileSuffix << endl;

            _copyOfGenMat = _genMat;
            _copyOfColIndRange = _colIndRange;
            _copyOfGenColVec = _genColVec;

            expectedMatColsEqual();

            _genMat = _copyOfGenMat;
            _colIndRange = _copyOfColIndRange;
            _genColVec = _copyOfGenColVec;
            expectedMatColsPlus();

            _genMat = _copyOfGenMat;
            _colIndRange = _copyOfColIndRange;
            _genColVec = _copyOfGenColVec;
            expectedMatColsMinus();

            _genMat = _copyOfGenMat;
            _colIndRange = _copyOfColIndRange;
            _genColVec = _copyOfGenColVec;
            expectedMatColsElemTimes();

            _genMat = _copyOfGenMat;
            _colIndRange = _copyOfColIndRange;
            _genColVec = _copyOfGenColVec;
            expectedMatColsElemDivide();
          }

          cout << "done." << endl;
        }

    protected:
      Mat<double> _genMat;
      Mat<double> _copyOfGenMat;

      span _colIndRange;
      span _copyOfColIndRange;

      Col<double> _genColVec;
      Col<double> _copyOfGenColVec;

      void expectedMatColsEqual() {
        if(_colIndRange.whole) {
          return;
        }

        if(_colIndRange.b >= _genMat.n_cols) {
          return;
        }

        if(_genColVec.n_rows != _genMat.n_rows) {
          return;
        }

        if(_colIndRange.b - _colIndRange.a + 1 != 1) {
          return;
        }

        cout << "- Compute expectedMatColsEqual() ... ";

        _genMat.cols(_colIndRange.a, _colIndRange.b) = _genColVec;
        save<double>("Mat.colsEqual", _genMat);

        cout << "done." << endl;
      }

      void expectedMatColsPlus() {
        if(_colIndRange.whole) {
          return;
        }

        if(_colIndRange.b >= _genMat.n_cols) {
          return;
        }

        if(_genColVec.n_rows != _genMat.n_rows) {
          return;
        }

        if(_colIndRange.b - _colIndRange.a + 1 != 1) {
          return;
        }

        cout << "- Compute expectedMatColsPlus() ... ";

        _genMat.cols(_colIndRange.a, _colIndRange.b) += _genColVec;
        save<double>("Mat.colsPlus", _genMat);

        cout << "done." << endl;
      }

      void expectedMatColsMinus() {
        if(_colIndRange.whole) {
          return;
        }

        if(_colIndRange.b >= _genMat.n_cols) {
          return;
        }

        if(_genColVec.n_rows != _genMat.n_rows) {
          return;
        }

        if(_colIndRange.b - _colIndRange.a + 1 != 1) {
          return;
        }

        cout << "- Compute expectedMatColsMinus() ... ";

        _genMat.cols(_colIndRange.a, _colIndRange.b) -= _genColVec;
        save<double>("Mat.colsMinus", _genMat);

        cout << "done." << endl;
      }

      void expectedMatColsElemTimes() {
        if(_colIndRange.whole) {
          return;
        }

        if(_colIndRange.b >= _genMat.n_cols) {
          return;
        }

        if(_genColVec.n_rows != _genMat.n_rows) {
          return;
        }

        if(_colIndRange.b - _colIndRange.a + 1 != 1) {
          return;
        }

        cout << "- Compute expectedMatColsElemTimes() ... ";

        _genMat.cols(_colIndRange.a, _colIndRange.b) %= _genColVec;
        save<double>("Mat.colsElemTimes", _genMat);

        cout << "done." << endl;
      }

      void expectedMatColsElemDivide() {
        if(_colIndRange.whole) {
          return;
        }

        if(_colIndRange.b >= _genMat.n_cols) {
          return;
        }

        if(_genColVec.n_rows != _genMat.n_rows) {
          return;
        }

        if(_colIndRange.b - _colIndRange.a + 1 != 1) {
          return;
        }

        cout << "- Compute expectedMatColsElemDivide() ... ";

        _genMat.cols(_colIndRange.a, _colIndRange.b) /= _genColVec;
        save<double>("Mat.colsElemDivide", _genMat);

        cout << "done." << endl;
      }
  };
}
