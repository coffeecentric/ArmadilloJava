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
using arma::uword;
using arma::raw_ascii;
using arma::find;

#include <InputClass.hpp>
using armadilloJava::InputClass;

#include <Input.hpp>
using armadilloJava::Input;

namespace armadilloJava {
  class ExpectedLogicMatNumElems : public Expected {
    public:
      ExpectedLogicMatNumElems() {
        cout << "Compute ExpectedLogicMatNumElems(): " << endl;

        vector<vector<pair<string, void*>>> inputs = Input::getTestParameters({InputClass::LogicMat, InputClass::NumElems});

        for (vector<pair<string, void*>> input : inputs) {
          _fileSuffix = "";

          int n = 0;
          for (pair<string, void*> value : input) {
            switch (n) {
              case 0:
                _fileSuffix += value.first;
                _logicMat = *static_cast<Mat<double>*>(value.second);
                break;
              case 1:
                _fileSuffix += "," + value.first;
                _numElems = *static_cast<int*>(value.second);
                break;
            }
            ++n;
          }

          cout << "Using input: " << _fileSuffix << endl;

          expectedFind();
        }

        cout << "done." << endl;
      }

    protected:
      Mat<double> _logicMat;
      int _numElems;

      void expectedFind() {
        cout << "- Compute expectedFind() ... ";
        // Unable to convert the result of find(...) to Mat<double>
        Mat<uword> expected = find(_logicMat, _numElems);
        expected.save("../data/expected/find" + _fileSuffix + ".mat", raw_ascii);
        cout << "done." << endl;
      }

  };
}