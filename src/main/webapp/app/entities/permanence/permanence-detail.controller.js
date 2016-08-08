(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .controller('PermanenceDetailController', PermanenceDetailController);

    PermanenceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Permanence', 'User'];

    function PermanenceDetailController($scope, $rootScope, $stateParams, previousState, entity, Permanence, User) {
        var vm = this;

        vm.permanence = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('cebilletterieApp:permanenceUpdate', function(event, result) {
            vm.permanence = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
