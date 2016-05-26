(function() {
    'use strict';

    angular
        .module('project1App')
        .controller('AuthorController', AuthorController);

    AuthorController.$inject = ['$scope', '$state', 'Author'];

    function AuthorController ($scope, $state, Author) {
        var vm = this;
        vm.authors = [];
        vm.loadAll = function() {
            Author.query(function(result) {
                vm.authors = result;
            });
        };

        vm.loadAll();
        
    }
})();
