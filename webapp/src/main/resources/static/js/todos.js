(function () {
    "use strict";
    angular.module("todos", [])
        .controller("todos", function ($scope) {
            $scope.todos = [{id: 1, text: "Remember this!", done: false}, {id: 2, text: "Fuhgettaboutit!", done: true}];
        });
}());