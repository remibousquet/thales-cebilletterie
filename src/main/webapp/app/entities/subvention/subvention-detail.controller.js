(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .controller('SubventionDetailController', SubventionDetailController);

    SubventionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Subvention', 'User'];

    function SubventionDetailController($scope, $rootScope, $stateParams, previousState, entity, Subvention, User) {
        var vm = this;

        vm.subvention = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('cebilletterieApp:subventionUpdate', function(event, result) {
            vm.subvention = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
