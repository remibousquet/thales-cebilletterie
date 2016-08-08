(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .controller('BilletDetailController', BilletDetailController);

    BilletDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Billet', 'TypeBillet', 'TitreTypeBillet'];

    function BilletDetailController($scope, $rootScope, $stateParams, previousState, entity, Billet, TypeBillet, TitreTypeBillet) {
        var vm = this;

        vm.billet = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('cebilletterieApp:billetUpdate', function(event, result) {
            vm.billet = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
