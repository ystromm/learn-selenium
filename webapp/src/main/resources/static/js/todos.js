(function () {
    "use strict";
    angular.module('todos', ['ui.router', 'ngResource'])
        .config(function ($stateProvider, $urlRouterProvider) {

            $urlRouterProvider.otherwise('/');

            $stateProvider
                .state('home', {
                    url: '/',
                    templateUrl: 'todos.html'
                });
        })
        .factory('TodosResource', function ($resource, TODOS_URL) {
            return $resource(TODOS_URL + '/api/todo/:id', {}, {
                update: {method: 'PUT'}
            });
        }).controller("todos", function ($scope, TodosResource) {
        $scope.todos = [];
        $scope.error = {message: ""};

        function getTodos() {
            TodosResource.query().$promise.then(function (todos) {
                $scope.todos = todos;
                $scope.error = undefined;
            }, function (error) {
                $scope.error = error;
            });
        }

        $scope.add = function () {
            TodosResource.save({text: $scope.text, done: false}).$promise.then(function () {
                    $scope.text = "";
                    getTodos();
                },
                function (error) {
                    $scope.error = error;
                });
        };

        $scope.done = function (todo) {
            todo.done = !todo.done;
            todo.$save().then(function (result) {
                    getTodos();
                },
                function (error) {
                    $scope.error = error;
                });
        };

        $scope.delete = function (todo) {
            todo.$delete().then(function () {
                getTodos();
            }, function (error) {
                $scope.error = error;
            });
        }

        getTodos();
    });
}());
