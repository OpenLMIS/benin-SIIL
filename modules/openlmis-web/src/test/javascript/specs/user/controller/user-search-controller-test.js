/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
 */

describe("User Search Controller", function () {

  var scope, $httpBackend, ctrl, navigateBackService, location, messageService;
  beforeEach(module('openlmis'));
  var searchTextId = 'searchTextId';

  beforeEach(inject(function ($rootScope, _$httpBackend_, $controller, _navigateBackService_, $location, _messageService_) {
    scope = $rootScope.$new();
    $httpBackend = _$httpBackend_;
    scope.query = "joh";
    navigateBackService = _navigateBackService_;
    navigateBackService.query = '';
    location = $location;
    ctrl = $controller;
    messageService = _messageService_;
    ctrl('UserSearchController', {$scope: scope, messageService: messageService});
  }));

  it('should get all users in a page depending on search criteria', function () {
    var user = {"id": 1, "firstName": "john", "lastName": "Doe", "email": "john_doe@gmail.com"};
    var pagination = {"page": 1, "pageSize": 10, "numberOfPages": 10, "totalRecords": 100};
    var response = {"userList": [user], "pagination": pagination};
    scope.query = "j";
    $httpBackend.when('GET', '/users.json?page=1&searchParam=' + scope.query).respond(response);

    scope.loadUsers(1);
    $httpBackend.flush();

    expect(scope.userList).toEqual([user]);
    expect(scope.pagination).toEqual(pagination);
    expect(scope.currentPage).toEqual(1);
    expect(scope.showResults).toEqual(true);
    expect(scope.totalItems).toEqual(100);
  });

  it('should return if query is null', function () {
    scope.query = "";
    var httpBackendSpy = spyOn($httpBackend, 'expectGET');

    scope.loadUsers(1);

    expect(httpBackendSpy).not.toHaveBeenCalled();
  });

  it('should clear search param and result list', function () {
    var userList = [{"id": 1, "firstName": "john", "lastName": "Doe", "email": "john_doe@gmail.com"}];

    scope.query = "j";
    scope.totalItems = 100;
    scope.userList = userList;
    scope.showResults = true;

    scope.clearSearch();

    expect(scope.showResults).toEqual(false);
    expect(scope.query).toEqual("");
    expect(scope.totalItems).toEqual(0);
    expect(scope.userList).toEqual([]);
  });

  it('should trigger search on enter key', function () {
    var event = {"keyCode": 13};
    var searchSpy = spyOn(scope, 'loadUsers');

    scope.triggerSearch(event);

    expect(searchSpy).toHaveBeenCalledWith(1);
  });

  it('should get results according to specified page', function () {
    scope.currentPage = 5;
    var searchSpy = spyOn(scope, 'loadUsers');

    scope.$apply(function () {
      scope.currentPage = 6;
    });

    expect(searchSpy).toHaveBeenCalledWith(6);
  });

  it("should save query into shared service on clicking edit link",function(){
    spyOn(navigateBackService, 'setData');
    spyOn(location, 'path');
    scope.query = "john";

    scope.edit(1);

    expect(navigateBackService.setData).toHaveBeenCalledWith({query: "john"});
    expect(location.path).toHaveBeenCalledWith('edit/1');
  });

//  it("should open reset password modal", function () {
//    var user = {id: 1, firstName: "User", active: true};
//    scope.changePassword(user);
//    expect(scope.password1).toEqual("");
//    expect(scope.password2).toEqual("");
//    expect(scope.message).toEqual("");
//    expect(scope.error).toEqual("");
//    expect(scope.changePasswordModal).toEqual(true);
//    expect(scope.user).toEqual(user);
//  });
//
//  it("should not open reset password modal if user is inactive", function () {
//    var user = {id: 1, firstName: "User", active: false};
//    scope.changePassword(user);
//    expect(scope.changePasswordModal).toBeUndefined();
//  });
//
//  it("should reset password modal", function () {
//    scope.resetPasswordModal();
//    expect(scope.changePasswordModal).toEqual(false);
//    expect(scope.user).toEqual(undefined);
//  });
//
//  it("should update user password if password matches and is valid",function () {
//    scope.password1 = scope.password2 = "Abcd1234!";
//    scope.user = {id: 1, firstName: "User"};
//
//    $httpBackend.expect('PUT', '/admin/resetPassword/1.json').respond(200, {success: "password updated"});
//    scope.updatePassword();
//    $httpBackend.flush();
//    expect(scope.message).toEqual("password updated")
//    expect(scope.error).toEqual(undefined)
//  });
//
//  it("should update show error if password is not valid",function () {
//    scope.password1 = scope.password2 = "invalid";
//    scope.user = {id: 1, firstName: "User"};
//    spyOn(messageService, 'get');
//    scope.updatePassword();
//    expect(messageService.get).toHaveBeenCalledWith("error.password.invalid");
//  });
//
//  it("should update show error if passwords do not match" ,function () {
//    scope.password1 = "Abcd1234!";
//  scope.password2 = "invalid";
//    scope.user = {id: 1, firstName: "User"};
//    spyOn(messageService, 'get');
//    scope.updatePassword();
//    expect(messageService.get).toHaveBeenCalledWith("error.password.mismatch");
//  });

});