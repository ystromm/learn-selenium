(function () {
    "use strict";
    angular.module("todos", ['ui.router'])
        .config(function ($stateProvider, $urlRouterProvider) {

            $urlRouterProvider.otherwise('/');

            $stateProvider
                // HOME STATES AND NESTED VIEWS ========================================
                .state('home', {
                    url: '/',
                    templateUrl: 'todos.html'
                })
                // ABOUT PAGE AND MULTIPLE NAMED VIEWS =================================
                .state('about', {
                    // we'll get to this in a bit
                });
        })
        .controller("todos", function ($scope, TodosResource) {
            $scope.todos = [];
            TodosResource.query().$promise.then(function (todos) {
                $scope.todos = todos;
                $scope.error = undefined;
            }, function (error) {
                $scope.error = error;
            });
            $scope.add = function () {
                $scope.todos.push({id: 0, text: $scope.text, done: false});
            };
            $scope.done = function (todo) {
                todo.done = !todo.done;
            };
        }).factory('TodosResource', function ($resource, TODOS_URL) {
        return $resource(TODOS_URL + '/api/todo/:id', {id: '@id'}, {
            update: {method: 'PUT'}
        });
    });
    ;
}());