(function() {
    'use strict';

    angular
        .module('project1App')
        .controller('AuthorDetailController', AuthorDetailController);

    AuthorDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Author'];

    function AuthorDetailController($scope, $rootScope, $stateParams, entity, Author) {
        var vm = this;
        vm.author = entity;
        
        var unsubscribe = $rootScope.$on('project1App:authorUpdate', function(event, result) {
            vm.author = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
