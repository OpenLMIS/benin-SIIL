/*
 * Copyright © 2013 VillageReach.  All Rights Reserved.  This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

function SaveRegimenTemplateController($scope, program, programRegimens, regimenTemplate, regimenCategories, messageService, Regimens, $location) {

  $scope.program = program;
  $scope.regimens = programRegimens;
  $scope.regimenTemplate = regimenTemplate;
  $scope.regimenCategories = regimenCategories;
  $scope.selectProgramUrl = "/public/pages/admin/regimen-template/index.html#/select-program";
  $scope.regimensByCategory = [];
  $scope.$parent.message = "";
  $scope.newRegimen = {active: true};
  $scope.showReportingFields = false;
  $scope.regimensError = false;
  $scope.reportingFieldsError = false;

  function addRegimenByCategory(regimen) {
    if (!$scope.regimensByCategory[regimen.category.id])
      $scope.regimensByCategory[regimen.category.id] = [];
    $scope.regimensByCategory[regimen.category.id].push(regimen);
    $scope.error = "";
  }

  function filterRegimensByCategory(regimens) {
    $scope.regimensByCategory = _.groupBy(regimens, function (regimen) {
      return regimen.category.id;
    });
  }

  filterRegimensByCategory($scope.regimens);

  $scope.addNewRegimen = function () {
    if (invalidRegimen($scope.newRegimen)) {
      $scope.inputClass = true;
      $scope.newRegimenError = messageService.get('label.missing.values');
      $scope.regimensError = true;
    } else {
      if (!valid($scope.newRegimen)) {
        return;
      }
      $scope.newRegimen.programId = $scope.program.id;
      $scope.newRegimen.displayOrder = 1;
      $scope.newRegimen.editable = false;
      addRegimenByCategory($scope.newRegimen);
      $scope.newRegimenError = null;
      $scope.newRegimen = null;
      $scope.inputClass = false;
      $scope.newRegimen = {active: true};
    }
  };

  function valid(regimen) {
    var regimens = _.flatten($scope.regimensByCategory);
    if (_.findWhere(regimens, {code: regimen.code})) {
      $scope.newRegimenError = "";
      $scope.error = messageService.get('error.duplicate.regimen.code');
      return false;
    }
    return true;
  }

  $scope.getRegimenValuesByCategory = function () {
    return _.values($scope.regimensByCategory);
  }

  $scope.highlightRequired = function (value) {
    if ($scope.inputClass && isUndefined(value)) {
      return "required-error";
    }
    return null;
  };

  $scope.saveRow = function (regimen) {
    if (!valid(regimen)) {
      return;
    }

    if (invalidRegimen(regimen)) {
      regimen.doneRegimenError = true;
      return;
    }

    regimen.doneRegimenError = false;
    regimen.editable = false;
    $scope.error = "";
  };

  function invalidRegimen(regimen) {
    if (isUndefined(regimen.category) || isUndefined(regimen.code) || isUndefined(regimen.name)) {
      $scope.regimensError = true;
      return true;
    }
    return false;
  }

  function checkAllRegimensNotDone() {
    var notDone = false;
    var regimenLists = _.values($scope.regimensByCategory);
    $(regimenLists).each(function (index, regimenList) {
      $(regimenList).each(function (index, loopRegimen) {
        if (loopRegimen.editable) {
          $scope.regimensError = true;
          $scope.error = messageService.get('error.regimens.not.done')
          notDone = true;
          return;
        }
      });
      if (notDone) return;
    });
    return notDone;
  }

  function validReportingFields() {

    var valid = true;
    var countVisible = 0;
    $($scope.regimenTemplate.regimenColumns).each(function (index, regimenColumn) {
      if (isUndefined(regimenColumn.label)) {
        valid = false;
        $scope.reportingFieldsError = true;
        $scope.error = messageService.get('error.regimen.null.label')
        return;
      }
      if (regimenColumn.visible) {
        countVisible++;
      }
    });

    if (valid && countVisible == 0) {
      $scope.reportingFieldsError = true;
      $scope.error = messageService.get('error.regimens.none.selected')
      return;
    }

    return valid;
  }

  $scope.save = function () {

    if (checkAllRegimensNotDone() || !validReportingFields()) {
      return;
    }

    var regimenForm = {};
    var regimenListToSave = [];

    $(_.flatten($scope.regimensByCategory)).each(function (index, regimen) {
      regimen.displayOrder = index + 1;
      regimenListToSave.push(regimen);
    });
    regimenForm.regimens = regimenListToSave;
    regimenForm.regimenTemplate = $scope.regimenTemplate;
    Regimens.save({programId: $scope.program.id}, regimenForm, function () {
      $scope.$parent.message = messageService.get('regimens.saved.successfully');
      $location.path('select-program');
    }, function () {
    });
  };

}

SaveRegimenTemplateController.resolve = {

  program: function ($q, Program, $location, $route, $timeout) {
    var deferred = $q.defer();
    var id = $route.current.params.programId;

    $timeout(function () {
      Program.get({id: id}, function (data) {
        deferred.resolve(data.program);
      }, function () {
        $location.path('select-program');
      });
    }, 100);

    return deferred.promise;
  },

  programRegimens: function ($q, ProgramRegimens, $location, $route, $timeout) {
    var deferred = $q.defer();
    var id = $route.current.params.programId;

    $timeout(function () {
      ProgramRegimens.get({programId: id}, function (data) {
        deferred.resolve(data.regimens);
      }, function () {
        $location.path('select-program');
      });
    }, 100);

    return deferred.promise;
  },

  regimenTemplate: function ($q, RegimenTemplate, $location, $route, $timeout) {
    var deferred = $q.defer();
    var id = $route.current.params.programId;

    $timeout(function () {
      RegimenTemplate.get({programId: id}, function (data) {
        deferred.resolve(data.template);
      }, function () {
        $location.path('select-program');
      });
    }, 100);

    return deferred.promise;
  },

  regimenCategories: function ($q, RegimenCategories, $location, $route, $timeout) {
    var deferred = $q.defer();
    $timeout(function () {
      RegimenCategories.get({}, function (data) {
        deferred.resolve(data.regimen_categories);
      }, function () {
        $location.path('select-program');
      });
    }, 100);

    return deferred.promise;
  }

};