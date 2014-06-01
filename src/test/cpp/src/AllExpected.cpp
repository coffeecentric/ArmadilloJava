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
#include <iostream>
// EXIT_SUCCESS
using std::cout;
using std::endl;

#include <fstream>
using std::ofstream;

#define ARMA_USE_CXX11
#include <armadillo>
using arma::set_stream_err1;
using arma::set_stream_err2;
using arma::arma_rng;

#include <ExpectedDatum.cpp>
using armadilloJava::ExpectedDatum;

#include <ExpectedGenColVec.cpp>
using armadilloJava::ExpectedGenColVec;

#include <ExpectedGenColVecElemInd.cpp>
using armadilloJava::ExpectedGenColVecElemInd;

#include <ExpectedGenColVecElemIndRange.cpp>
using armadilloJava::ExpectedGenColVecElemIndRange;

#include <ExpectedGenColVecExp.cpp>
using armadilloJava::ExpectedGenColVecExp;

#include <ExpectedGenColVecGenColVec.cpp>
using armadilloJava::ExpectedGenColVecGenColVec;

#include <ExpectedGenColVecGenColVecNormal.cpp>
using armadilloJava::ExpectedGenColVecGenColVecNormal;

#include <ExpectedGenColVecGenMat.cpp>
using armadilloJava::ExpectedGenColVecGenMat;

#include <ExpectedGenColVecGenRowVec.cpp>
using armadilloJava::ExpectedGenColVecGenRowVec;

#include <ExpectedGenColVecGenRowVecNormal.cpp>
using armadilloJava::ExpectedGenColVecGenRowVecNormal;

#include <ExpectedGenColVecMonColVec.cpp>
using armadilloJava::ExpectedGenColVecMonColVec;

#include <ExpectedGenColVecMonRowVec.cpp>
using armadilloJava::ExpectedGenColVecMonRowVec;

#include <ExpectedGenColVecNormal.cpp>
using armadilloJava::ExpectedGenColVecNormal;

#include <ExpectedGenColVecNumElems.cpp>
using armadilloJava::ExpectedGenColVecNumElems;

#include <ExpectedGenColVecNumRowsNumCols.cpp>
using armadilloJava::ExpectedGenColVecNumRowsNumCols;

#include <ExpectedGenColVecRowIndColInd.cpp>
using armadilloJava::ExpectedGenColVecRowIndColInd;

#include <ExpectedGenColVecRowIndColIndMatSize.cpp>
using armadilloJava::ExpectedGenColVecRowIndColIndMatSize;

#include <ExpectedGenColVecRowIndRangeColIndRange.cpp>
using armadilloJava::ExpectedGenColVecRowIndRangeColIndRange;

#include <ExpectedGenColVecSort.cpp>
using armadilloJava::ExpectedGenColVecSort;

#include <ExpectedGenColVecVecNormInt.cpp>
using armadilloJava::ExpectedGenColVecVecNormInt;

#include <ExpectedGenColVecVecNormString.cpp>
using armadilloJava::ExpectedGenColVecVecNormString;

#include <ExpectedGenDouble.cpp>
using armadilloJava::ExpectedGenDouble;

#include <ExpectedGenDoubleGenDouble.cpp>
using armadilloJava::ExpectedGenDoubleGenDouble;

#include <ExpectedGenDoubleGenDoubleNumElems.cpp>
using armadilloJava::ExpectedGenDoubleGenDoubleNumElems;

#include <ExpectedGenMat.cpp>
using armadilloJava::ExpectedGenMat;

#include <ExpectedGenMatColInd.cpp>
using armadilloJava::ExpectedGenMatColInd;

#include <ExpectedGenMatColIndRange.cpp>
using armadilloJava::ExpectedGenMatColIndRange;

#include <ExpectedGenMatColInds.cpp>
using armadilloJava::ExpectedGenMatColInds;

#include <ExpectedGenMatDim.cpp>
using armadilloJava::ExpectedGenMatDim;

#include <ExpectedGenMatElemInd.cpp>
using armadilloJava::ExpectedGenMatElemInd;

#include <ExpectedGenMatElemIndRange.cpp>
using armadilloJava::ExpectedGenMatElemIndRange;

#include <ExpectedGenMatElemInds.cpp>
using armadilloJava::ExpectedGenMatElemInds;

#include <ExpectedGenMatExp.cpp>
using armadilloJava::ExpectedGenMatExp;

#include <ExpectedGenMatGenColVec.cpp>
using armadilloJava::ExpectedGenMatGenColVec;

#include <ExpectedGenMatGenDouble.cpp>
using armadilloJava::ExpectedGenMatGenDouble;

#include <ExpectedGenMatGenMat.cpp>
using armadilloJava::ExpectedGenMatGenMat;

#include <ExpectedGenMatGenMatNormal.cpp>
using armadilloJava::ExpectedGenMatGenMatNormal;

#include <ExpectedGenMatGenRowVec.cpp>
using armadilloJava::ExpectedGenMatGenRowVec;

#include <ExpectedGenMatMatNormInt.cpp>
using armadilloJava::ExpectedGenMatMatNormInt;

#include <ExpectedGenMatMatNormString.cpp>
using armadilloJava::ExpectedGenMatMatNormString;

#include <ExpectedGenMatMonColVec.cpp>
using armadilloJava::ExpectedGenMatMonColVec;

#include <ExpectedGenMatMonColVecDim.cpp>
using armadilloJava::ExpectedGenMatMonColVecDim;

#include <ExpectedGenMatMonRowVec.cpp>
using armadilloJava::ExpectedGenMatMonRowVec;

#include <ExpectedGenMatMonRowVecDim.cpp>
using armadilloJava::ExpectedGenMatMonRowVecDim;

#include <ExpectedGenMatNormal.cpp>
using armadilloJava::ExpectedGenMatNormal;

#include <ExpectedGenMatNormalDim.cpp>
using armadilloJava::ExpectedGenMatNormalDim;

#include <ExpectedGenMatNumElems.cpp>
using armadilloJava::ExpectedGenMatNumElems;

#include <ExpectedGenMatNumElemsDim.cpp>
using armadilloJava::ExpectedGenMatNumElemsDim;

#include <ExpectedGenMatNumRowsNumCols.cpp>
using armadilloJava::ExpectedGenMatNumRowsNumCols;

#include <ExpectedGenMatRowInd.cpp>
using armadilloJava::ExpectedGenMatRowInd;

#include <ExpectedGenMatRowIndColInd.cpp>
using armadilloJava::ExpectedGenMatRowIndColInd;

#include <ExpectedGenMatRowIndColIndMatSize.cpp>
using armadilloJava::ExpectedGenMatRowIndColIndMatSize;

#include <ExpectedGenMatRowIndColIndRange.cpp>
using armadilloJava::ExpectedGenMatRowIndColIndRange;

#include <ExpectedGenMatRowIndRange.cpp>
using armadilloJava::ExpectedGenMatRowIndRange;

#include <ExpectedGenMatRowIndRangeColInd.cpp>
using armadilloJava::ExpectedGenMatRowIndRangeColInd;

#include <ExpectedGenMatRowIndRangeColIndRange.cpp>
using armadilloJava::ExpectedGenMatRowIndRangeColIndRange;

#include <ExpectedGenMatRowInds.cpp>
using armadilloJava::ExpectedGenMatRowInds;

#include <ExpectedGenMatRowIndsColInds.cpp>
using armadilloJava::ExpectedGenMatRowIndsColInds;

#include <ExpectedGenMatSinValSel.cpp>
using armadilloJava::ExpectedGenMatSinValSel;

#include <ExpectedGenMatSinValTol.cpp>
using armadilloJava::ExpectedGenMatSinValTol;

#include <ExpectedGenMatSort.cpp>
using armadilloJava::ExpectedGenMatSort;

#include <ExpectedGenMatSortDim.cpp>
using armadilloJava::ExpectedGenMatSortDim;

#include <ExpectedGenMatVecNormInt.cpp>
using armadilloJava::ExpectedGenMatVecNormInt;

#include <ExpectedGenRowVec.cpp>
using armadilloJava::ExpectedGenRowVec;

#include <ExpectedGenRowVecExp.cpp>
using armadilloJava::ExpectedGenRowVecExp;

#include <ExpectedGenRowVecGenColVec.cpp>
using armadilloJava::ExpectedGenRowVecGenColVec;

#include <ExpectedGenRowVecGenColVecNormal.cpp>
using armadilloJava::ExpectedGenRowVecGenColVecNormal;

#include <ExpectedGenRowVecGenMat.cpp>
using armadilloJava::ExpectedGenRowVecGenMat;

#include <ExpectedGenRowVecGenRowVec.cpp>
using armadilloJava::ExpectedGenRowVecGenRowVec;

#include <ExpectedGenRowVecGenRowVecNormal.cpp>
using armadilloJava::ExpectedGenRowVecGenRowVecNormal;

#include <ExpectedGenRowVecMonColVec.cpp>
using armadilloJava::ExpectedGenRowVecMonColVec;

#include <ExpectedGenRowVecMonRowVec.cpp>
using armadilloJava::ExpectedGenRowVecMonRowVec;

#include <ExpectedGenRowVecNormal.cpp>
using armadilloJava::ExpectedGenRowVecNormal;

#include <ExpectedGenRowVecNumElems.cpp>
using armadilloJava::ExpectedGenRowVecNumElems;

#include <ExpectedGenRowVecNumRowsNumCols.cpp>
using armadilloJava::ExpectedGenRowVecNumRowsNumCols;

#include <ExpectedGenRowVecSort.cpp>
using armadilloJava::ExpectedGenRowVecSort;

#include <ExpectedGenRowVecVecNormInt.cpp>
using armadilloJava::ExpectedGenRowVecVecNormInt;

#include <ExpectedGenRowVecVecNormString.cpp>
using armadilloJava::ExpectedGenRowVecVecNormString;

#include <ExpectedInPlaceGenColVec.cpp>
using armadilloJava::ExpectedInPlaceGenColVec;

#include <ExpectedInPlaceGenColVecElemInd.cpp>
using armadilloJava::ExpectedInPlaceGenColVecElemInd;

#include <ExpectedInPlaceGenColVecGenDouble.cpp>
using armadilloJava::ExpectedInPlaceGenColVecGenDouble;

#include <ExpectedInPlaceGenMat.cpp>
using armadilloJava::ExpectedInPlaceGenMat;

#include <ExpectedInPlaceGenMatColIndGenColVec.cpp>
using armadilloJava::ExpectedInPlaceGenMatColIndGenColVec;

#include <ExpectedInPlaceGenMatColIndGenDouble.cpp>
using armadilloJava::ExpectedInPlaceGenMatColIndGenDouble;

#include <ExpectedInPlaceGenMatColIndGenMat.cpp>
using armadilloJava::ExpectedInPlaceGenMatColIndGenMat;

#include <ExpectedInPlaceGenMatColIndGenRowVec.cpp>
using armadilloJava::ExpectedInPlaceGenMatColIndGenRowVec;

#include <ExpectedInPlaceGenMatColIndRangeGenColVec.cpp>
using armadilloJava::ExpectedInPlaceGenMatColIndRangeGenColVec;

#include <ExpectedInPlaceGenMatColIndRangeGenDouble.cpp>
using armadilloJava::ExpectedInPlaceGenMatColIndRangeGenDouble;

#include <ExpectedInPlaceGenMatColIndRangeGenMat.cpp>
using armadilloJava::ExpectedInPlaceGenMatColIndRangeGenMat;

#include <ExpectedInPlaceGenMatColIndRangeGenRowVec.cpp>
using armadilloJava::ExpectedInPlaceGenMatColIndRangeGenRowVec;

#include <ExpectedInPlaceGenMatColIndsGenColVec.cpp>
using armadilloJava::ExpectedInPlaceGenMatColIndsGenColVec;

#include <ExpectedInPlaceGenMatColIndsGenDouble.cpp>
using armadilloJava::ExpectedInPlaceGenMatColIndsGenDouble;

#include <ExpectedInPlaceGenMatColIndsGenMat.cpp>
using armadilloJava::ExpectedInPlaceGenMatColIndsGenMat;

#include <ExpectedInPlaceGenMatColIndsGenRowVec.cpp>
using armadilloJava::ExpectedInPlaceGenMatColIndsGenRowVec;

#include <ExpectedInPlaceGenMatElemInd.cpp>
using armadilloJava::ExpectedInPlaceGenMatElemInd;

#include <ExpectedInPlaceGenMatElemIndGenDouble.cpp>
using armadilloJava::ExpectedInPlaceGenMatElemIndGenDouble;

#include <ExpectedInPlaceGenMatElemIndsGenColVec.cpp>
using armadilloJava::ExpectedInPlaceGenMatElemIndsGenColVec;

#include <ExpectedInPlaceGenMatElemIndsGenDouble.cpp>
using armadilloJava::ExpectedInPlaceGenMatElemIndsGenDouble;

#include <ExpectedInPlaceGenMatElemIndsGenMat.cpp>
using armadilloJava::ExpectedInPlaceGenMatElemIndsGenMat;

#include <ExpectedInPlaceGenMatElemIndsGenRowVec.cpp>
using armadilloJava::ExpectedInPlaceGenMatElemIndsGenRowVec;

#include <ExpectedInPlaceGenMatExtColIndGenColVec.cpp>
using armadilloJava::ExpectedInPlaceGenMatExtColIndGenColVec;

#include <ExpectedInPlaceGenMatExtColIndGenMat.cpp>
using armadilloJava::ExpectedInPlaceGenMatExtColIndGenMat;

#include <ExpectedInPlaceGenMatExtColIndGenRowVec.cpp>
using armadilloJava::ExpectedInPlaceGenMatExtColIndGenRowVec;

#include <ExpectedInPlaceGenMatExtColIndNumCols.cpp>
using armadilloJava::ExpectedInPlaceGenMatExtColIndNumCols;

#include <ExpectedInPlaceGenMatExtRowIndGenColVec.cpp>
using armadilloJava::ExpectedInPlaceGenMatExtRowIndGenColVec;

#include <ExpectedInPlaceGenMatExtRowIndGenMat.cpp>
using armadilloJava::ExpectedInPlaceGenMatExtRowIndGenMat;

#include <ExpectedInPlaceGenMatExtRowIndGenRowVec.cpp>
using armadilloJava::ExpectedInPlaceGenMatExtRowIndGenRowVec;

#include <ExpectedInPlaceGenMatExtRowIndNumRows.cpp>
using armadilloJava::ExpectedInPlaceGenMatExtRowIndNumRows;

#include <ExpectedInPlaceGenMatGenColVec.cpp>
using armadilloJava::ExpectedInPlaceGenMatGenColVec;

#include <ExpectedInPlaceGenMatGenDouble.cpp>
using armadilloJava::ExpectedInPlaceGenMatGenDouble;

#include <ExpectedInPlaceGenMatGenMat.cpp>
using armadilloJava::ExpectedInPlaceGenMatGenMat;

#include <ExpectedInPlaceGenMatGenRowVec.cpp>
using armadilloJava::ExpectedInPlaceGenMatGenRowVec;

#include <ExpectedInPlaceGenMatNumRowsNumCols.cpp>
using armadilloJava::ExpectedInPlaceGenMatNumRowsNumCols;

#include <ExpectedInPlaceGenMatRowIndColInd.cpp>
using armadilloJava::ExpectedInPlaceGenMatRowIndColInd;

#include <ExpectedInPlaceGenMatRowIndColIndGenDouble.cpp>
using armadilloJava::ExpectedInPlaceGenMatRowIndColIndGenDouble;

#include <ExpectedInPlaceGenMatRowIndColIndMatSizeGenColVec.cpp>
using armadilloJava::ExpectedInPlaceGenMatRowIndColIndMatSizeGenColVec;

#include <ExpectedInPlaceGenMatRowIndColIndMatSizeGenDouble.cpp>
using armadilloJava::ExpectedInPlaceGenMatRowIndColIndMatSizeGenDouble;

#include <ExpectedInPlaceGenMatRowIndColIndMatSizeGenMat.cpp>
using armadilloJava::ExpectedInPlaceGenMatRowIndColIndMatSizeGenMat;

#include <ExpectedInPlaceGenMatRowIndColIndMatSizeGenRowVec.cpp>
using armadilloJava::ExpectedInPlaceGenMatRowIndColIndMatSizeGenRowVec;

#include <ExpectedInPlaceGenMatRowIndColIndRangeGenColVec.cpp>
using armadilloJava::ExpectedInPlaceGenMatRowIndColIndRangeGenColVec;

#include <ExpectedInPlaceGenMatRowIndColIndRangeGenDouble.cpp>
using armadilloJava::ExpectedInPlaceGenMatRowIndColIndRangeGenDouble;

#include <ExpectedInPlaceGenMatRowIndColIndRangeGenMat.cpp>
using armadilloJava::ExpectedInPlaceGenMatRowIndColIndRangeGenMat;

#include <ExpectedInPlaceGenMatRowIndColIndRangeGenRowVec.cpp>
using armadilloJava::ExpectedInPlaceGenMatRowIndColIndRangeGenRowVec;

#include <ExpectedInPlaceGenMatRowIndGenColVec.cpp>
using armadilloJava::ExpectedInPlaceGenMatRowIndGenColVec;

#include <ExpectedInPlaceGenMatRowIndGenDouble.cpp>
using armadilloJava::ExpectedInPlaceGenMatRowIndGenDouble;

#include <ExpectedInPlaceGenMatRowIndGenMat.cpp>
using armadilloJava::ExpectedInPlaceGenMatRowIndGenMat;

#include <ExpectedInPlaceGenMatRowIndGenRowVec.cpp>
using armadilloJava::ExpectedInPlaceGenMatRowIndGenRowVec;

#include <ExpectedInPlaceGenMatRowIndRangeColIndGenColVec.cpp>
using armadilloJava::ExpectedInPlaceGenMatRowIndRangeColIndGenColVec;

#include <ExpectedInPlaceGenMatRowIndRangeColIndGenDouble.cpp>
using armadilloJava::ExpectedInPlaceGenMatRowIndRangeColIndGenDouble;

#include <ExpectedInPlaceGenMatRowIndRangeColIndGenMat.cpp>
using armadilloJava::ExpectedInPlaceGenMatRowIndRangeColIndGenMat;

#include <ExpectedInPlaceGenMatRowIndRangeColIndGenRowVec.cpp>
using armadilloJava::ExpectedInPlaceGenMatRowIndRangeColIndGenRowVec;

#include <ExpectedInPlaceGenMatRowIndRangeColIndRangeGenColVec.cpp>
using armadilloJava::ExpectedInPlaceGenMatRowIndRangeColIndRangeGenColVec;

#include <ExpectedInPlaceGenMatRowIndRangeColIndRangeGenDouble.cpp>
using armadilloJava::ExpectedInPlaceGenMatRowIndRangeColIndRangeGenDouble;

#include <ExpectedInPlaceGenMatRowIndRangeColIndRangeGenMat.cpp>
using armadilloJava::ExpectedInPlaceGenMatRowIndRangeColIndRangeGenMat;

#include <ExpectedInPlaceGenMatRowIndRangeColIndRangeGenRowVec.cpp>
using armadilloJava::ExpectedInPlaceGenMatRowIndRangeColIndRangeGenRowVec;

#include <ExpectedInPlaceGenMatRowIndRangeGenColVec.cpp>
using armadilloJava::ExpectedInPlaceGenMatRowIndRangeGenColVec;

#include <ExpectedInPlaceGenMatRowIndRangeGenDouble.cpp>
using armadilloJava::ExpectedInPlaceGenMatRowIndRangeGenDouble;

#include <ExpectedInPlaceGenMatRowIndRangeGenMat.cpp>
using armadilloJava::ExpectedInPlaceGenMatRowIndRangeGenMat;

#include <ExpectedInPlaceGenMatRowIndRangeGenRowVec.cpp>
using armadilloJava::ExpectedInPlaceGenMatRowIndRangeGenRowVec;

#include <ExpectedInPlaceGenMatRowIndsColIndsGenColVec.cpp>
using armadilloJava::ExpectedInPlaceGenMatRowIndsColIndsGenColVec;

#include <ExpectedInPlaceGenMatRowIndsColIndsGenDouble.cpp>
using armadilloJava::ExpectedInPlaceGenMatRowIndsColIndsGenDouble;

#include <ExpectedInPlaceGenMatRowIndsColIndsGenMat.cpp>
using armadilloJava::ExpectedInPlaceGenMatRowIndsColIndsGenMat;

#include <ExpectedInPlaceGenMatRowIndsColIndsGenRowVec.cpp>
using armadilloJava::ExpectedInPlaceGenMatRowIndsColIndsGenRowVec;

#include <ExpectedInPlaceGenMatRowIndsGenColVec.cpp>
using armadilloJava::ExpectedInPlaceGenMatRowIndsGenColVec;

#include <ExpectedInPlaceGenMatRowIndsGenDouble.cpp>
using armadilloJava::ExpectedInPlaceGenMatRowIndsGenDouble;

#include <ExpectedInPlaceGenMatRowIndsGenMat.cpp>
using armadilloJava::ExpectedInPlaceGenMatRowIndsGenMat;

#include <ExpectedInPlaceGenMatRowIndsGenRowVec.cpp>
using armadilloJava::ExpectedInPlaceGenMatRowIndsGenRowVec;

#include <ExpectedInPlaceRandomGenMat.cpp>
using armadilloJava::ExpectedInPlaceRandomGenMat;

#include <ExpectedInPlaceRandomGenMatNumRowsNumCols.cpp>
using armadilloJava::ExpectedInPlaceRandomGenMatNumRowsNumCols;

#include <ExpectedInvMat.cpp>
using armadilloJava::ExpectedInvMat;

#include <ExpectedLogicColVec.cpp>
using armadilloJava::ExpectedLogicColVec;

#include <ExpectedLogicColVecNumElems.cpp>
using armadilloJava::ExpectedLogicColVecNumElems;

#include <ExpectedLogicMat.cpp>
using armadilloJava::ExpectedLogicMat;

#include <ExpectedLogicMatDim.cpp>
using armadilloJava::ExpectedLogicMatDim;

#include <ExpectedLogicMatNumElems.cpp>
using armadilloJava::ExpectedLogicMatNumElems;

#include <ExpectedLogicMatNumElemsSearch.cpp>
using armadilloJava::ExpectedLogicMatNumElemsSearch;

#include <ExpectedLogicRowVec.cpp>
using armadilloJava::ExpectedLogicRowVec;

#include <ExpectedLogicRowVecNumElems.cpp>
using armadilloJava::ExpectedLogicRowVecNumElems;

#include <ExpectedNumElems.cpp>
using armadilloJava::ExpectedNumElems;

#include <ExpectedNumRowsNumCols.cpp>
using armadilloJava::ExpectedNumRowsNumCols;

#include <ExpectedOOColVec.cpp>
using armadilloJava::ExpectedOOColVec;

#include <ExpectedOOMat.cpp>
using armadilloJava::ExpectedOOMat;

#include <ExpectedOORowVec.cpp>
using armadilloJava::ExpectedOORowVec;

#include <ExpectedRandomGenColVec.cpp>
using armadilloJava::ExpectedRandomGenColVec;

#include <ExpectedRandomGenMat.cpp>
using armadilloJava::ExpectedRandomGenMat;

#include <ExpectedRandomGenMatDim.cpp>
using armadilloJava::ExpectedRandomGenMatDim;

#include <ExpectedRandomGenRowVec.cpp>
using armadilloJava::ExpectedRandomGenRowVec;

#include <ExpectedRandomNumElems.cpp>
using armadilloJava::ExpectedRandomNumElems;

#include <ExpectedRandomNumElemsDistrParam.cpp>
using armadilloJava::ExpectedRandomNumElemsDistrParam;

#include <ExpectedRandomNumRowsNumCols.cpp>
using armadilloJava::ExpectedRandomNumRowsNumCols;

#include <ExpectedRandomNumRowsNumColsDistrParam.cpp>
using armadilloJava::ExpectedRandomNumRowsNumColsDistrParam;

#include <ExpectedSquMat.cpp>
using armadilloJava::ExpectedSquMat;

#include <ExpectedSquMatSquMatGenMat.cpp>
using armadilloJava::ExpectedSquMatSquMatGenMat;

#include <ExpectedSymMat.cpp>
using armadilloJava::ExpectedSymMat;

#include <ExpectedSymPDMat.cpp>
using armadilloJava::ExpectedSymPDMat;

int main() {
  ofstream logfile("./expected.log");
  cout.rdbuf(logfile.rdbuf());
  set_stream_err1(logfile);
  set_stream_err2(logfile);

  arma_rng::set_seed(123456789);

  /**
   * Add comments to all classes below, before pushing a commit to avoid unnecessary conflicts.
   */

//  ExpectedDatum();
//  ExpectedGenColVec();
//  ExpectedGenColVecElemInd();
//  ExpectedGenColVecElemIndRange();
//  ExpectedGenColVecExp();
//  ExpectedGenColVecGenColVec();
//  ExpectedGenColVecGenColVecNormal();
//  ExpectedGenColVecGenMat();
//  ExpectedGenColVecGenRowVec();
//  ExpectedGenColVecGenRowVecNormal();
//  ExpectedGenColVecMonColVec();
//  ExpectedGenColVecMonRowVec();
//  ExpectedGenColVecNormal();
//  ExpectedGenColVecNumElems();
//  ExpectedGenColVecNumRowsNumCols();
//	ExpectedGenColVecRowIndColInd();
//	ExpectedGenColVecRowIndColIndMatSize();
//	ExpectedGenColVecRowIndRangeColIndRange();
//  ExpectedGenColVecSort();
//  ExpectedGenColVecVecNormInt();
//  ExpectedGenColVecVecNormString();
//  ExpectedGenDouble();
//  ExpectedGenDoubleGenDouble();
//  ExpectedGenDoubleGenDoubleNumElems();
//  ExpectedGenMat();
//  ExpectedGenMatColInd();
//  ExpectedGenMatColIndRange();
//  ExpectedGenMatColInds();
//  ExpectedGenMatDim();
//  ExpectedGenMatElemInd();
//  ExpectedGenMatElemIndRange();
//  ExpectedGenMatElemInds();
//  ExpectedGenMatExp();
//  ExpectedGenMatGenColVec();
//  ExpectedGenMatGenDouble();
//  ExpectedGenMatGenMat();
//  ExpectedGenMatGenMatNormal();
//  ExpectedGenMatGenRowVec();
//  ExpectedGenMatMatNormInt();
//  ExpectedGenMatMatNormString();
//  ExpectedGenMatMonColVec();
//  ExpectedGenMatMonColVecDim();
//  ExpectedGenMatMonRowVec();
//  ExpectedGenMatMonRowVecDim();
//  ExpectedGenMatNormal();
//  ExpectedGenMatNormalDim();
//  ExpectedGenMatNumElems();
//  ExpectedGenMatNumElemsDim();
//  ExpectedGenMatNumRowsNumCols();
//  ExpectedGenMatRowInd();
//  ExpectedGenMatRowIndColInd();
//  ExpectedGenMatRowIndColIndMatSize();
//  ExpectedGenMatRowIndColIndRange();
//  ExpectedGenMatRowIndRange();
//  ExpectedGenMatRowIndRangeColInd();
//  ExpectedGenMatRowIndRangeColIndRange();
//  ExpectedGenMatRowInds();
//  ExpectedGenMatRowIndsColInds();
//  ExpectedGenMatSinValSel();
//  ExpectedGenMatSinValTol();
//  ExpectedGenMatSort();
//  ExpectedGenMatSortDim();
//  ExpectedGenMatVecNormInt();
//  ExpectedGenRowVec();
//  ExpectedGenRowVecExp();
//  ExpectedGenRowVecGenColVec();
//  ExpectedGenRowVecGenColVecNormal();
//  ExpectedGenRowVecGenMat();
//  ExpectedGenRowVecGenRowVec();
//  ExpectedGenRowVecGenRowVecNormal();
//  ExpectedGenRowVecMonColVec();
//  ExpectedGenRowVecMonRowVec();
//  ExpectedGenRowVecNormal();
//  ExpectedGenRowVecNumElems();
//  ExpectedGenRowVecNumRowsNumCols();
//  ExpectedGenRowVecSort();
//  ExpectedGenRowVecVecNormInt();
//  ExpectedGenRowVecVecNormString();
//	ExpectedInPlaceGenColVec();
	ExpectedInPlaceGenColVecElemInd();
//	ExpectedInPlaceGenColVecGenDouble();
//  ExpectedInPlaceGenMat();
//  ExpectedInPlaceGenMatColIndGenColVec();
//  ExpectedInPlaceGenMatColIndGenDouble();
//  ExpectedInPlaceGenMatColIndGenMat();
//  ExpectedInPlaceGenMatColIndGenRowVec();
//  ExpectedInPlaceGenMatColIndRangeGenColVec();
//  ExpectedInPlaceGenMatColIndRangeGenDouble();
//  ExpectedInPlaceGenMatColIndRangeGenMat();
//  ExpectedInPlaceGenMatColIndRangeGenRowVec();
//  ExpectedInPlaceGenMatColIndsGenColVec();
//  ExpectedInPlaceGenMatColIndsGenDouble();
//  ExpectedInPlaceGenMatColIndsGenMat();
//  ExpectedInPlaceGenMatColIndsGenRowVec();
//  ExpectedInPlaceGenMatElemInd();
//  ExpectedInPlaceGenMatElemIndGenDouble();
//  ExpectedInPlaceGenMatElemIndsGenColVec();
//  ExpectedInPlaceGenMatElemIndsGenDouble();
//  ExpectedInPlaceGenMatElemIndsGenMat();
//  ExpectedInPlaceGenMatElemIndsGenRowVec();
//  ExpectedInPlaceGenMatExtColIndGenColVec();
//  ExpectedInPlaceGenMatExtColIndGenMat();
//  ExpectedInPlaceGenMatExtColIndGenRowVec();
//  ExpectedInPlaceGenMatExtColIndNumCols();
//  ExpectedInPlaceGenMatExtRowIndGenColVec();
//  ExpectedInPlaceGenMatExtRowIndGenMat();
//  ExpectedInPlaceGenMatExtRowIndGenRowVec();
//  ExpectedInPlaceGenMatExtRowIndNumRows();
//  ExpectedInPlaceGenMatGenColVec();
//  ExpectedInPlaceGenMatGenDouble();
//  ExpectedInPlaceGenMatGenMat();
//  ExpectedInPlaceGenMatGenRowVec();
//  ExpectedInPlaceGenMatNumRowsNumCols();
//  ExpectedInPlaceGenMatRowIndColInd();
//  ExpectedInPlaceGenMatRowIndColIndGenDouble();
//  ExpectedInPlaceGenMatRowIndColIndMatSizeGenColVec();
//  ExpectedInPlaceGenMatRowIndColIndMatSizeGenDouble();
//  ExpectedInPlaceGenMatRowIndColIndMatSizeGenMat();
//  ExpectedInPlaceGenMatRowIndColIndMatSizeGenRowVec();
//  ExpectedInPlaceGenMatRowIndColIndRangeGenColVec();
//  ExpectedInPlaceGenMatRowIndColIndRangeGenDouble();
//  ExpectedInPlaceGenMatRowIndColIndRangeGenMat();
//  ExpectedInPlaceGenMatRowIndColIndRangeGenRowVec();
//  ExpectedInPlaceGenMatRowIndGenColVec();
//  ExpectedInPlaceGenMatRowIndGenDouble();
//  ExpectedInPlaceGenMatRowIndGenMat();
//  ExpectedInPlaceGenMatRowIndGenRowVec();
//  ExpectedInPlaceGenMatRowIndRangeColIndGenColVec();
//  ExpectedInPlaceGenMatRowIndRangeColIndGenDouble();
//  ExpectedInPlaceGenMatRowIndRangeColIndGenMat();
//  ExpectedInPlaceGenMatRowIndRangeColIndGenRowVec();
//  ExpectedInPlaceGenMatRowIndRangeColIndRangeGenColVec();
//  ExpectedInPlaceGenMatRowIndRangeColIndRangeGenDouble();
//  ExpectedInPlaceGenMatRowIndRangeColIndRangeGenMat();
//  ExpectedInPlaceGenMatRowIndRangeColIndRangeGenRowVec();
//  ExpectedInPlaceGenMatRowIndRangeGenColVec();
//  ExpectedInPlaceGenMatRowIndRangeGenDouble();
//  ExpectedInPlaceGenMatRowIndRangeGenMat();
//  ExpectedInPlaceGenMatRowIndRangeGenRowVec();
//  ExpectedInPlaceGenMatRowIndsColIndsGenColVec();
//  ExpectedInPlaceGenMatRowIndsColIndsGenDouble();
//  ExpectedInPlaceGenMatRowIndsColIndsGenMat();
//  ExpectedInPlaceGenMatRowIndsColIndsGenRowVec();
//  ExpectedInPlaceGenMatRowIndsGenColVec();
//  ExpectedInPlaceGenMatRowIndsGenDouble();
//  ExpectedInPlaceGenMatRowIndsGenMat();
//  ExpectedInPlaceGenMatRowIndsGenRowVec();
//  ExpectedInPlaceRandomGenMat();
//  ExpectedInPlaceRandomGenMatNumRowsNumCols();
//  ExpectedInvMat();
//  ExpectedLogicColVec();
//  ExpectedLogicColVecNumElems();
//  ExpectedLogicMat();
//  ExpectedLogicMatDim();
//  ExpectedLogicMatNumElems();
//  ExpectedLogicMatNumElemsSearch();
//  ExpectedLogicRowVec();
//  ExpectedLogicRowVecNumElems();
//  ExpectedNumElems();
//  ExpectedNumRowsNumCols();
//  ExpectedOOColVec();
//  ExpectedOOMat();
//  ExpectedOORowVec();
//  ExpectedRandomGenColVec();
//  ExpectedRandomGenMat();
//  ExpectedRandomGenMatDim();
//  ExpectedRandomGenRowVec();
//  ExpectedRandomNumElems();
//  ExpectedRandomNumElemsDistrParam();
//  ExpectedRandomNumRowsNumCols();
//  ExpectedRandomNumRowsNumColsDistrParam();
//  ExpectedSquMat();
//  ExpectedSquMatSquMatGenMat();
//  ExpectedSymMat();
//  ExpectedSymPDMat();

  return EXIT_SUCCESS;
}
