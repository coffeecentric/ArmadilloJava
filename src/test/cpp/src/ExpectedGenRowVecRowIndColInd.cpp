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
using arma::Row;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedGenRowVecRowIndColInd : public Expected {
    public:
      ExpectedGenRowVecRowIndColInd() {
        cout << "Compute ExpectedGenRowVecRowIndColInd(): " << endl;

          vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({
            InputClass::GenRowVec,
            InputClass::RowInd,
            InputClass::ColInd
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
                case 1:
                  _fileSuffix += "," + value.first;
                  _rowInd = *static_cast<int*>(value.second);
                  break;
                case 2:
                  _fileSuffix += "," + value.first;
                  _colInd = *static_cast<int*>(value.second);
                  break;
              }
              ++n;
            }

            cout << "Using input: " << _fileSuffix << endl;

            expectedRowIn_range();
          }

          cout << "done." << endl;
        }

    protected:
      Row<double> _genRowVec;
      int _rowInd;
      int _colInd;

      void expectedRowIn_range() {
        cout << "- Compute expectedRowIn_range() ... ";

        if(_genRowVec.in_range(_rowInd, _colInd)) {
          save<double>("Row.in_range", Row<double>({1}));
        } else {
          save<double>("Row.in_range", Row<double>({0}));
        }

        cout << "done." << endl;
      }
  };
}
