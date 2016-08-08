(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .controller('PaiementDetailController', PaiementDetailController);

    PaiementDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Paiement'];

    function PaiementDetailController($scope, $rootScope, $stateParams, previousState, entity, Paiement) {
        var vm = this;

        vm.paiement = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('cebilletterieApp:paiementUpdate', function(event, result) {
            vm.paiement = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
