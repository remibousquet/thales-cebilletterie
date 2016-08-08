(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .controller('EnfantDetailController', EnfantDetailController);

    EnfantDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Enfant', 'User'];

    function EnfantDetailController($scope, $rootScope, $stateParams, previousState, entity, Enfant, User) {
        var vm = this;

        vm.enfant = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('cebilletterieApp:enfantUpdate', function(event, result) {
            vm.enfant = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
